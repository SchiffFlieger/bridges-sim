package de.karstenkoehler.bridges.model;

import java.util.*;

/**
 * This class represents a single bridges puzzle. It uses a kind of graph implementation with instances
 * of {@link Island} acting as nodes and instances of {@link Connection} acting as edges.
 */
public class BridgesPuzzle {
    private final IslandStore islands;
    private final List<Connection> connections;
    private final Map<Integer, Map<Orientation, Connection>> bridgeConnections;
    private final int width;
    private final int height;

    /**
     * Creates a new representation for a bridges puzzle. Make sure to call {@link BridgesPuzzle#fillMissingConnections()}
     * immediately after the constructor, so that the internal graph representation gets filled.
     *
     * @param islands the islands of the puzzle
     * @param bridges the bridges of the puzzle
     * @param width   the width of the puzzle
     * @param height  the height of the puzzle
     */
    public BridgesPuzzle(List<Island> islands, List<Connection> bridges, int width, int height) {
        this.islands = new IslandStore(islands);
        this.connections = bridges;
        this.width = width;
        this.height = height;
        this.bridgeConnections = new HashMap<>();
    }

    /**
     * @return a list of all islands in the puzzle
     */
    public List<Island> getIslands() {
        return Collections.unmodifiableList(this.islands.getAsList());
    }

    /**
     * @return a list of all connections in the puzzle
     */
    public List<Connection> getConnections() {
        return Collections.unmodifiableList(this.connections);
    }

    /**
     * @return the width of the puzzle
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height of the puzzle
     */
    public int getHeight() {
        return height;
    }

    public void emphasizeBridge(Connection connection) {
        this.connections.forEach(b -> b.setEmphasized(false));
        if (connection != null) {
            connection.setEmphasized(true);
        }
    }

    /**
     * Returns the connection attached to the island in the specified direction.
     *
     * @return the connection
     */
    public Connection getConnectedBridge(Island island, Orientation orientation) {
        return this.bridgeConnections.get(island.getId()).get(orientation);
    }

    /**
     * Returns the remaining number of bridges to complete the given island.
     */
    public int getRemainingBridgeCount(Island island) {
        int bridgeCount = 0;
        for (Connection connection : this.bridgeConnections.get(island.getId()).values()) {
            if (connection != null) {
                bridgeCount += connection.getBridgeCount();
            }
        }

        return island.getRequiredBridges() - bridgeCount;
    }

    /**
     * Identifies bridges that are crossing each other and marks them.
     */
    public void markInvalidBridges() {
        this.connections.forEach(b -> b.setValid(true));

        for (Connection connection : this.connections) {
            for (Connection other : this.connections) {
                if (connection == other || connection.getBridgeCount() == 0 || other.getBridgeCount() == 0) {
                    continue;
                }

                if (connection.isHorizontal() && other.isVertical()) {
                    if (areConnectionsCrossing(connection, other)) {
                        connection.setValid(false);
                        other.setValid(false);
                    }
                }

                if (connection.isVertical() && other.isHorizontal()) {
                    if (areConnectionsCrossing(other, connection)) {
                        connection.setValid(false);
                        other.setValid(false);
                    }
                }
            }
        }
    }

    /**
     * Removes all bridges from the puzzle.
     */
    public void restart() {
        this.connections.forEach(bridge -> bridge.setBridgeCount(0));
    }

    /**
     * Evaluates the current state of the puzzle.
     */
    public PuzzleState getState() {
        if (anyBridgesCrossing() || anyIslandsTooManyBridges()) {
            return PuzzleState.ERROR;
        }

        if (isSolved()) {
            return PuzzleState.SOLVED;
        }

        if (!hasPossibleBridges()) {
            return PuzzleState.NO_LONGER_SOLVABLE;
        }

        return PuzzleState.NOT_SOLVED;
    }

    /**
     * @return true if any valid bridge can be added to the puzzle, false otherwise
     */
    private boolean hasPossibleBridges() {
        return this.connections.stream().anyMatch(this::hasPossibleBridges);
    }

    private boolean hasPossibleBridges(Connection connection) {
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

    /**
     * @return true if adding a bridge to the connection would result in a crossing, false otherwise
     */
    public boolean causesCrossing(Connection bridge) {
        for (Connection other : this.connections) {
            if (bridge == other || other.getBridgeCount() == 0) {
                continue;
            }

            if (bridge.isHorizontal() && other.isVertical()) {
                if (areConnectionsCrossing(bridge, other)) {
                    return true;
                }
            }

            if (bridge.isVertical() && other.isHorizontal()) {
                if (areConnectionsCrossing(other, bridge)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return true if the puzzle is solved correctly, false otherwise
     */
    private boolean isSolved() {
        return everyIslandSatisfied() && everyIslandConnected();
    }

    /**
     * @return true if all islands are indirectly connected to each other
     */
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

    /**
     * @return true if every island has the desired amount of bridges connected, false otherwise
     */
    private boolean everyIslandSatisfied() {
        return this.islands.getAsList().stream().allMatch(island -> getRemainingBridgeCount(island) == 0);
    }

    /**
     * Returns the island relative to the given island in the given direction.
     */
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

    /**
     * @return true if there are any bridges crossing, false otherwise
     */
    private boolean anyBridgesCrossing() {
        markInvalidBridges();
        return this.connections.stream().anyMatch(bridge -> !bridge.isValid());
    }

    /**
     * @return true if any island has more bridges connected than it needs
     */
    private boolean anyIslandsTooManyBridges() {
        return this.islands.getAsList().stream().anyMatch(island -> getRemainingBridgeCount(island) < 0);
    }

    /**
     * Checks if the two connections are crossing each other. Returns true even if the given connections
     * have no bridges assigned to them.
     */
    private boolean areConnectionsCrossing(Connection connection, Connection other) {
        int y1 = connection.getStartIsland().getY();
        int x2 = other.getEndIsland().getX();

        int x1a = connection.getStartIsland().getX();
        int x1e = connection.getEndIsland().getX();
        int y2a = other.getStartIsland().getY();
        int y2e = other.getEndIsland().getY();
        return x1a < x2 && x2 < x1e && y2a < y1 && y1 < y2e;
    }

    /**
     * Creates all missing {@link Connection} objects, so that each island is connected to its neighbors.
     */
    public void fillMissingConnections() {
        for (Island island : this.islands.getAsList()) {
            Map<Orientation, Connection> connections = new EnumMap<>(Orientation.class);

            connections.put(Orientation.NORTH, findConnectionInDirection(island, 0, -1));
            connections.put(Orientation.EAST, findConnectionInDirection(island, 1, 0));
            connections.put(Orientation.SOUTH, findConnectionInDirection(island, 0, 1));
            connections.put(Orientation.WEST, findConnectionInDirection(island, -1, 0));

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

    /**
     * Seeks the next island in the specified direction, then checks if the two islands are connected.
     * If there is no connection a new one is created.
     */
    private Connection findConnectionInDirection(Island island, int dx, int dy) {
        Island neighbor = findIslandInDirection(island.getX(), island.getY(), dx, dy);
        if (neighbor == null) {
            return null;
        }
        Connection existingConnection = getExistingConnection(island, neighbor);
        if (existingConnection != null) {
            return existingConnection;
        }

        int min = Math.min(island.getId(), neighbor.getId());
        int max = Math.max(island.getId(), neighbor.getId());
        Connection connection = new Connection(islands.getById(min), islands.getById(max));
        this.connections.add(connection);
        return connection;
    }

    /**
     * Returns the connection between the two given islands, if there is one.
     */
    private Connection getExistingConnection(Island island, Island other) {
        int a = Math.min(island.getId(), other.getId());
        int b = Math.max(island.getId(), other.getId());

        for (Connection connection : this.connections) {
            if (connection.getStartIsland().getId() == a && connection.getEndIsland().getId() == b) {
                return connection;
            }
        }
        return null;
    }

    /**
     * Seeks islands from the given starting coordinates. Either <code>dx</code> or <code>dy</code>
     * should be 0, otherwise this method searches along a diagonal path.
     *
     * @param x  the x starting coordinate
     * @param y  the y starting coordinate
     * @param dx use -1 to search on the left, +1 to search on the right
     * @param dy use -1 to search on the top, +1 to search on the bottom
     * @return the first island found
     */
    private Island findIslandInDirection(int x, int y, int dx, int dy) {
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
