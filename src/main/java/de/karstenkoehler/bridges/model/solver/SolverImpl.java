package de.karstenkoehler.bridges.model.solver;

import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
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
    public Bridge nextSafeBridge(BridgesPuzzle puzzle) {
        for (Island island : puzzle.getIslands()) {
            Bridge save = findSafeBridge(island, puzzle);
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
    private Bridge findSafeBridge(Island island, BridgesPuzzle puzzle) {
        int remainingBridges = puzzle.getRemainingBridgeCount(island);
        if (remainingBridges == 0) {
            return null;
        }

        List<Bridge> possibleNeighborBridges = getPossibleNeighborBridges(island, puzzle);
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
     * Calculates how many additional bridges can be build on the given bridge.
     */
    private int bridgesPossible(Bridge bridge, BridgesPuzzle puzzle) {
        int bridgesPossible = 2 - bridge.getBridgeCount();
        int remainingBridgesStart = puzzle.getRemainingBridgeCount(bridge.getStartIsland());
        int remainingBridgesEnd = puzzle.getRemainingBridgeCount(bridge.getEndIsland());

        return Math.min(Math.min(bridgesPossible, remainingBridgesStart), remainingBridgesEnd);
    }

    /**
     * Returns all connected bridges of an island, considering the fact that these bridges are actually safe.
     */
    private List<Bridge> getPossibleNeighborBridges(Island island, BridgesPuzzle puzzle) {
        List<Bridge> result = new ArrayList<>(4);

        for (Orientation orientation : Orientation.values()) {
            Bridge bridge = puzzle.getConnectedBridge(island, orientation);
            if (isConsideredSafe(bridge, puzzle)) {
                result.add(bridge);
            }
        }

        return result;
    }

    private boolean isConsideredSafe(Bridge bridge, BridgesPuzzle puzzle) {
        return bridge != null && bridge.getBridgeCount() < 2
                && !puzzle.causesCrossing(bridge)
                && !onlyTwoIslandsPuzzle(bridge, puzzle)
                && !(puzzle.getRemainingBridgeCount(bridge.getStartIsland()) == 0)
                && !(puzzle.getRemainingBridgeCount(bridge.getEndIsland()) == 0);
    }

    private boolean onlyTwoIslandsPuzzle(Bridge bridge, BridgesPuzzle puzzle) {
        return bridge.getStartIsland().getRequiredBridges() == 1
                && bridge.getEndIsland().getRequiredBridges() == 1
                && puzzle.getIslands().size() != 2;
    }
}
