package de.karstenkoehler.bridges.ui.shapes;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.Island;
import de.karstenkoehler.bridges.ui.BridgeHintsVisible;
import de.karstenkoehler.bridges.ui.CanvasDimensions;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * The graphical representation of a single or double bridge. Every instance of this class is tied to
 * an instance of {@link Connection}. Bridges are drawn on a canvas.
 */
public class BridgeShape {

    private final Connection connection;
    private final GraphicsContext cg;
    private final CanvasDimensions dimensions;
    private final BridgesPuzzle puzzle;

    /**
     * Creates a new bridge shape and ties it to a connection.
     *
     * @param connection the connection to bind to
     * @param cg         the graphics context of the canvas to draw on
     * @param dimensions the calculated dimensions for the puzzle
     * @param puzzle     the puzzle of the connection
     */
    public BridgeShape(Connection connection, GraphicsContext cg, CanvasDimensions dimensions, BridgesPuzzle puzzle) {
        this.connection = connection;
        this.cg = cg;
        this.dimensions = dimensions;
        this.puzzle = puzzle;
    }

    /**
     * Redraws a single or a double bridge on the canvas, depending on the number of bridges in
     * the underlying connection. If there is no bridge in the connection there may be drawn
     * bridge hints.
     *
     * @param hintsVisible the configuration for bridge hints
     */
    public void draw(BridgeHintsVisible hintsVisible) {
        if (connection.getBridgeCount() <= 1) {
            drawBridge(0, hintsVisible);
        } else {
            drawBridge(-dimensions.getDoubleBridgeOffset(), hintsVisible);
            drawBridge(dimensions.getDoubleBridgeOffset(), hintsVisible);
        }
    }

    /**
     * Redraws a single bridge line on the canvas.
     *
     * @param offset       the offset of the bridge
     * @param hintsVisible the configuration for bridge hints
     */
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

    /**
     * Draws a single line on the canvas.
     *
     * @param x0           the starting x coordinate
     * @param y0           the starting y coordinate
     * @param x1           the ending x coordinate
     * @param y1           the ending y coordinate
     * @param hintsVisible the configuration for bridge hints
     */
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

    /**
     * Returns true if bridge hints should be drawn.
     *
     * @param hintsVisible the configuration for bridge hints
     * @return true if bridge hints should be drawn
     */
    private boolean showBridgeHint(BridgeHintsVisible hintsVisible) {
        return hintsVisible == BridgeHintsVisible.ALWAYS
                || (!puzzle.causesCrossing(this.connection) && needsMoreBridges(connection.getStartIsland()) && needsMoreBridges(connection.getEndIsland()));
    }

    /**
     * Returns true if the given island has not enough bridges.
     *
     * @param island the island to check
     * @return true if the given island has not enough bridges.
     */
    private boolean needsMoreBridges(Island island) {
        return puzzle.getRemainingBridgeCount(island) != 0;
    }
}
