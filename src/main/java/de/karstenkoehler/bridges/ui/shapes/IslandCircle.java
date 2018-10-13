package de.karstenkoehler.bridges.ui.shapes;

import de.karstenkoehler.bridges.InvalidBridgeCountException;
import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.model.Node;
import de.karstenkoehler.bridges.model.Orientation;
import de.karstenkoehler.bridges.ui.CanvasController;
import de.karstenkoehler.bridges.ui.ParameterObject;
import javafx.event.Event;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class IslandCircle {
    private static final Color FILL_COLOR = Color.BLACK;

    private final Canvas canvas;
    private final Node island;
    private final Pane controlPane;
    private final GraphicsContext gc;
    private final ParameterObject params;
    private final ParseResult result;

    public IslandCircle(Canvas canvas, Node island, Pane controlPane, GraphicsContext gc, ParameterObject params, ParseResult result) {
        this.canvas = canvas;
        this.island = island;
        this.controlPane = controlPane;
        this.gc = gc;
        this.params = params;
        this.result = result;

        initControls(coordinate(island.getX()), coordinate(island.getY()), island.getId());
    }

    public void draw(boolean drawClickArea) {
        double x = coordinate(island.getX());
        double y = coordinate(island.getY());

        if (result.getRemainingBridgeCount(island) > 0) {
            gc.setFill(Color.BLUE);
        } else if (result.getRemainingBridgeCount(island) == 0) {
            gc.setFill(Color.GREEN);
        } else {
            gc.setFill(Color.RED);
        }

        gc.fillOval(x - params.getIslandOffset(), y - params.getIslandOffset(), params.getIslandDiameter(), params.getIslandDiameter());

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(Font.font(params.getFontSize()));
        gc.setFill(Color.BLACK);
        gc.fillText(String.valueOf(island.getRequiredBridges()), x, y);

        if (drawClickArea) {
            drawClickArea();
        }

    }

    private void initControls(double x, double y, int id) {
        final double x0 = x - params.getClickAreaSize();
        final double x1 = x + params.getClickAreaSize();
        final double y0 = y - params.getClickAreaSize();
        final double y1 = y + params.getClickAreaSize();

        createTriangle(Orientation.NORTH, x, y, x1, y0, x0, y0, id);
        createTriangle(Orientation.EAST, x, y, x1, y0, x1, y1, id);
        createTriangle(Orientation.SOUTH, x, y, x1, y1, x0, y1, id);
        createTriangle(Orientation.WEST, x, y, x0, y1, x0, y0, id);
    }

    private void createTriangle(Orientation orientation, double x0, double y0, double x1, double y1, double x2, double y2, int id) {
        Polygon poly = new Polygon(x0, y0, x1, y1, x2, y2);
        poly.setStroke(Color.TRANSPARENT);
        poly.setFill(Color.TRANSPARENT);
        poly.setOnMouseEntered(event -> poly.setFill(FILL_COLOR));
        poly.setOnMouseExited(event -> poly.setFill(Color.TRANSPARENT));
        poly.setOnMouseClicked(event -> {
            System.out.println(id + " " + orientation);
            int count = event.getButton().equals(MouseButton.PRIMARY) ? 1 : -1;
            onClick(orientation, count);
        });
        this.controlPane.getChildren().add(0, poly);
    }

    private void onClick(Orientation orientation, int count) {
        try {
            this.result.getConnectedBridge(this.island, orientation).addBridges(count);
            canvas.fireEvent(new Event(CanvasController.REDRAW));
        } catch (NullPointerException | InvalidBridgeCountException e) {
            canvas.fireEvent(new Event(CanvasController.ERROR));
        }
    }

    private void drawClickArea() {
        final double x = coordinate(island.getX());
        final double y = coordinate(island.getY());

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

    private double coordinate(int i) {
        return (i * (params.getFieldSize())) + params.getPadding();
    }
}
