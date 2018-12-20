package de.karstenkoehler.bridges.ui.shapes;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.Direction;
import de.karstenkoehler.bridges.model.Island;
import de.karstenkoehler.bridges.ui.CanvasController;
import de.karstenkoehler.bridges.ui.CanvasDimensions;
import de.karstenkoehler.bridges.ui.MainController;
import de.karstenkoehler.bridges.ui.NumberDisplay;
import javafx.event.Event;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.function.Consumer;
import java.util.function.Function;

import static de.karstenkoehler.bridges.ui.CanvasController.EVAL_STATE;

public class IslandShape {
    private static final Color FILL_COLOR = Color.BLACK;

    private final Island island;
    private final Pane controlPane;
    private final GraphicsContext gc;
    private final CanvasDimensions dimensions;
    private final BridgesPuzzle puzzle;

    public IslandShape(Island island, Pane controlPane, GraphicsContext gc, CanvasDimensions dimensions, BridgesPuzzle puzzle) {
        this.island = island;
        this.controlPane = controlPane;
        this.gc = gc;
        this.dimensions = dimensions;
        this.puzzle = puzzle;

        initControls(dimensions.coordinate(island.getX()), dimensions.coordinate(island.getY()));
    }

    public void draw(NumberDisplay numberDisplay, boolean drawClickArea) {
        double x = dimensions.coordinate(island.getX());
        double y = dimensions.coordinate(island.getY());

        if (puzzle.getRemainingBridgeCount(island) > 0) {
            gc.setFill(Color.GRAY);
        } else if (puzzle.getRemainingBridgeCount(island) == 0) {
            gc.setFill(Color.GREEN);
        } else {
            gc.setFill(Color.RED);
        }

        gc.fillOval(x - dimensions.getIslandOffset(), y - dimensions.getIslandOffset(), dimensions.getIslandDiameter(), dimensions.getIslandDiameter());

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(Font.font(dimensions.getFontSize()));
        gc.setFill(Color.BLACK);
        gc.fillText(getDisplayNumber(numberDisplay), x, y);

        if (drawClickArea) {
            drawClickArea();
        }

    }

    private String getDisplayNumber(NumberDisplay display) {
        if (display == NumberDisplay.SHOW_REQUIRED) {
            return String.valueOf(island.getRequiredBridges());
        } else if (display == NumberDisplay.SHOW_REMAINING) {
            return String.valueOf(puzzle.getRemainingBridgeCount(this.island));
        }

        throw new RuntimeException("IslandShape#getDisplayNumber: found invalid enum value");
    }

    private void initControls(double x, double y) {
        final double x0 = x - dimensions.getClickAreaSize();
        final double x1 = x + dimensions.getClickAreaSize();
        final double y0 = y - dimensions.getClickAreaSize();
        final double y1 = y + dimensions.getClickAreaSize();

        createTriangle(Direction.NORTH, x, y, x1, y0, x0, y0);
        createTriangle(Direction.EAST, x, y, x1, y0, x1, y1);
        createTriangle(Direction.SOUTH, x, y, x1, y1, x0, y1);
        createTriangle(Direction.WEST, x, y, x0, y1, x0, y0);
    }

    private void createTriangle(Direction direction, double x0, double y0, double x1, double y1, double x2, double y2) {
        Polygon poly = new Polygon(x0, y0, x1, y1, x2, y2);
        poly.setStroke(Color.TRANSPARENT);
        poly.setFill(Color.TRANSPARENT);
        poly.setOnMouseEntered(event -> poly.setFill(FILL_COLOR));
        poly.setOnMouseExited(event -> poly.setFill(Color.TRANSPARENT));
        poly.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                onLeftClick(poly, direction);
            } else {
                onRightClick(poly, direction);
            }
        });
        this.controlPane.getChildren().add(0, poly);
    }

    private void onLeftClick(Polygon poly, Direction direction) {
        onClick(poly, direction, Connection::canAddBridge, Connection::addBridge);
    }

    private void onRightClick(Polygon poly, Direction direction) {
        onClick(poly, direction, Connection::canRemoveBridge, Connection::removeBridge);
    }

    private void onClick(Polygon poly, Direction direction, Function<Connection, Boolean> canExec, Consumer<Connection> exec) {
        Connection connection = this.puzzle.getConnectedBridge(this.island, direction);
        if (connection == null) {
            return;
        }

        if (canExec.apply(connection)) {
            exec.accept(connection);
        }
        this.puzzle.emphasizeBridge(connection);

        poly.fireEvent(new Event(MainController.FILE_CHANGED));
        poly.fireEvent(new Event(EVAL_STATE));
        poly.fireEvent(new Event(CanvasController.REDRAW));
    }

    private void drawClickArea() {
        final double x = dimensions.coordinate(island.getX());
        final double y = dimensions.coordinate(island.getY());

        final double x0 = x - dimensions.getClickAreaSize();
        final double x1 = x + dimensions.getClickAreaSize();
        final double y0 = y - dimensions.getClickAreaSize();
        final double y1 = y + dimensions.getClickAreaSize();

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
}
