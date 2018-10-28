package de.karstenkoehler.bridges.ui.shapes;

import de.karstenkoehler.bridges.InvalidBridgeCountException;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Island;
import de.karstenkoehler.bridges.model.Orientation;
import de.karstenkoehler.bridges.ui.CanvasController;
import de.karstenkoehler.bridges.ui.MainController;
import de.karstenkoehler.bridges.ui.NumberDisplay;
import de.karstenkoehler.bridges.ui.ParameterObject;
import javafx.event.Event;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import static de.karstenkoehler.bridges.ui.CanvasController.EVAL_STATE;

public class IslandShape {
    private static final Color FILL_COLOR = Color.BLACK;

    private final Island island;
    private final Pane controlPane;
    private final GraphicsContext gc;
    private final ParameterObject params;
    private final BridgesPuzzle puzzle;
    private NumberDisplay display;

    public IslandShape(Island island, Pane controlPane, GraphicsContext gc, ParameterObject params, BridgesPuzzle puzzle) {
        this.island = island;
        this.controlPane = controlPane;
        this.gc = gc;
        this.params = params;
        this.puzzle = puzzle;
        this.display = NumberDisplay.SHOW_REQUIRED;

        initControls(params.coordinate(island.getX()), params.coordinate(island.getY()));
    }

    public void draw(boolean drawClickArea) {
        double x = params.coordinate(island.getX());
        double y = params.coordinate(island.getY());

        if (puzzle.getRemainingBridgeCount(island) > 0) {
            gc.setFill(Color.BLUE);
        } else if (puzzle.getRemainingBridgeCount(island) == 0) {
            gc.setFill(Color.GREEN);
        } else {
            gc.setFill(Color.RED);
        }

        gc.fillOval(x - params.getIslandOffset(), y - params.getIslandOffset(), params.getIslandDiameter(), params.getIslandDiameter());

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(Font.font(params.getFontSize()));
        gc.setFill(Color.BLACK);
        gc.fillText(getDisplayNumber(), x, y);

        if (drawClickArea) {
            drawClickArea();
        }

    }

    private String getDisplayNumber () {
        if (this.display == NumberDisplay.SHOW_REQUIRED) {
            return String.valueOf(island.getRequiredBridges());
        } else if (this.display == NumberDisplay.SHOW_REMAINING) {
            return String.valueOf(puzzle.getRemainingBridgeCount(this.island));
        }

        throw new RuntimeException("IslandShape#getDisplayNumber: found invalid enum value");
    }

    private void initControls(double x, double y) {
        final double x0 = x - params.getClickAreaSize();
        final double x1 = x + params.getClickAreaSize();
        final double y0 = y - params.getClickAreaSize();
        final double y1 = y + params.getClickAreaSize();

        createTriangle(Orientation.NORTH, x, y, x1, y0, x0, y0);
        createTriangle(Orientation.EAST, x, y, x1, y0, x1, y1);
        createTriangle(Orientation.SOUTH, x, y, x1, y1, x0, y1);
        createTriangle(Orientation.WEST, x, y, x0, y1, x0, y0);
    }

    private void createTriangle(Orientation orientation, double x0, double y0, double x1, double y1, double x2, double y2) {
        Polygon poly = new Polygon(x0, y0, x1, y1, x2, y2);
        poly.setStroke(Color.TRANSPARENT);
        poly.setFill(Color.TRANSPARENT);
        poly.setOnMouseEntered(event -> poly.setFill(FILL_COLOR));
        poly.setOnMouseExited(event -> poly.setFill(Color.TRANSPARENT));
        poly.setOnMouseClicked(event -> {
            int count = event.getButton().equals(MouseButton.PRIMARY) ? 1 : -1;
            onClick(poly, orientation, count);
        });
        this.controlPane.getChildren().add(0, poly);
    }

    private void onClick(Polygon poly, Orientation orientation, int count) {
        try {
            this.puzzle.getConnectedBridge(this.island, orientation).addBridges(count);
            this.puzzle.emphasizeBridge(this.puzzle.getConnectedBridge(this.island, orientation));
            poly.fireEvent(new Event(MainController.FILE_CHANGED));
            poly.fireEvent(new Event(EVAL_STATE));
            poly.fireEvent(new Event(CanvasController.REDRAW));

        } catch (NullPointerException | InvalidBridgeCountException e) {
            poly.fireEvent(new Event(CanvasController.ERROR));
        }
    }

    private void drawClickArea() {
        final double x = params.coordinate(island.getX());
        final double y = params.coordinate(island.getY());

        final double x0 = x - params.getClickAreaSize();
        final double x1 = x + params.getClickAreaSize();
        final double y0 = y - params.getClickAreaSize();
        final double y1 = y + params.getClickAreaSize();

        gc.setStroke(Color.RED);
        gc.setLineWidth(1);

        // diagonals
        gc.strokeLine(x0, y0, x1, y1);
        gc.strokeLine(x0, y1, x1, y0);

        // square
        gc.strokeLine(x0, y0, x1, y0);
        gc.strokeLine(x0, y0, x0, y1);
        gc.strokeLine(x0, y1, x1, y1);
        gc.strokeLine(x1, y1, x1, y0);
    }

    public void setNumberDisplay (NumberDisplay display) {
        this.display = display;
    }
}
