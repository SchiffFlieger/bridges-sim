package de.karstenkoehler.bridges.model;

import java.util.Objects;

public class Node {
    private final int id;
    private final int x, y;
    private final int requiredBridges;

    public Node(int id, int x, int y, int requiredBridges) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.requiredBridges = requiredBridges;
    }

    public int getId() {
        return id;
    }

    public int getRequiredBridges() {
        return requiredBridges;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id == node.id &&
                x == node.x &&
                y == node.y &&
                requiredBridges == node.requiredBridges;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y, requiredBridges);
    }
}
