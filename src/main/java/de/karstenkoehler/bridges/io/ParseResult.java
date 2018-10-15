package de.karstenkoehler.bridges.io;

import de.karstenkoehler.bridges.model.Edge;
import de.karstenkoehler.bridges.model.Node;
import de.karstenkoehler.bridges.model.Orientation;

import java.util.*;

public class ParseResult {
    private final Map<Integer, Node> islands;
    private final List<Edge> bridges;
    private final Map<Integer, Map<Orientation, Edge>> bridgeConnections;
    private final int width;
    private final int height;

    public ParseResult (Map<Integer, Node> islands, List<Edge> bridges, int width, int height) {
        this.islands = islands;
        this.bridges = bridges;
        this.width = width;
        this.height = height;
        this.bridgeConnections = new HashMap<>();
    }

    public List<Node> getIslands() {
        return Collections.unmodifiableList(new ArrayList<>(this.islands.values()));
    }

    public List<Edge> getBridges () {
        return Collections.unmodifiableList(this.bridges);
    }

    public int getWidth () {
        return width;
    }

    public int getHeight () {
        return height;
    }

    public void fillMissingBridges () {
        for (Node island : this.islands.values()) {
            Map<Orientation, Edge> connections = new HashMap<>();

            connections.put(Orientation.NORTH, fillBridgeInDirection(island, 0, -1));
            connections.put(Orientation.EAST, fillBridgeInDirection(island, 1, 0));
            connections.put(Orientation.SOUTH, fillBridgeInDirection(island, 0, 1));
            connections.put(Orientation.WEST, fillBridgeInDirection(island, -1, 0));

            this.bridgeConnections.put(island.getId(), connections);
        }
    }

    public boolean isVertical(Edge bridge) {
        return islands.get(bridge.getNode1()).getX() == islands.get(bridge.getNode2()).getX();
    }

    public boolean isHorizontal(Edge bridge) {
        return islands.get(bridge.getNode1()).getY() == islands.get(bridge.getNode2()).getY();
    }

    public Edge getConnectedBridge(Node island, Orientation orientation) {
        return this.bridgeConnections.get(island.getId()).get(orientation);
    }

    public int getRemainingBridgeCount(Node island) {
        int bridgeCount = 0;
        for (Edge bridge : this.bridgeConnections.get(island.getId()).values()) {
            if (bridge != null) {
                bridgeCount += bridge.getBridgeCount();
            }
        }

        return island.getRequiredBridges() - bridgeCount;
    }

    public void markInvalidBridges() {
        this.bridges.forEach(edge -> edge.setValid(true));

        for (Edge bridge : this.bridges) {
            for (Edge other : this.bridges) {
                if (bridge == other || bridge.getBridgeCount() == 0 || other.getBridgeCount() == 0) {
                    continue;
                }

                if (isHorizontal(bridge) && isVertical(other)) {
                    areBridgesCrossing(bridge, other);
                }

                if (isVertical(bridge) && isHorizontal(other)) {
                    areBridgesCrossing(other, bridge);
                }

            }
        }
    }

    private void areBridgesCrossing(Edge bridge, Edge other) {
        int y1 = this.islands.get(bridge.getNode1()).getY();
        int x2 = this.islands.get(other.getNode2()).getX();

        int x1a = this.islands.get(bridge.getNode1()).getX();
        int x1e = this.islands.get(bridge.getNode2()).getX();
        int y2a = this.islands.get(other.getNode1()).getY();
        int y2e = this.islands.get(other.getNode2()).getY();
        if (x1a < x2 && x2 < x1e && y2a < y1 && y1 < y2e) {
            bridge.setValid(false);
            other.setValid(false);
        }
    }

    private Edge fillBridgeInDirection (Node island, int dx, int dy) {
        Node north = findNextInDirection(island.getX(), island.getY(), dx, dy);
        if (north == null) {
            return null;
        }
        Edge existingBridge = getExistingBridge(island, north);
        if (existingBridge != null) {
            return existingBridge;
        }

        int min = Math.min(island.getId(), north.getId());
        int max = Math.max(island.getId(), north.getId());
        Edge bridge = new Edge(this.bridges.size(), min, max);
        this.bridges.add(bridge);
        return bridge;
    }

    private Edge getExistingBridge (Node island, Node other) {
        int a = Math.min(island.getId(), other.getId());
        int b = Math.max(island.getId(), other.getId());

        for (Edge bridge : this.bridges) {
            if (bridge.getNode1() == a && bridge.getNode2() == b) {
                return bridge;
            }
        }
        return null;
    }

    private Node findNextInDirection (int x, int y, int dx, int dy) {
        x += dx;
        y += dy;
        while (x >= 0 && y >= 0 && x < this.width && y < this.height) {
            for (Node island : this.islands.values()) {
                if (island.getX() == x && island.getY() == y) {
                    return island;
                }
            }
            x += dx;
            y += dy;
        }
        return null;
    }
}
