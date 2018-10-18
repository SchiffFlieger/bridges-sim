package de.karstenkoehler.bridges.ui.shapes;

import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.ui.ParameterObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BridgeShape {

    private final Bridge bridge;
    private final GraphicsContext cg;
    private final ParameterObject params;
    private final BridgesPuzzle puzzle;

    public BridgeShape(Bridge bridge, GraphicsContext cg, ParameterObject params, BridgesPuzzle puzzle) {
        this.bridge = bridge;
        this.cg = cg;
        this.params = params;
        this.puzzle = puzzle;
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
        final double x0 = params.coordinate(this.bridge.getStartIsland().getX());
        final double y0 = params.coordinate(this.bridge.getStartIsland().getY());
        final double x1 = params.coordinate(this.bridge.getEndIsland().getX());
        final double y1 = params.coordinate(this.bridge.getEndIsland().getY());

        if (puzzle.isVertical(bridge)) {
            createLine(x0 + offset, y0, x1 + offset, y1);
        } else if (puzzle.isHorizontal(bridge)) {
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
            if (bridge.isEmphasized()) {
                cg.setStroke(Color.GREEN);
            } else {
                cg.setStroke(Color.BLACK);
            }
        } else {
            cg.setStroke(Color.RED);
        }
        cg.setLineWidth(params.getBridgeLineSize());
        cg.strokeLine(x0, y0, x1, y1);
    }
}
