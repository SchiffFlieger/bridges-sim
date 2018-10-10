package de.karstenkoehler.bridges.io;

import de.karstenkoehler.bridges.model.Edge;
import de.karstenkoehler.bridges.model.Node;

import java.util.List;
import java.util.Map;

public class ParseResult {
    private final Map<Integer, Node> islands;
    private final List<Edge> bridges;
    private final int width;
    private final int height;

    public ParseResult (Map<Integer, Node> islands, List<Edge> bridges, int width, int height) {
        this.islands = islands;
        this.bridges = bridges;
        this.width = width;
        this.height = height;
    }

    public Map<Integer, Node> getIslands () {
        return islands;
    }

    public List<Edge> getBridges () {
        return bridges;
    }

    public int getWidth () {
        return width;
    }

    public int getHeight () {
        return height;
    }

    public void fillMissingBridges () {
        for (Node island : this.islands.values()) {
            Edge north = fillBridgeInDirection(island, 0, -1);
            Edge east = fillBridgeInDirection(island, 1, 0);
            Edge south = fillBridgeInDirection(island, 0, 1);
            Edge west = fillBridgeInDirection(island, -1, 0);

            System.out.printf("island %2d: %s %s %s %s\n", island.getId(), north, east, south, west);

            island.setConnections(north, east, south, west);
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
