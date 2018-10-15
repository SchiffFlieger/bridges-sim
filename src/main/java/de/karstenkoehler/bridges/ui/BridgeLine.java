package de.karstenkoehler.bridges.ui;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.model.Edge;
import de.karstenkoehler.bridges.model.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class BridgeLine {

    private final Edge bridge;
    private final GraphicsContext cg;
    private final List<Node> islands;
    private final ParameterObject params;
    private final ParseResult result;

    public BridgeLine(Edge bridge, GraphicsContext cg, List<Node> islands, ParameterObject params, ParseResult result) {
        this.bridge = bridge;
        this.cg = cg;
        this.islands = islands;
        this.params = params;
        this.result = result;
        drawBridge(0);
        drawBridge(-params.getDoubleBridgeOffset());
        drawBridge(params.getDoubleBridgeOffset());
    }

    public void draw () {
        if (bridge.getBridgeCount() <= 1) {
            drawBridge(0);
        } else {
            drawBridge(-params.getDoubleBridgeOffset());
            drawBridge(params.getDoubleBridgeOffset());
        }
    }

    private void drawBridge (final double offset) {
        final double x0 = params.coordinate(this.islands.get(this.bridge.getNode1()).getX());
        final double y0 = params.coordinate(this.islands.get(this.bridge.getNode1()).getY());
        final double x1 = params.coordinate(this.islands.get(this.bridge.getNode2()).getX());
        final double y1 = params.coordinate(this.islands.get(this.bridge.getNode2()).getY());

        if (result.isVertical(bridge)) {
            createLine(x0 + offset, y0, x1 + offset, y1);
        } else if (result.isHorizontal(bridge)) {
            createLine(x0, y0 + offset, x1, y1 + offset);
        } else {
            throw new RuntimeException("fatal error: bridge is neither vertical nor horizontal");
        }
    }

    private void createLine (double x0, double y0, double x1, double y1) {
        if (bridge.getBridgeCount() == 0) {
            cg.setLineDashes(params.getBridgeLineSize()*5);
        } else {
            cg.setLineDashes(0);
        }
        if (bridge.isValid()) {
            cg.setStroke(Color.BLACK);
        } else {
            cg.setStroke(Color.RED);
        }
        cg.setLineWidth(params.getBridgeLineSize());
        cg.strokeLine(x0, y0, x1, y1);
    }
}
