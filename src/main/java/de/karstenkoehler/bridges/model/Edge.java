package de.karstenkoehler.bridges.model;

public class Edge {
    private final int bridgeCount;
    private final int node1, node2;
    private boolean valid;

    public Edge(int node1, int node2) {
        this(node1, node2, 0);
    }

    public Edge(int node1, int node2, int bridgeCount) {
        this.bridgeCount = bridgeCount;
        this.node1 = node1;
        this.node2 = node2;
    }
}
