package de.karstenkoehler.bridges.ui.shapes;

import de.karstenkoehler.bridges.model.Node;
import de.karstenkoehler.bridges.ui.ParameterObject;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class IslandCircle {
    private static final Color FILL_COLOR = Color.BLACK;

    private final Node island;
    private final Pane controlPane;
    private final GraphicsContext gc;
    private ParameterObject params;

    public IslandCircle(Node island, Pane controlPane, GraphicsContext gc, ParameterObject params) {
        this.island = island;
        this.controlPane = controlPane;
        this.gc = gc;
        this.params = params;

        initControls(coordinate(island.getX()), coordinate(island.getY()), island.getId());
    }

    public void draw(boolean drawClickArea) {
        double x = coordinate(island.getX());
        double y = coordinate(island.getY());
        gc.setFill(Color.GREEN);
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

        createTriangle("north", x, y, x1, y0, x0, y0, id);
        createTriangle("east", x, y, x1, y0, x1, y1, id);
        createTriangle("south", x, y, x1, y1, x0, y1, id);
        createTriangle("west", x, y, x0, y1, x0, y0, id);
    }

    private void createTriangle(String orientation, double x0, double y0, double x1, double y1, double x2, double y2, int id) {
        Polygon north = new Polygon(x0, y0, x1, y1, x2, y2);
        north.setStroke(Color.TRANSPARENT);
        north.setFill(Color.TRANSPARENT);
        north.setOnMouseEntered(event -> north.setFill(FILL_COLOR));
        north.setOnMouseExited(event -> north.setFill(Color.TRANSPARENT));
        north.setOnMouseClicked(event -> System.out.println(id + " " + orientation));
        this.controlPane.getChildren().add(0, north);
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
