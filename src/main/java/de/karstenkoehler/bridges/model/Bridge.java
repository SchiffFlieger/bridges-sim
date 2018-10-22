package de.karstenkoehler.bridges.model;

import de.karstenkoehler.bridges.InvalidBridgeCountException;

import java.util.Objects;

public class Bridge {
    private int bridgeCount;
    private final Island startIsland;
    private final Island endIsland;
    private boolean valid;
    private boolean emphasized;

    public Bridge(Island startIsland, Island endIsland) {
        this(startIsland, endIsland, 0);
    }

    public Bridge(Island startIsland, Island endIsland, int bridgeCount) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bridge bridge = (Bridge) o;
        return bridgeCount == bridge.bridgeCount &&
                Objects.equals(startIsland, bridge.startIsland) &&
                Objects.equals(endIsland, bridge.endIsland);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bridgeCount, startIsland, endIsland);
    }

    @Override
    public String toString() {
        return "Bridge{" +
                "bridgeCount=" + bridgeCount +
                ", startIsland=" + startIsland +
                ", endIsland=" + endIsland +
                ", valid=" + valid +
                ", emphasized=" + emphasized +
                '}';
    }

    public void addBridges(int count) throws InvalidBridgeCountException {
        if (count != 1 && count != -1) {
            throw new IllegalArgumentException(String.format("Bridge.addBridges got %d as argument", count));
        }

        if ((count == 1 && this.bridgeCount > 1) || (count == -1 && this.bridgeCount == 0)) {
            throw new InvalidBridgeCountException();
        }

        this.bridgeCount += count;
    }

    public boolean isEmphasized() {
        return emphasized;
    }

    public void setEmphasized(boolean emphasized) {
        this.emphasized = emphasized;
    }
}
