package de.karstenkoehler.bridges.model.solver;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.Island;
import de.karstenkoehler.bridges.model.Orientation;

import java.util.ArrayList;
import java.util.List;

/**
 * A solver that draws logical conclusions based on the rules of the puzzle and thus notices
 * where a safe bridge can be placed.
 */
public class SolverImpl implements Solver {

    /**
     * @see Solver#nextSafeBridge(BridgesPuzzle)
     */
    @Override
    public Connection nextSafeBridge(BridgesPuzzle puzzle) {
        for (Island island : puzzle.getIslands()) {
            Connection save = findSafeBridge(island, puzzle);
            if (save != null) {
                return save;
            }
        }

        return null;
    }

    /**
     * Checks if one of the connected bridges of the island is considered safe. Returns <code>null</code> if
     * there are no safe bridges near the island.
     *
     * @param island the island where safe bridges are sought
     * @param puzzle the puzzle that contains the island
     * @return a safe bridge
     */
    private Connection findSafeBridge(Island island, BridgesPuzzle puzzle) {
        int remainingBridges = puzzle.getRemainingBridgeCount(island);
        if (remainingBridges == 0) {
            return null;
        }

        List<Connection> possibleNeighborBridges = getPossibleNeighborBridges(island, puzzle);
        int possibleNeighborBridgeCount = possibleNeighborBridges.stream().mapToInt((bridge) -> bridgesPossible(bridge, puzzle)).sum();

        if (possibleNeighborBridgeCount - remainingBridges == 0) {
            return possibleNeighborBridges.stream().filter(bridge -> bridge.getBridgeCount() < 2).findFirst().orElse(null);
        }

        if (possibleNeighborBridgeCount - remainingBridges == 1) {
            return possibleNeighborBridges.stream().filter(bridge -> bridgesPossible(bridge, puzzle) == 2).findFirst().orElse(null);
        }

        return null;
    }

    /**
     * Calculates how many additional bridges can be build on the given connection.
     */
    private int bridgesPossible(Connection connection, BridgesPuzzle puzzle) {
        int bridgesPossible = 2 - connection.getBridgeCount();
        int remainingBridgesStart = puzzle.getRemainingBridgeCount(connection.getStartIsland());
        int remainingBridgesEnd = puzzle.getRemainingBridgeCount(connection.getEndIsland());

        return Math.min(Math.min(bridgesPossible, remainingBridgesStart), remainingBridgesEnd);
    }

    /**
     * Returns all connected bridges of an island, considering the fact that these bridges are actually safe.
     */
    private List<Connection> getPossibleNeighborBridges(Island island, BridgesPuzzle puzzle) {
        List<Connection> result = new ArrayList<>(4);

        for (Orientation orientation : Orientation.values()) {
            Connection connection = puzzle.getConnectedBridge(island, orientation);
            if (isConsideredSafe(connection, puzzle)) {
                result.add(connection);
            }
        }

        return result;
    }

    private boolean isConsideredSafe(Connection connection, BridgesPuzzle puzzle) {
        return connection != null && connection.getBridgeCount() < 2
                && !puzzle.causesCrossing(connection)
                && !onlyTwoIslandsPuzzle(connection, puzzle)
                && !(puzzle.getRemainingBridgeCount(connection.getStartIsland()) == 0)
                && !(puzzle.getRemainingBridgeCount(connection.getEndIsland()) == 0);
    }

    private boolean onlyTwoIslandsPuzzle(Connection connection, BridgesPuzzle puzzle) {
        return connection.getStartIsland().getRequiredBridges() == 1
                && connection.getEndIsland().getRequiredBridges() == 1
                && puzzle.getIslands().size() != 2;
    }
}
