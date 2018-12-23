package de.karstenkoehler.bridges.ui;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.Island;
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

public class CanvasController {

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

    public CanvasController(Canvas canvas, Pane controlPane, IslandShape.NumberDisplay currentDisplay) {
        this.canvas = canvas;
        this.controlPane = controlPane;
        this.numberDisplay = currentDisplay;
        this.islands = new ArrayList<>();
        this.bridges = new ArrayList<>();
    }

    public void drawThings() {
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

    public void setGridVisible(boolean visible) {
        this.gridVisible = visible;
        this.drawThings();
    }

    public void setClickAreaVisible(boolean visible) {
        this.clickAreaVisible = visible;
        this.drawThings();
    }

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

    private void clearEverything() {
        this.controlPane.getChildren().clear();
        this.islands.clear();
        this.bridges.clear();
    }

    public void setNumberDisplay(IslandShape.NumberDisplay display) {
        this.numberDisplay = display;
    }

    public void restartPuzzle() {
        this.puzzle.restart();
        canvas.fireEvent(new Event(EventTypes.EVAL_STATE));
        canvas.fireEvent(new Event(EventTypes.FILE_MODIFIED));
        this.drawThings();
    }

    public void setPuzzle(BridgesPuzzle puzzle) {
        this.puzzle = puzzle;

        clearEverything();
        this.dimensions = new CanvasDimensions(Math.max(puzzle.getWidth(), puzzle.getHeight()), this.canvas.getWidth());

        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (Island island : puzzle.getIslands()) {
            this.islands.add(new IslandShape(island, controlPane, gc, dimensions, puzzle));
        }
        for (Connection connection : puzzle.getConnections()) {
            this.bridges.add(new BridgeShape(connection, canvas.getGraphicsContext2D(), dimensions, puzzle));
        }
        drawThings();

        this.canvas.fireEvent(new Event(EventTypes.EVAL_STATE));
    }

    public BridgesPuzzle getPuzzle() {
        return this.puzzle;
    }

    public void setBridgeHintsVisible(BridgeShape.BridgeHintsVisible visible) {
        this.bridgeHintsVisible = visible;
    }
}
