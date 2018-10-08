package de.karstenkoehler.bridges.ui;

public class ParameterObject {

    private final int gridLines;
    private final double fieldSize;
    private final double fontSize;
    private final double islandDiameter;
    private final double islandOffset;
    private final double padding;
    private final double clickAreaSize;

    public ParameterObject(final int gridLines, final double canvasSize) {
        this.gridLines = gridLines;
        this.fontSize = 500 / gridLines;
        this.islandDiameter = 600 / gridLines;
        this.islandOffset = this.islandDiameter / 2;
        this.padding = (this.islandDiameter / 2) + 5;
        this.clickAreaSize = (this.islandDiameter / 2);
        this.fieldSize = (canvasSize - 2 * this.padding) / (this.gridLines - 1);
    }

    public int getGridLines() {
        return gridLines;
    }

    public double getFieldSize() {
        return fieldSize;
    }

    public double getFontSize() {
        return fontSize;
    }

    public double getIslandDiameter() {
        return islandDiameter;
    }

    public double getIslandOffset() {
        return islandOffset;
    }

    public double getPadding() {
        return padding;
    }

    public double getClickAreaSize() {
        return clickAreaSize;
    }
}
