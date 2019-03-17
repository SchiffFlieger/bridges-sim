package de.karstenkoehler.bridges.ui;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.Island;
import de.karstenkoehler.bridges.model.PuzzleState;
import de.karstenkoehler.bridges.ui.events.EventTypes;
import de.karstenkoehler.bridges.ui.shapes.BridgeShape;
import de.karstenkoehler.bridges.ui.shapes.IslandShape;
import javafx.event.Event;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * The controller class for the canvas. It manages everything that happens in the area
 * where the puzzle is displayed. The controller is responsible for drawing the game
 * objects on the canvas and managing the shapes for user input.
 */
public class PlayingFieldController {

    private final Canvas canvas;
    private final Pane controlPane;

    private boolean gridVisible;
    private boolean clickAreaVisible;

    private List<IslandShape> islands;
    private List<BridgeShape> bridges;
    private CanvasDimensions dimensions;
    private BridgesPuzzle puzzle;
    private BridgeShape.BridgeHintsVisible bridgeHintsVisible;
    private IslandShape.NumberDisplay numberDisplay;

    /**
     * Creates a new playing field controller.
     *
     * @param canvas         the canvas to draw on
     * @param controlPane    the pane for holding the click shapes
     * @param currentDisplay the currently selected number display configuration
     */
    public PlayingFieldController(Canvas canvas, Pane controlPane, IslandShape.NumberDisplay currentDisplay) {
        this.canvas = canvas;
        this.controlPane = controlPane;
        this.numberDisplay = currentDisplay;
        this.islands = new ArrayList<>();
        this.bridges = new ArrayList<>();
    }

    /**
     * Draws all the islands and bridges from the current puzzle
     * onto the canvas.
     */
    public void draw() {
        if (this.puzzle == null) {
            return;
        }

        this.puzzle.markInvalidBridges();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawCanvasBorder(gc);
        drawGrid(gc);
        for (BridgeShape bridge : this.bridges) {
            bridge.draw(this.bridgeHintsVisible);
        }
        for (IslandShape island : this.islands) {
            island.draw(this.numberDisplay, this.clickAreaVisible);
        }
    }

    /**
     * Toggles the visibility of the underlying grid, that the islands are
     * aligned to.
     *
     * @param visible true if the grid should be visible
     */
    public void setGridVisible(boolean visible) {
        this.gridVisible = visible;
        this.draw();
    }

    /**
     * Toggles the visibility of the click areas surrounding the islands.
     *
     * @param visible true if the click areas should be visible
     */
    public void setClickAreaVisible(boolean visible) {
        this.clickAreaVisible = visible;
        this.draw();
    }

    /**
     * Sets the configuration for the numbers to draw on islands.
     *
     * @param display the configuration for the numbers to draw on islands
     */
    public void setNumberDisplay(IslandShape.NumberDisplay display) {
        this.numberDisplay = display;
    }

    /**
     * Sets the configuration for the visibility of bridge hints.
     *
     * @param visible the configuration for the visibility of bridge hints
     */
    public void setBridgeHintsVisible(BridgeShape.BridgeHintsVisible visible) {
        this.bridgeHintsVisible = visible;
    }

    /**
     * Changes the active puzzle.
     *
     * @param puzzle the puzzle to display
     */
    public void setPuzzle(BridgesPuzzle puzzle) {
        this.puzzle = puzzle;

        clearClickShapes();
        this.dimensions = new CanvasDimensions(Math.max(puzzle.getWidth(), puzzle.getHeight()), this.canvas.getWidth());

        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (Island island : puzzle.getIslands()) {
            this.islands.add(new IslandShape(island, controlPane, gc, dimensions, puzzle));
        }
        for (Connection connection : puzzle.getConnections()) {
            this.bridges.add(new BridgeShape(connection, canvas.getGraphicsContext2D(), dimensions, puzzle));
        }
        draw();

        this.canvas.fireEvent(new Event(EventTypes.EVAL_STATE));
    }

    /**
     * Returns the currently active puzzle.
     *
     * @return the currently active puzzle
     */
    public BridgesPuzzle getPuzzle() {
        return this.puzzle;
    }

    /**
     * Restart the currently active puzzle by deleting all existing bridges.
     */
    public void restartPuzzle() {
        this.puzzle.restart();
        canvas.fireEvent(new Event(EventTypes.EVAL_STATE));
        canvas.fireEvent(new Event(EventTypes.FILE_MODIFIED));
        this.draw();
    }

    /**
     * Returns the current puzzle state. If no puzzle is loaded, returns the {@link PuzzleState#NOT_LOADED} state.
     *
     * @return the current puzzle state
     */
    public PuzzleState getPuzzleState() {
        if (this.puzzle == null) {
            return PuzzleState.NOT_LOADED;
        }

        return this.puzzle.getState();
    }

    /**
     * Draws the outlines of the canvas.
     *
     * @param gc the graphics context of the canvas to draw on
     */
    private void drawCanvasBorder(GraphicsContext gc) {
        gc.setStroke(Color.DARKGRAY);
        gc.setLineDashes(0);
        gc.setLineWidth(3);
        gc.strokePolygon(
                new double[]{0, canvas.getWidth(), canvas.getWidth(), 0},
                new double[]{0, 0, canvas.getHeight(), canvas.getHeight()},
                4
        );
    }

    /**
     * Draws the grid that the islands are placed on.
     *
     * @param gc the graphics context of the canvas to draw on
     */
    private void drawGrid(GraphicsContext gc) {
        if (!gridVisible) {
            return;
        }

        final int lineWidth = 1;
        final double x1 = canvas.getWidth() - dimensions.getPadding();
        final double y1 = canvas.getHeight() - dimensions.getPadding();

        gc.setStroke(Color.RED);
        gc.setLineWidth(lineWidth);

        for (int i = 0; i < dimensions.getGridLines(); i++) {
            final double val = dimensions.coordinate(i);
            gc.strokeLine(dimensions.getPadding(), val, x1, val);
            gc.strokeLine(val, dimensions.getPadding(), val, y1);
        }
    }

    /**
     * Deletes all existing shapes that handle user input. Call this method
     * before loading a new puzzle to the playing field.
     */
    private void clearClickShapes() {
        this.controlPane.getChildren().clear();
        this.islands.clear();
        this.bridges.clear();
    }
}
