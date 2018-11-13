package de.karstenkoehler.bridges.model;

import java.util.*;

public class BridgesPuzzle {
    private final IslandStore islands;
    private final List<Bridge> bridges;
    private final Map<Integer, Map<Orientation, Bridge>> bridgeConnections;
    private final int width;
    private final int height;

    public BridgesPuzzle(List<Island> islands, List<Bridge> bridges, int width, int height) {
        this.islands = new IslandStore(islands);
        this.bridges = bridges;
        this.width = width;
        this.height = height;
        this.bridgeConnections = new HashMap<>();
    }

    public List<Island> getIslands() {
        return Collections.unmodifiableList(this.islands.getAsList());
    }

    public List<Bridge> getBridges() {
        return Collections.unmodifiableList(this.bridges);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void fillMissingBridges() {
        for (Island island : this.islands.getAsList()) {
            Map<Orientation, Bridge> connections = new EnumMap<>(Orientation.class);

            connections.put(Orientation.NORTH, fillBridgeInDirection(island, 0, -1));
            connections.put(Orientation.EAST, fillBridgeInDirection(island, 1, 0));
            connections.put(Orientation.SOUTH, fillBridgeInDirection(island, 0, 1));
            connections.put(Orientation.WEST, fillBridgeInDirection(island, -1, 0));

            this.bridgeConnections.put(island.getId(), connections);
        }

        this.bridges.sort((o1, o2) -> {
            int diff = o1.getStartIsland().getId() - o2.getStartIsland().getId();
            if (diff != 0) {
                return diff;
            }

            return o1.getEndIsland().getId() - o2.getEndIsland().getId();
        });
    }

    public void emphasizeBridge(Bridge bridge) {
        this.bridges.forEach(b -> b.setEmphasized(false));
        if (bridge != null) {
            bridge.setEmphasized(true);
        }
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

                if (bridge.isHorizontal() && other.isVertical()) {
                    if (areBridgesCrossing(bridge, other)) {
                        bridge.setValid(false);
                        other.setValid(false);
                    }
                }

                if (bridge.isVertical() && other.isHorizontal()) {
                    if (areBridgesCrossing(other, bridge)) {
                        bridge.setValid(false);
                        other.setValid(false);
                    }
                }
            }
        }
    }

    public void restart() {
        this.bridges.forEach(bridge -> bridge.setBridgeCount(0));
    }

    public PuzzleState getState() {
        if (anyBridgesCrossing() || anyIslandsTooManyBridges()) {
            return PuzzleState.ERROR;
        }

        if (isSolved()) {
            return PuzzleState.SOLVED;
        }

        if (!hasPossibleMoves()) {
            return PuzzleState.NO_LONGER_SOLVABLE;
        }

        return PuzzleState.NOT_SOLVED;
    }

    private boolean hasPossibleMoves() {
        return this.bridges.stream().anyMatch(this::hasPossibleMoves);
    }

    private boolean hasPossibleMoves(Bridge bridge) {
        if (getRemainingBridgeCount(bridge.getStartIsland()) <= 0 || getRemainingBridgeCount(bridge.getEndIsland()) <= 0) {
            return false;
        }

        if (bridge.getBridgeCount() == 1) {
            return true;
        }
        if (bridge.getBridgeCount() == 2) {
            return false;
        }

        return !causesCrossing(bridge);
    }

    public boolean causesCrossing(Bridge bridge) {
        for (Bridge other : this.bridges) {
            if (bridge == other || other.getBridgeCount() == 0) {
                continue;
            }

            if (bridge.isHorizontal() && other.isVertical()) {
                if (areBridgesCrossing(bridge, other)) {
                    return true;
                }
            }

            if (bridge.isVertical() && other.isHorizontal()) {
                if (areBridgesCrossing(other, bridge)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void reevalIslandBridgeCount() {
        for (Island island : this.islands.getAsList()) {
            int count = 0;
            for (Bridge bridge : this.bridges) {
                if (bridge.getStartIsland() == island || bridge.getEndIsland() == island) {
                    count += bridge.getBridgeCount();
                }
            }
            island.setRequiredBridges(count);
        }
    }

    private boolean isSolved() {
        return everyIslandSatisfied() && everyIslandConnected();
    }

    private boolean everyIslandConnected() {
        Set<Island> visited = new HashSet<>();
        visitAllNodes(this.islands.getAsList().get(0), visited);

        return visited.size() == this.islands.getAsList().size();
    }

    private void visitAllNodes(Island start, Set<Island> visited) {
        if (visited.contains(start)) {
            return;
        }
        visited.add(start);

        final List<Orientation> orientations = Arrays.asList(Orientation.NORTH, Orientation.EAST, Orientation.SOUTH, Orientation.WEST);

        for (Orientation orientation : orientations) {
            Island other = getConnectedIsland(start, orientation);
            if (other == null) {
                continue;
            }
            visitAllNodes(other, visited);
        }
    }

    private Island getConnectedIsland(Island island, Orientation orientation) {
        Bridge bridge = getConnectedBridge(island, orientation);
        if (bridge == null || bridge.getBridgeCount() == 0) {
            return null;
        }
        if (bridge.getStartIsland() == island) {
            return bridge.getEndIsland();
        }
        return bridge.getStartIsland();
    }

    private boolean everyIslandSatisfied() {
        return this.islands.getAsList().stream().allMatch(island -> getRemainingBridgeCount(island) == 0);
    }

    private boolean anyBridgesCrossing() {
        markInvalidBridges();
        return this.bridges.stream().anyMatch(bridge -> !bridge.isValid());
    }

    private boolean anyIslandsTooManyBridges() {
        return this.islands.getAsList().stream().anyMatch(island -> getRemainingBridgeCount(island) < 0);
    }

    private boolean areBridgesCrossing(Bridge bridge, Bridge other) {
        int y1 = bridge.getStartIsland().getY();
        int x2 = other.getEndIsland().getX();

        int x1a = bridge.getStartIsland().getX();
        int x1e = bridge.getEndIsland().getX();
        int y2a = other.getStartIsland().getY();
        int y2e = other.getEndIsland().getY();
        return x1a < x2 && x2 < x1e && y2a < y1 && y1 < y2e;
    }

    private Bridge fillBridgeInDirection(Island island, int dx, int dy) {
        Island neighbor = findNextInDirection(island.getX(), island.getY(), dx, dy);
        if (neighbor == null) {
            return null;
        }
        Bridge existingBridge = getExistingBridge(island, neighbor);
        if (existingBridge != null) {
            return existingBridge;
        }

        int min = Math.min(island.getId(), neighbor.getId());
        int max = Math.max(island.getId(), neighbor.getId());
        Bridge bridge = new Bridge(islands.getById(min), islands.getById(max));
        this.bridges.add(bridge);
        return bridge;
    }

    private Bridge getExistingBridge(Island island, Island other) {
        int a = Math.min(island.getId(), other.getId());
        int b = Math.max(island.getId(), other.getId());

        for (Bridge bridge : this.bridges) {
            if (bridge.getStartIsland().getId() == a && bridge.getEndIsland().getId() == b) {
                return bridge;
            }
        }
        return null;
    }

    public Island findNextInDirection(int x, int y, int dx, int dy) {
        x += dx;
        y += dy;
        while (x >= 0 && y >= 0 && x < this.width && y < this.height) {
            Island island = this.islands.getByCoordinates(x, y);
            if (island != null) {
                return island;
            }
            x += dx;
            y += dy;
        }
        return null;
    }
}
