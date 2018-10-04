package de.karstenkoehler.bridges.model;

public class Edge {
    private final int id;
    private final int bridgeCount;
    private final int node1, node2; // TODO think up better names
    private boolean valid;

    public Edge(int id, int node1, int node2) {
        this(id, node1, node2, 0);
    }

    public Edge(int id, int node1, int node2, int bridgeCount) {
        this.id = id;
        this.bridgeCount = bridgeCount;
        this.node1 = node1;
        this.node2 = node2;
    }

    public int getId() {
        return id;
    }

    public int getNode1() {
        return node1;
    }

    public int getNode2() {
        return node2;
    }
}
