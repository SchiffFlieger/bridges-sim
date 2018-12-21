package de.karstenkoehler.bridges.model;

import java.util.Objects;

/**
 * This class represents an island in the bridges puzzle. It is used as a node in the graph implementation
 * of {@link BridgesPuzzle}.
 */
public class Island {
    private int id;
    private final int x, y;
    private int requiredBridges;

    /**
     * Creates a new island.
     *
     * @param id              the id of the island
     * @param x               the x coordinate of the island
     * @param y               the y coordinate of the island
     * @param requiredBridges the amount of bridges needed to satisfy this island
     */
    public Island(int id, int x, int y, int requiredBridges) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.requiredBridges = requiredBridges;
    }

    /**
     * Sets a new id for this island.
     *
     * @param id the new id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the id of this island.
     *
     * @return the id of this island
     */
    public int getId() {
        return id;
    }

    /**
     * Sets a new number of required bridges for this island.
     *
     * @param count the new number of required bridges
     */
    public void setRequiredBridges(int count) {
        this.requiredBridges = count;
    }

    /**
     * Returns the number of required bridges of this island.
     *
     * @return the number of required bridges
     */
    public int getRequiredBridges() {
        return requiredBridges;
    }

    /**
     * Returns the x coordinate of this island.
     *
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y coordinate of this island.
     *
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Island island = (Island) o;
        return id == island.id &&
                x == island.x &&
                y == island.y &&
                requiredBridges == island.requiredBridges;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y, requiredBridges);
    }

    @Override
    public String toString() {
        return "Island{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", requiredBridges=" + requiredBridges +
                '}';
    }
}
