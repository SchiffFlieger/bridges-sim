package de.karstenkoehler.bridges.ui;

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

    public double getDoubleBridgeOffset() {
        return doubleBridgeOffset;
    }

    public double getBridgeLineSize() {
        return bridgeLineSize;
    }

    public double coordinate(int i) {
        return (i * (this.getFieldSize())) + this.getPadding();
    }
}
