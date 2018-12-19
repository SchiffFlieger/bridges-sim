package de.karstenkoehler.bridges.model;

import java.util.*;

public class BridgesPuzzle {
    private final IslandStore islands;
    private final List<Connection> connections;
    private final Map<Integer, Map<Orientation, Connection>> bridgeConnections;
    private final int width;
    private final int height;

    public BridgesPuzzle(List<Island> islands, List<Connection> connections, int width, int height) {
        this.islands = new IslandStore(islands);
        this.connections = connections;
        this.width = width;
        this.height = height;
        this.bridgeConnections = new HashMap<>();
    }

    public List<Island> getIslands() {
        return Collections.unmodifiableList(this.islands.getAsList());
    }

    public List<Connection> getConnections() {
        return Collections.unmodifiableList(this.connections);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void fillMissingBridges() {
        for (Island island : this.islands.getAsList()) {
            Map<Orientation, Connection> connections = new EnumMap<>(Orientation.class);

            connections.put(Orientation.NORTH, fillBridgeInDirection(island, 0, -1));
            connections.put(Orientation.EAST, fillBridgeInDirection(island, 1, 0));
            connections.put(Orientation.SOUTH, fillBridgeInDirection(island, 0, 1));
            connections.put(Orientation.WEST, fillBridgeInDirection(island, -1, 0));

            this.bridgeConnections.put(island.getId(), connections);
        }

        this.connections.sort((o1, o2) -> {
            int diff = o1.getStartIsland().getId() - o2.getStartIsland().getId();
            if (diff != 0) {
                return diff;
            }

            return o1.getEndIsland().getId() - o2.getEndIsland().getId();
        });
    }

    public void emphasizeBridge(Connection connection) {
        this.connections.forEach(b -> b.setEmphasized(false));
        if (connection != null) {
            connection.setEmphasized(true);
        }
    }

    public Connection getConnectedBridge(Island island, Orientation orientation) {
        return this.bridgeConnections.get(island.getId()).get(orientation);
    }

    public int getRemainingBridgeCount(Island island) {
        int bridgeCount = 0;
        for (Connection connection : this.bridgeConnections.get(island.getId()).values()) {
            if (connection != null) {
                bridgeCount += connection.getBridgeCount();
            }
        }

        return island.getRequiredBridges() - bridgeCount;
    }

    public void markInvalidBridges() {
        this.connections.forEach(b -> b.setValid(true));

        for (Connection connection : this.connections) {
            for (Connection other : this.connections) {
                if (connection == other || connection.getBridgeCount() == 0 || other.getBridgeCount() == 0) {
                    continue;
                }

                if (connection.isHorizontal() && other.isVertical()) {
                    if (areBridgesCrossing(connection, other)) {
                        connection.setValid(false);
                        other.setValid(false);
                    }
                }

                if (connection.isVertical() && other.isHorizontal()) {
                    if (areBridgesCrossing(other, connection)) {
                        connection.setValid(false);
                        other.setValid(false);
                    }
                }
            }
        }
    }

    public void restart() {
        this.connections.forEach(bridge -> bridge.setBridgeCount(0));
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
        return this.connections.stream().anyMatch(this::hasPossibleMoves);
    }

    private boolean hasPossibleMoves(Connection connection) {
        if (getRemainingBridgeCount(connection.getStartIsland()) <= 0 || getRemainingBridgeCount(connection.getEndIsland()) <= 0) {
            return false;
        }

        if (connection.getBridgeCount() == 1) {
            return true;
        }
        if (connection.getBridgeCount() == 2) {
            return false;
        }

        return !causesCrossing(connection);
    }

    public boolean causesCrossing(Connection bridge) {
        for (Connection other : this.connections) {
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
            for (Connection bridge : this.connections) {
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
        Connection connection = getConnectedBridge(island, orientation);
        if (connection == null || connection.getBridgeCount() == 0) {
            return null;
        }
        if (connection.getStartIsland() == island) {
            return connection.getEndIsland();
        }
        return connection.getStartIsland();
    }

    private boolean everyIslandSatisfied() {
        return this.islands.getAsList().stream().allMatch(island -> getRemainingBridgeCount(island) == 0);
    }

    private boolean anyBridgesCrossing() {
        markInvalidBridges();
        return this.connections.stream().anyMatch(bridge -> !bridge.isValid());
    }

    private boolean anyIslandsTooManyBridges() {
        return this.islands.getAsList().stream().anyMatch(island -> getRemainingBridgeCount(island) < 0);
    }

    private boolean areBridgesCrossing(Connection bridge, Connection other) {
        int y1 = bridge.getStartIsland().getY();
        int x2 = other.getEndIsland().getX();

        int x1a = bridge.getStartIsland().getX();
        int x1e = bridge.getEndIsland().getX();
        int y2a = other.getStartIsland().getY();
        int y2e = other.getEndIsland().getY();
        return x1a < x2 && x2 < x1e && y2a < y1 && y1 < y2e;
    }

    private Connection fillBridgeInDirection(Island island, int dx, int dy) {
        Island neighbor = findNextInDirection(island.getX(), island.getY(), dx, dy);
        if (neighbor == null) {
            return null;
        }
        Connection existingConnection = getExistingBridge(island, neighbor);
        if (existingConnection != null) {
            return existingConnection;
        }

        int min = Math.min(island.getId(), neighbor.getId());
        int max = Math.max(island.getId(), neighbor.getId());
        Connection connection = new Connection(islands.getById(min), islands.getById(max));
        this.connections.add(connection);
        return connection;
    }

    private Connection getExistingBridge(Island island, Island other) {
        int a = Math.min(island.getId(), other.getId());
        int b = Math.max(island.getId(), other.getId());

        for (Connection connection : this.connections) {
            if (connection.getStartIsland().getId() == a && connection.getEndIsland().getId() == b) {
                return connection;
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
