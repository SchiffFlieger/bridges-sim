package de.karstenkoehler.bridges.ui;

import de.karstenkoehler.bridges.io.BridgesFileReader;
import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.parser.ParseException;
import de.karstenkoehler.bridges.io.validators.ValidateException;
import de.karstenkoehler.bridges.model.Edge;
import de.karstenkoehler.bridges.model.Node;
import de.karstenkoehler.bridges.ui.shapes.IslandCircle;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CanvasController {
    public static final EventType<Event> REDRAW = new EventType<>("REDRAW");
    public static final EventType<Event> ERROR = new EventType<>("ERROR");

    private final Canvas canvas;
    private final Pane controlPane;

    private boolean gridVisible;
    private boolean clickAreaVisible;

    private List<IslandCircle> islands;
    private List<BridgeLine> bridges;
    private ParameterObject params;

    public CanvasController(Canvas canvas, Pane controlPane) {
        this.canvas = canvas;
        this.controlPane = controlPane;
        this.islands = new ArrayList<>();
        this.bridges = new ArrayList<>();

        this.canvas.addEventHandler(REDRAW, event -> drawThings());
        this.canvas.addEventHandler(ERROR, event -> System.out.println("could not draw bridge"));


        File file = new File("src\\main\\resources\\data\\bsp_5x5.bgs");
        openAndShowFile(file);
    }

    public void drawThings() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawCanvasBorder(gc);
        drawGrid(gc);
        for (int i = 0; i < this.bridges.size(); i++) {
            this.bridges.get(i).draw();
        }
        for (int i = 0; i < this.islands.size(); i++) {
            this.islands.get(i).draw(this.clickAreaVisible);
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
        final double x1 = canvas.getWidth() - params.getPadding();
        final double y1 = canvas.getHeight() - params.getPadding();

        gc.setStroke(Color.RED);
        gc.setLineWidth(lineWidth);

        for (int i = 0; i < params.getGridLines(); i++) {
            final double val = params.coordinate(i);
            gc.strokeLine(params.getPadding(), val, x1, val);
            gc.strokeLine(val, params.getPadding(), val, y1);
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

    public void openFile(File file) {
        clearEverything();
        openAndShowFile(file);
    }

    private void openAndShowFile(File file) {
        ParseResult result = tryReadFile(file);
        if (result == null) {
            return;
        }

        this.params = new ParameterObject(Math.max(result.getWidth(), result.getHeight()), this.canvas.getWidth());

        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (Node island : result.getIslands()) {
            this.islands.add(new IslandCircle(this.canvas, island, controlPane, gc, params, result));
        }
        for (Edge bridge : result.getBridges()) {
            this.bridges.add(new BridgeLine(bridge, canvas.getGraphicsContext2D(), result.getIslands(), params, result));
        }
        drawThings();
    }

    private ParseResult tryReadFile(File file) {
        ParseResult result = null;
        try {
            result = new BridgesFileReader().readFile(file);
        } catch (ParseException e) {
            Alert error = new Alert(Alert.AlertType.ERROR, "syntactic error in file:\n" + e.getMessage());
            error.showAndWait();
        } catch (ValidateException e) {
            Alert error = new Alert(Alert.AlertType.ERROR, "semantic error in file:\n" + e.getMessage());
            error.showAndWait();
        } catch (IOException e) {
            Alert error = new Alert(Alert.AlertType.ERROR, "could not read file:\n" + e.getMessage());
            error.showAndWait();
        }
        return result;
    }
}
