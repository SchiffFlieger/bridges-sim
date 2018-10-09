package de.karstenkoehler.bridges.ui;

import de.karstenkoehler.bridges.model.Edge;
import de.karstenkoehler.bridges.model.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.Map;

public class BridgeLine {

    private final Edge bridge;
    private final Pane pane;
    private final Map<Integer, Node> islands;
    private final ParameterObject params;

    private final Polygon singleBridge;
    private final Polygon doubleBridge1;
    private final Polygon doubleBridge2;

    public BridgeLine(Edge bridge, Pane pane, Map<Integer, Node> islands, ParameterObject params) {
        this.bridge = bridge;
        this.pane = pane;
        this.islands = islands;
        this.params = params;
        singleBridge = createDoubleBridge(0);
        doubleBridge1 = createDoubleBridge(-20);
        doubleBridge2 = createDoubleBridge(+20);
    }

    public void draw() {
        if (bridge.getBridgeCount() == 0) {
            singleBridge.setVisible(false);
            doubleBridge1.setVisible(false);
            doubleBridge2.setVisible(false);
        } else if (bridge.getBridgeCount() == 1) {
            singleBridge.setVisible(true);
            doubleBridge1.setVisible(false);
            doubleBridge2.setVisible(false);
        } else {
            singleBridge.setVisible(false);
            doubleBridge1.setVisible(true);
            doubleBridge2.setVisible(true);
        }
    }

    private Polygon createDoubleBridge(final double offset) {
        final double x0 = coordinate(this.islands.get(this.bridge.getNode1()).getX());
        final double y0 = coordinate(this.islands.get(this.bridge.getNode1()).getY());
        final double x1 = coordinate(this.islands.get(this.bridge.getNode2()).getX());
        final double y1 = coordinate(this.islands.get(this.bridge.getNode2()).getY());
        final double size = 5;

        if (isVertical()) {
            return createLine(x0 - size + offset, y0, x0 + size + offset, y0, x1 + size + offset, y1, x1 - size + offset, y1);
        } else if (isHorizontal()) {
            return createLine(x0, y0 - size + offset, x1, y1 - size + offset, x1, y1 + size + offset, x0, y0 + size + offset);
        } else {
            throw new RuntimeException("fatal error: bridge is neither vertical nor horizontal");
        }
    }

    private Polygon createLine(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3) {
        Polygon line = new Polygon(x0, y0, x1, y1, x2, y2, x3, y3);
        line.setStroke(Color.BLACK);
        line.setFill(Color.BLACK);
        line.setStrokeWidth(1);
        line.setOnMouseEntered(event -> line.setStroke(Color.RED));
        line.setOnMouseExited(event -> line.setStroke(Color.BLACK));
        line.setOnMouseClicked(event -> System.out.println(bridge.getId()));
        this.pane.getChildren().add(0, line);
        return line;
    }

    private boolean isVertical() {
        return islands.get(bridge.getNode1()).getX() == islands.get(bridge.getNode2()).getX();
    }

    private boolean isHorizontal() {
        return islands.get(bridge.getNode1()).getY() == islands.get(bridge.getNode2()).getY();
    }

    private double coordinate(int i) {
        return (i * (params.getFieldSize())) + params.getPadding();
    }

    public void clear() {

    }
}
