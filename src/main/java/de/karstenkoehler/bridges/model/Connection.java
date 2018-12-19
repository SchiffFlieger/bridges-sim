package de.karstenkoehler.bridges.model;

import java.util.Objects;

public class Connection {
    private int bridgeCount;
    private final Island startIsland;
    private final Island endIsland;
    private boolean valid;
    private boolean emphasized;

    public Connection(Island startIsland, Island endIsland) {
        this(startIsland, endIsland, 0);
    }

    public Connection(Island startIsland, Island endIsland, int bridgeCount) {
        this.bridgeCount = bridgeCount;
        this.startIsland = startIsland;
        this.endIsland = endIsland;
        this.valid = true;
        this.emphasized = false;
    }

    public Island getStartIsland() {
        return startIsland;
    }

    public Island getEndIsland() {
        return endIsland;
    }

    public void setBridgeCount(int count) {
        this.bridgeCount = count;
    }

    public int getBridgeCount() {
        return bridgeCount;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getLength() {
        return Math.abs(startIsland.getX() - endIsland.getX()) + Math.abs(startIsland.getY() - endIsland.getY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection connection = (Connection) o;
        return bridgeCount == connection.bridgeCount &&
                Objects.equals(startIsland, connection.startIsland) &&
                Objects.equals(endIsland, connection.endIsland);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bridgeCount, startIsland, endIsland);
    }

    @Override
    public String toString() {
        return "Connection{" +
                "bridgeCount=" + bridgeCount +
                ", startIsland=" + startIsland +
                ", endIsland=" + endIsland +
                ", valid=" + valid +
                ", emphasized=" + emphasized +
                '}';
    }

    public boolean canAddBridge() {
        return this.bridgeCount < 2;
    }

    public boolean canRemoveBridge() {
        return this.bridgeCount > 0;
    }

    public void addBridge() {
        this.bridgeCount++;
    }

    public void removeBridge() {
        this.bridgeCount--;
    }

    public boolean isEmphasized() {
        return emphasized;
    }

    public void setEmphasized(boolean emphasized) {
        this.emphasized = emphasized;
    }

    public boolean isVertical() {
        return this.startIsland.getX() == this.endIsland.getX();
    }

    public boolean isHorizontal() {
        return this.startIsland.getY() == this.endIsland.getY();
    }
}
