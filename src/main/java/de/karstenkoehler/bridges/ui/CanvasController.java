package de.karstenkoehler.bridges.ui;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.ValidateException;
import de.karstenkoehler.bridges.io.parser.ParseException;
import de.karstenkoehler.bridges.io.parser.Parser;
import de.karstenkoehler.bridges.io.parser.TokenConsumingParser;
import de.karstenkoehler.bridges.io.parser.token.TokenizerImpl;
import de.karstenkoehler.bridges.io.validators.DefaultValidator;
import de.karstenkoehler.bridges.io.validators.Validator;
import de.karstenkoehler.bridges.model.Node;
import de.karstenkoehler.bridges.ui.shapes.IslandCircle;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CanvasController {


    private final Canvas canvas;

    private boolean gridVisible;
    private boolean clickAreaVisible;

    private List<IslandCircle> islands;
    private final ParameterObject params;

    public CanvasController(Canvas canvas, Pane pane) {
        this.canvas = canvas;
        this.islands = new ArrayList<>();

        ParseResult result = getParseResult();
        params = new ParameterObject(Math.max(result.getWidth(), result.getHeight()), this.canvas.getWidth());

        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (Node island : result.getIslands().values()) {
            this.islands.add(new IslandCircle(island, pane, gc, params));
        }
    }

    public void drawThings() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawCanvasBorder(gc);
        drawGrid(gc);
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
            final double val = coordinate(i);
            gc.strokeLine(params.getPadding(), val, x1, val);
            gc.strokeLine(val, params.getPadding(), val, y1);
        }
    }

    private double coordinate(int i) {
        return (i * (params.getFieldSize())) + params.getPadding();
    }

    private ParseResult getParseResult() {
        Validator validator = new DefaultValidator();

        File file = new File("src\\main\\resources\\data\\bsp_5x5.bgs");

        ParseResult result = new ParseResult(Collections.emptyMap(), Collections.emptyList(), 0, 0);
        try {
            Parser parser = new TokenConsumingParser(new TokenizerImpl(readFile(file.getAbsolutePath())));
            result = parser.parse();
            validator.validate(result);

        } catch (IOException | ParseException | ValidateException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void drawCanvasBorder(GraphicsContext gc) {
        gc.setStroke(Color.DARKGRAY);
        gc.setLineWidth(3);
        gc.strokePolygon(
                new double[]{0, canvas.getWidth(), canvas.getWidth(), 0},
                new double[]{0, 0, canvas.getHeight(), canvas.getHeight()},
                4
        );
    }

    private static String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), Charset.defaultCharset());
    }
}
