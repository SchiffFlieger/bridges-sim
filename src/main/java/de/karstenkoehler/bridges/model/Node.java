package de.karstenkoehler.bridges.model;

import java.util.Objects;

public class Node {
    private final int id;
    private final int x, y;
    private final int requiredBridges;
    private Edge north;
    private Edge east;
    private Edge south;
    private Edge west;

    public Node (int id, int x, int y, int requiredBridges) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.requiredBridges = requiredBridges;
    }

    public int getId () {
        return id;
    }

    public int getRequiredBridges () {
        return requiredBridges;
    }

    public int getX () {
        return x;
    }

    public int getY () {
        return y;
    }

    public void setConnections (Edge north, Edge east, Edge south, Edge west) {
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
    }

    public Edge north () {
        return this.north;
    }

    public Edge east () {
        return this.east;
    }

    public Edge south () {
        return this.south;
    }

    public Edge west () {
        return this.west;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id == node.id &&
                x == node.x &&
                y == node.y &&
                requiredBridges == node.requiredBridges;
    }

    @Override
    public int hashCode () {
        return Objects.hash(id, x, y, requiredBridges);
    }
}
