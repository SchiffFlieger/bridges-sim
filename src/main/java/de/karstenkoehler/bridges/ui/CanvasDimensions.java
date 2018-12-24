package de.karstenkoehler.bridges.ui;

/**
 * This class encapsulates some calculations for the size of the game objects on
 * the canvas. Each puzzle is dimensioned accordingly, i.e. it uses the entire
 * canvas area, regardless of its width and height.
 */
public class CanvasDimensions {

    private final int gridLines;
    private final double fieldSize;
    private final double fontSize;
    private final double islandDiameter;
    private final double islandOffset;
    private final double padding;
    private final double clickAreaSize;
    private final double doubleBridgeOffset;
    private final double bridgeLineSize;

    /**
     * Creates a new dimensions objects.
     *
     * @param gridLines  the max of height and width of the puzzle
     * @param canvasSize the max of height and width of the canvas
     */
    public CanvasDimensions(final int gridLines, final double canvasSize) {
        this.gridLines = gridLines;
        this.fontSize = 500.0 / gridLines;
        this.islandDiameter = 600.0 / gridLines;
        this.doubleBridgeOffset = 90.0 / gridLines;
        this.bridgeLineSize = 7.8 - (gridLines / 5.0);
        this.islandOffset = this.islandDiameter / 2;
        this.padding = (this.islandDiameter / 2) + 5;
        this.clickAreaSize = (this.islandDiameter / 2);
        this.fieldSize = (canvasSize - 2 * this.padding) / (this.gridLines - 1);
    }

    /**
     * Returns the number of grid lines in the puzzle.
     *
     * @return the number of grid lines in the puzzle
     */
    public int getGridLines() {
        return gridLines;
    }

    /**
     * Returns the size of a single grid field.
     *
     * @return the size of a single grid field
     */
    public double getFieldSize() {
        return fieldSize;
    }

    /**
     * Returns the size of the font for the island numbers.
     *
     * @return the size of the font for the island numbers
     */
    public double getFontSize() {
        return fontSize;
    }

    /**
     * Returns the diameter of an island.
     *
     * @return the diameter of an island
     */
    public double getIslandDiameter() {
        return islandDiameter;
    }

    /**
     * Returns the offset that is needed so the island is placed directly on the grid.
     *
     * @return the offset that is needed so the island is placed directly on the grid
     */
    public double getIslandOffset() {
        return islandOffset;
    }

    /**
     * Returns the padding of the puzzle inside the canvas.
     *
     * @return the padding of the puzzle inside the canvas
     */
    public double getPadding() {
        return padding;
    }

    /**
     * Returns the size of the click area.
     *
     * @return the size of the click area
     */
    public double getClickAreaSize() {
        return clickAreaSize;
    }

    /**
     * Returns the offset that is needed to draw a double bridge.
     *
     * @return the offset that is needed to draw a double bridge
     */
    public double getDoubleBridgeOffset() {
        return doubleBridgeOffset;
    }

    /**
     * Returns the thickness of a bridge.
     *
     * @return the thickness of a bridge
     */
    public double getBridgeLineSize() {
        return bridgeLineSize;
    }

    /**
     * Transforms the puzzles x and y coordinates to the according x and y coordinate on the canvas.
     *
     * @param i the coordinate of the puzzle to transform
     * @return the resulting coordinate on the canvas
     */
    public double coordinate(int i) {
        return (i * (this.getFieldSize())) + this.getPadding();
    }
}
