package de.karstenkoehler.bridges.ui.shapes;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.Island;
import de.karstenkoehler.bridges.ui.BridgeHintsVisible;
import de.karstenkoehler.bridges.ui.CanvasDimensions;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BridgeShape {

    private final Connection connection;
    private final GraphicsContext cg;
    private final CanvasDimensions dimensions;
    private final BridgesPuzzle puzzle;

    public BridgeShape(Connection connection, GraphicsContext cg, CanvasDimensions dimensions, BridgesPuzzle puzzle) {
        this.connection = connection;
        this.cg = cg;
        this.dimensions = dimensions;
        this.puzzle = puzzle;
    }

    public void draw(BridgeHintsVisible hintsVisible) {
        if (connection.getBridgeCount() <= 1) {
            drawBridge(0, hintsVisible);
        } else {
            drawBridge(-dimensions.getDoubleBridgeOffset(), hintsVisible);
            drawBridge(dimensions.getDoubleBridgeOffset(), hintsVisible);
        }
    }

    private void drawBridge(final double offset, BridgeHintsVisible hintsVisible) {
        if (this.connection.getBridgeCount() == 0 && hintsVisible == BridgeHintsVisible.NEVER) {
            return;
        }

        final double x0 = dimensions.coordinate(this.connection.getStartIsland().getX());
        final double y0 = dimensions.coordinate(this.connection.getStartIsland().getY());
        final double x1 = dimensions.coordinate(this.connection.getEndIsland().getX());
        final double y1 = dimensions.coordinate(this.connection.getEndIsland().getY());

        if (connection.isVertical()) {
            createLine(x0 + offset, y0, x1 + offset, y1, hintsVisible);
        } else if (connection.isHorizontal()) {
            createLine(x0, y0 + offset, x1, y1 + offset, hintsVisible);
        } else {
            throw new RuntimeException("fatal error: connection is neither vertical nor horizontal");
        }
    }

    private void createLine(double x0, double y0, double x1, double y1, BridgeHintsVisible hintsVisible) {
        cg.setLineWidth(dimensions.getBridgeLineSize());

        if (connection.getBridgeCount() == 0) {
            if (showBridgeHint(hintsVisible)) {
                cg.setLineDashes(dimensions.getBridgeLineSize() * 5);
                cg.setStroke(Color.LIGHTGRAY);
                cg.strokeLine(x0, y0, x1, y1);
            }
        } else {
            cg.setLineDashes(0);
            if (connection.isValid()) {
                if (connection.isEmphasized()) {
                    cg.setStroke(Color.GREEN);
                } else {
                    cg.setStroke(Color.BLACK);
                }
            } else {
                cg.setStroke(Color.RED);
            }
            cg.strokeLine(x0, y0, x1, y1);
        }
    }

    private boolean showBridgeHint(BridgeHintsVisible hintsVisible) {
        return hintsVisible == BridgeHintsVisible.ALWAYS
                || (!puzzle.causesCrossing(this.connection) && needsMoreBridges(connection.getStartIsland()) && needsMoreBridges(connection.getEndIsland()));
    }

    private boolean needsMoreBridges(Island island) {
        return puzzle.getRemainingBridgeCount(island) != 0;
    }
}
