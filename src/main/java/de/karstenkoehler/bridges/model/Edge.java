package de.karstenkoehler.bridges.model;

import java.util.Objects;

public class Edge {
    private final int id;
    private final int bridgeCount;
    private final int node1, node2; // TODO think up better names

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return id == edge.id &&
                bridgeCount == edge.bridgeCount &&
                node1 == edge.node1 &&
                node2 == edge.node2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bridgeCount, node1, node2);
    }
}
