package de.karstenkoehler.bridges.ui.shapes;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.Direction;
import de.karstenkoehler.bridges.model.Island;
import de.karstenkoehler.bridges.ui.CanvasDimensions;
import de.karstenkoehler.bridges.ui.NumberDisplay;
import de.karstenkoehler.bridges.ui.events.EventTypes;
import javafx.event.Event;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.function.Consumer;
import java.util.function.Function;

import static de.karstenkoehler.bridges.ui.events.EventTypes.EVAL_STATE;

/**
 * The graphical representation of an island. Every instance of this class is tied to
 * an instance of {@link Island}. The island itself is drawn on the canvas. The click
 * area to build bridges is a separate shape on a invisible plane in front of the canvas.
 */
public class IslandShape {
    private static final Color FILL_COLOR = Color.BLACK;

    private final Island island;
    private final Pane controlPane;
    private final GraphicsContext gc;
    private final CanvasDimensions dimensions;
    private final BridgesPuzzle puzzle;

    /**
     * Creates a new island shape and binds it to an island.
     *
     * @param island      the island to bind to
     * @param controlPane the pane to hold the click area shape
     * @param gc          the graphics context of the canvas to draw on
     * @param dimensions  the calculated dimensions for the puzzle
     * @param puzzle      the puzzle of the connection
     */
    public IslandShape(Island island, Pane controlPane, GraphicsContext gc, CanvasDimensions dimensions, BridgesPuzzle puzzle) {
        this.island = island;
        this.controlPane = controlPane;
        this.gc = gc;
        this.dimensions = dimensions;
        this.puzzle = puzzle;

        initControls(dimensions.coordinate(island.getX()), dimensions.coordinate(island.getY()));
    }

    /**
     * Redraws the island on the canvas.
     *
     * @param numberDisplay        the configuration for displaying numbers
     * @param drawClickAreaOutline the configuration for outlining the click area
     */
    public void draw(NumberDisplay numberDisplay, boolean drawClickAreaOutline) {
        double x = dimensions.coordinate(island.getX());
        double y = dimensions.coordinate(island.getY());

        if (puzzle.getRemainingBridgeCount(island) > 0) {
            gc.setFill(Color.GRAY);
        } else if (puzzle.getRemainingBridgeCount(island) == 0) {
            gc.setFill(Color.GREEN);
        } else {
            gc.setFill(Color.RED);
        }

        gc.fillOval(x - dimensions.getIslandOffset(), y - dimensions.getIslandOffset(), dimensions.getIslandDiameter(), dimensions.getIslandDiameter());

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(Font.font(dimensions.getFontSize()));
        gc.setFill(Color.BLACK);
        gc.fillText(getDisplayNumber(numberDisplay), x, y);

        if (drawClickAreaOutline) {
            drawClickAreaOutline();
        }

    }

    /**
     * Returns the number to display depending on the given configuration.
     *
     * @param display the configuration for displaying numbers
     * @return the number to display depending on the configuration
     */
    private String getDisplayNumber(NumberDisplay display) {
        if (display == NumberDisplay.SHOW_REQUIRED) {
            return String.valueOf(island.getRequiredBridges());
        } else if (display == NumberDisplay.SHOW_REMAINING) {
            return String.valueOf(puzzle.getRemainingBridgeCount(this.island));
        }

        throw new RuntimeException("IslandShape#getDisplayNumber: found invalid enum value");
    }

    /**
     * Initializes the triangle shapes that handle user input.
     *
     * @param x the x coordinate of the island
     * @param y the y coordinate of the island
     */
    private void initControls(double x, double y) {
        final double x0 = x - dimensions.getClickAreaSize();
        final double x1 = x + dimensions.getClickAreaSize();
        final double y0 = y - dimensions.getClickAreaSize();
        final double y1 = y + dimensions.getClickAreaSize();

        createTriangle(Direction.NORTH, x, y, x1, y0, x0, y0);
        createTriangle(Direction.EAST, x, y, x1, y0, x1, y1);
        createTriangle(Direction.SOUTH, x, y, x1, y1, x0, y1);
        createTriangle(Direction.WEST, x, y, x0, y1, x0, y0);
    }

    /**
     * Create a single triangle shape to handle user input.
     *
     * @param direction the direction of the shape relative to the island
     * @param x0        the first point's x coordinate
     * @param y0        the first point's y coordinate
     * @param x1        the second point's x coordinate
     * @param y1        the second point's y coordinate
     * @param x2        the third point's x coordinate
     * @param y2        the third point's y coordinate
     */
    private void createTriangle(Direction direction, double x0, double y0, double x1, double y1, double x2, double y2) {
        Polygon poly = new Polygon(x0, y0, x1, y1, x2, y2);
        poly.setStroke(Color.TRANSPARENT);
        poly.setFill(Color.TRANSPARENT);
        poly.setOnMouseEntered(event -> poly.setFill(FILL_COLOR));
        poly.setOnMouseExited(event -> poly.setFill(Color.TRANSPARENT));
        poly.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                onLeftClick(poly, direction);
            } else {
                onRightClick(poly, direction);
            }
        });
        this.controlPane.getChildren().add(0, poly);
    }

    /**
     * Handles a left click on a click area. A bridge is added to the connection.
     *
     * @param poly      the triangle clicked
     * @param direction the direction of the triangle
     */
    private void onLeftClick(Polygon poly, Direction direction) {
        onClick(poly, direction, Connection::canAddBridge, Connection::addBridge);
    }

    /**
     * Handles a right click on a click area. A bridge is removed from the connection.
     *
     * @param poly      the triangle clicked
     * @param direction the direction of the triangle
     */
    private void onRightClick(Polygon poly, Direction direction) {
        onClick(poly, direction, Connection::canRemoveBridge, Connection::removeBridge);
    }

    /**
     * Generic handler for both left and right click. Checks if there exists a connection
     * for the clicked direction and executes the appropriate bridge operation.
     *
     * @param poly      the triangle clicked
     * @param direction the direction of the triangle
     * @param canExec   a method that checks if the bridge operation can be executed
     * @param exec      a method that executes the bridge operation
     */
    private void onClick(Polygon poly, Direction direction, Function<Connection, Boolean> canExec, Consumer<Connection> exec) {
        Connection connection = this.puzzle.getConnectedBridge(this.island, direction);
        if (connection == null) {
            return;
        }

        if (canExec.apply(connection)) {
            exec.accept(connection);
        }
        this.puzzle.emphasizeBridge(connection);

        poly.fireEvent(new Event(EventTypes.FILE_MODIFIED));
        poly.fireEvent(new Event(EVAL_STATE));
        poly.fireEvent(new Event(EventTypes.REDRAW));
    }

    /**
     * Draws the outlines of all click areas of the underlying island to the canvas.
     */
    private void drawClickAreaOutline() {
        final double x = dimensions.coordinate(island.getX());
        final double y = dimensions.coordinate(island.getY());

        final double x0 = x - dimensions.getClickAreaSize();
        final double x1 = x + dimensions.getClickAreaSize();
        final double y0 = y - dimensions.getClickAreaSize();
        final double y1 = y + dimensions.getClickAreaSize();

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
}
