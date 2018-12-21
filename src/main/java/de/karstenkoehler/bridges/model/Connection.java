package de.karstenkoehler.bridges.model;

import java.util.Objects;

/**
 * A connection links two adjacent bridges together. It is used as an edge in the graph implementation of
 * {@link BridgesPuzzle}. The connection also holds the information about existing bridges between islands.
 */
public class Connection {
    private int bridgeCount;
    private final Island startIsland;
    private final Island endIsland;
    private boolean valid;
    private boolean emphasized;


    /**
     * Creates a new connection between two islands without any bridges.
     *
     * @param startIsland the start island of the connection
     * @param endIsland   the end island of the connection
     */
    public Connection(Island startIsland, Island endIsland) {
        this(startIsland, endIsland, 0);
    }

    /**
     * Creates a new connection between two islands. The connection holds the given number
     * of bridges.
     *
     * @param startIsland the start island of the connection
     * @param endIsland   the end island of the connection
     * @param bridgeCount the number of bridges between the two islands
     */
    public Connection(Island startIsland, Island endIsland, int bridgeCount) {
        this.bridgeCount = bridgeCount;
        this.startIsland = startIsland;
        this.endIsland = endIsland;
        this.valid = true;
        this.emphasized = false;
    }

    /**
     * Returns the start island of the connection.
     *
     * @return the start island of the connection
     */
    public Island getStartIsland() {
        return startIsland;
    }

    /**
     * Returns the end island of the connection.
     *
     * @return the end island of the connection
     */
    public Island getEndIsland() {
        return endIsland;
    }

    /**
     * Sets the number of bridges in this connection.
     *
     * @param count the number of bridges
     */
    public void setBridgeCount(int count) {
        this.bridgeCount = count;
    }

    /**
     * Returns the number of bridges in this connection.
     *
     * @return the number of bridges in this connection
     */
    public int getBridgeCount() {
        return bridgeCount;
    }

    /**
     * Returns true if the bridge is valid.
     *
     * @return true if the bridge is valid, false otherwise
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Marks this bridge as valid or invalid.
     *
     * @param valid true if the bridge is valid, false otherwise
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * Returns the distance from the start island to the end island. Caution: this method does not calculate
     * the Euclidean distance, so it will not work for diagonal connections (that should not occurr anyways).
     *
     * @return the distance from the start island to the end island
     */
    public int getLength() {
        return Math.abs(startIsland.getX() - endIsland.getX()) + Math.abs(startIsland.getY() - endIsland.getY());
    }

    /**
     * Returns true if this connection can hold at least one more bridge.
     *
     * @return true if this connection can hold at least one more bridge, false otherwise
     */
    public boolean canAddBridge() {
        return this.bridgeCount < 2;
    }

    /**
     * Returns true if this connection holds at least one bridge.
     *
     * @return true if this connection holds at least one bridge, false otherwise
     */
    public boolean canRemoveBridge() {
        return this.bridgeCount > 0;
    }

    /**
     * Adds a single bridge to this connection.
     */
    public void addBridge() {
        this.bridgeCount++;
    }

    /**
     * Removes a single bridge from this connection.
     */
    public void removeBridge() {
        this.bridgeCount--;
    }

    /**
     * Returns true if the bridges of this connection are emphasized.
     *
     * @return true if the bridges of this connection are emphasized
     */
    public boolean isEmphasized() {
        return emphasized;
    }

    /**
     * Marks the bridges of this connection as emphasized.
     *
     * @param emphasized true if the bridge should be emphasized, false otherwise
     */
    public void setEmphasized(boolean emphasized) {
        this.emphasized = emphasized;
    }

    /**
     * Returns true if this connection is vertical.
     *
     * @return true  if this connection is vertical, false otherwise
     */
    public boolean isVertical() {
        return this.startIsland.getX() == this.endIsland.getX();
    }

    /**
     * Returns true if this connection is horizontal.
     *
     * @return true  if this connection is horizontal, false otherwise
     */
    public boolean isHorizontal() {
        return this.startIsland.getY() == this.endIsland.getY();
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
}
