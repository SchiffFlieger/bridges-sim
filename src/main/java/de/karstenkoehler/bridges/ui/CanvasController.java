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
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

public class CanvasController {
    private static final int GRID_LINES = 25;

    private static final int ISLAND_DIAMETER = 600 / GRID_LINES;
    private static final int ISLAND_OFFSET = ISLAND_DIAMETER / 2;

    private static final int CLICK_AREA_SIZE = ISLAND_DIAMETER / 2;

    private static final int PADDING = (ISLAND_DIAMETER / 2) + 5;
    private static final int FONT_SIZE = 500 / GRID_LINES;

    private final Canvas canvas;
    private final double fieldSize;

    private boolean gridVisible;
    private boolean clickAreaVisible;

    public CanvasController(Canvas canvas) {
        this.canvas = canvas;
        this.fieldSize = (canvas.getWidth() - 2 * PADDING) / (GRID_LINES - 1);
    }

    public void drawThings() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());


        drawCanvasBorder(gc);

        drawGrid(gc);

        ParseResult result = getParseResult();
        for (Node island : result.getIslands().values()) {
            drawIsland(gc, island);
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
        final double x1 = canvas.getWidth() - PADDING;
        final double y1 = canvas.getHeight() - PADDING;

        gc.setStroke(Color.RED);
        gc.setLineWidth(lineWidth);

        for (int i = 0; i < GRID_LINES; i++) {
            final double val = coordinate(i);
            gc.strokeLine(PADDING, val, x1, val);
            gc.strokeLine(val, PADDING, val, y1);
        }
    }

    private double coordinate(int i) {
        return (i * (fieldSize)) + PADDING;
    }

    private void drawIsland(GraphicsContext gc, Node island) {
        double x = coordinate(island.getX());
        double y = coordinate(island.getY());
        gc.setFill(Color.GREEN);
        gc.fillOval(x - ISLAND_OFFSET, y - ISLAND_OFFSET, ISLAND_DIAMETER, ISLAND_DIAMETER);

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(Font.font(FONT_SIZE));
        gc.setFill(Color.BLACK);
        gc.fillText(String.valueOf(island.getRequiredBridges()), x, y);

        drawClickArea(gc, x, y);
    }

    private void drawClickArea(GraphicsContext gc, double x, double y) {
        if (!this.clickAreaVisible) {
            return;
        }

        final double x0 = x - CLICK_AREA_SIZE;
        final double x1 = x + CLICK_AREA_SIZE;
        final double y0 = y - CLICK_AREA_SIZE;
        final double y1 = y + CLICK_AREA_SIZE;

        gc.setStroke(Color.RED);
        gc.setLineWidth(1);

        // diagonals
        gc.strokeLine(x0, y0, x1, y1);
        gc.strokeLine(x0, y1, x1, y0);

        // square
        gc.strokeLine(x0, y0, x1, y0);
        gc.strokeLine(x0, y0, x0, y1);
        gc.strokeLine(x0, y1, x1, y1);
        gc.strokeLine(x1, y1, x1, y0);
    }

    private ParseResult getParseResult() {
        Validator validator = new DefaultValidator();

        File file = new File("src\\main\\resources\\data\\bsp_25x25.bgs");

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
