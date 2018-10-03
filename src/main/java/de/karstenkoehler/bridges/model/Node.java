package de.karstenkoehler.bridges.model;

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
}
