package de.karstenkoehler.bridges.ui;

import de.karstenkoehler.bridges.model.Edge;
import de.karstenkoehler.bridges.model.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Map;

public class BridgeLine {

    private final Edge bridge;
    private final GraphicsContext cg;
    private final Map<Integer, Node> islands;
    private final ParameterObject params;

    public BridgeLine (Edge bridge, GraphicsContext cg, Map<Integer, Node> islands, ParameterObject params) {
        this.bridge = bridge;
        this.cg = cg;
        this.islands = islands;
        this.params = params;
        drawBridge(0);
        drawBridge(-params.getDoubleBridgeOffset());
        drawBridge(params.getDoubleBridgeOffset());
    }

    public void draw () {
        if (bridge.getBridgeCount() == 1) {
            drawBridge(0);
        } else {
            drawBridge(-params.getDoubleBridgeOffset());
            drawBridge(params.getDoubleBridgeOffset());
        }
    }

    private void drawBridge (final double offset) {
        final double x0 = coordinate(this.islands.get(this.bridge.getNode1()).getX());
        final double y0 = coordinate(this.islands.get(this.bridge.getNode1()).getY());
        final double x1 = coordinate(this.islands.get(this.bridge.getNode2()).getX());
        final double y1 = coordinate(this.islands.get(this.bridge.getNode2()).getY());

        if (isVertical()) {
            createLine(x0 + offset, y0, x1 + offset, y1);
        } else if (isHorizontal()) {
            createLine(x0, y0 + offset, x1, y1 + offset);
        } else {
            throw new RuntimeException("fatal error: bridge is neither vertical nor horizontal");
        }
    }

    private void createLine (double x0, double y0, double x1, double y1) {
        cg.setStroke(Color.BLACK);
        cg.setLineWidth(params.getBridgeLineSize());
        cg.strokeLine(x0, y0, x1, y1);

//
//        Polygon line = new Polygon(x0, y0, x1, y1, x2, y2, x3, y3);
//        line.setStroke(Color.BLACK);
//        line.setFill(Color.BLACK);
//        line.setStrokeWidth(1);
//        line.setOnMouseEntered(event -> line.setStroke(Color.RED));
//        line.setOnMouseExited(event -> line.setStroke(Color.BLACK));
//        line.setOnMouseClicked(event -> System.out.println(bridge.getId()));
//        this.pane.getChildren().add(0, line);
    }

    private boolean isVertical () {
        return islands.get(bridge.getNode1()).getX() == islands.get(bridge.getNode2()).getX();
    }

    private boolean isHorizontal () {
        return islands.get(bridge.getNode1()).getY() == islands.get(bridge.getNode2()).getY();
    }

    private double coordinate (int i) {
        return (i * (params.getFieldSize())) + params.getPadding();
    }

    public void clear () {

    }
}
