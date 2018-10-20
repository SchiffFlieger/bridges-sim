package de.karstenkoehler.bridges.model;

import java.util.*;

public class BridgesPuzzle {
    private final Map<Integer, Island> islands;
    private final List<Bridge> bridges;
    private final Map<Integer, Map<Orientation, Bridge>> bridgeConnections;
    private final int width;
    private final int height;

    public BridgesPuzzle (Map<Integer, Island> islands, List<Bridge> bridges, int width, int height) {
        this.islands = islands;
        this.bridges = bridges;
        this.width = width;
        this.height = height;
        this.bridgeConnections = new HashMap<>();
    }

    public List<Island> getIslands() {
        return Collections.unmodifiableList(new ArrayList<>(this.islands.values()));
    }

    public List<Bridge> getBridges () {
        return Collections.unmodifiableList(this.bridges);
    }

    public int getWidth () {
        return width;
    }

    public int getHeight () {
        return height;
    }

    public void fillMissingBridges () {
        for (Island island : this.islands.values()) {
            Map<Orientation, Bridge> connections = new EnumMap<>(Orientation.class);

            connections.put(Orientation.NORTH, fillBridgeInDirection(island, 0, -1));
            connections.put(Orientation.EAST, fillBridgeInDirection(island, 1, 0));
            connections.put(Orientation.SOUTH, fillBridgeInDirection(island, 0, 1));
            connections.put(Orientation.WEST, fillBridgeInDirection(island, -1, 0));

            this.bridgeConnections.put(island.getId(), connections);
        }
    }

    public void emphasizeBridge (Bridge bridge) {
        this.bridges.forEach(b -> b.setEmphasized(false));
        if (bridge != null) {
            bridge.setEmphasized(true);
        }
    }

    public boolean isVertical(Bridge bridge) {
        return bridge.getStartIsland().getX() == bridge.getEndIsland().getX();
    }

    public boolean isHorizontal(Bridge bridge) {
        return bridge.getStartIsland().getY() == bridge.getEndIsland().getY();
    }

    public Bridge getConnectedBridge(Island island, Orientation orientation) {
        return this.bridgeConnections.get(island.getId()).get(orientation);
    }

    public int getRemainingBridgeCount(Island island) {
        int bridgeCount = 0;
        for (Bridge bridge : this.bridgeConnections.get(island.getId()).values()) {
            if (bridge != null) {
                bridgeCount += bridge.getBridgeCount();
            }
        }

        return island.getRequiredBridges() - bridgeCount;
    }

    public void markInvalidBridges() {
        this.bridges.forEach(b -> b.setValid(true));

        for (Bridge bridge : this.bridges) {
            for (Bridge other : this.bridges) {
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

    public void restart() {
        this.bridges.forEach(bridge -> bridge.setBridgeCount(0));
    }

    private void areBridgesCrossing(Bridge bridge, Bridge other) {
        int y1 = bridge.getStartIsland().getY();
        int x2 = other.getEndIsland().getX();

        int x1a = bridge.getStartIsland().getX();
        int x1e = bridge.getEndIsland().getX();
        int y2a = other.getStartIsland().getY();
        int y2e = other.getEndIsland().getY();
        if (x1a < x2 && x2 < x1e && y2a < y1 && y1 < y2e) {
            bridge.setValid(false);
            other.setValid(false);
        }
    }

    private Bridge fillBridgeInDirection (Island island, int dx, int dy) {
        Island north = findNextInDirection(island.getX(), island.getY(), dx, dy);
        if (north == null) {
            return null;
        }
        Bridge existingBridge = getExistingBridge(island, north);
        if (existingBridge != null) {
            return existingBridge;
        }

        int min = Math.min(island.getId(), north.getId());
        int max = Math.max(island.getId(), north.getId());
        Bridge bridge = new Bridge(this.bridges.size(), islands.get(min), islands.get(max));
        this.bridges.add(bridge);
        return bridge;
    }

    private Bridge getExistingBridge (Island island, Island other) {
        int a = Math.min(island.getId(), other.getId());
        int b = Math.max(island.getId(), other.getId());

        for (Bridge bridge : this.bridges) {
            if (bridge.getStartIsland().getId() == a && bridge.getEndIsland().getId() == b) {
                return bridge;
            }
        }
        return null;
    }

    private Island findNextInDirection (int x, int y, int dx, int dy) {
        x += dx;
        y += dy;
        while (x >= 0 && y >= 0 && x < this.width && y < this.height) {
            for (Island island : this.islands.values()) {
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
