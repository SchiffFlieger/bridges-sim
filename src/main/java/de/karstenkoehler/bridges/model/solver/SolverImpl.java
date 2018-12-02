package de.karstenkoehler.bridges.model.solver;

import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Island;
import de.karstenkoehler.bridges.model.Orientation;

import java.util.ArrayList;
import java.util.List;

public class SolverImpl implements Solver {

    @Override
    public Bridge nextSafeBridge(BridgesPuzzle puzzle) {
        for (Island island : puzzle.getIslands()) {
            Bridge save = findNextSimple(island, puzzle);
            if (save != null) {
                return save;
            }
        }

        for (Island island : puzzle.getIslands()) {
            Bridge save = findNextExtended(island, puzzle);
            if (save != null) {
                return save;
            }
        }

        return null;
    }

    private Bridge findNextSimple(Island island, BridgesPuzzle puzzle) {
        int remaining = puzzle.getRemainingBridgeCount(island);
        if (remaining == 0) {
            return null;
        }

        List<Bridge> neighbors = getPossibleNeighborBridges(island, puzzle);

        int val = (neighbors.size() * 2) - remaining;
        if (val >= 0 && val <= 1) {
            Bridge bridge = findFirstWithLessBridges(neighbors, 2);
            if (bridge != null) return bridge;
        }

        int possibleBridges = getMaxPossibleBridges(neighbors, puzzle);
        if (possibleBridges == remaining) {
            Bridge bridge = findFirstWithLessBridges(neighbors, 2);
            if (bridge != null) return bridge;
        }

        return null;
    }

    private Bridge findNextExtended(Island island, BridgesPuzzle puzzle) {
        List<Bridge> possibleNeighborBridges = getPossibleNeighborBridges(island, puzzle);
        int possibleNeighborBridgeCount = possibleNeighborBridges.stream().mapToInt((bridge) -> {
            int bridgesPossible = 2 - bridge.getBridgeCount();
            int bridgesNeededByOtherIsland = puzzle.getRemainingBridgeCount(getOtherIsland(bridge, island));

            return Math.min(bridgesNeededByOtherIsland, bridgesPossible);
        }).sum();
        int remainingBridges = puzzle.getRemainingBridgeCount(island);

        if (possibleNeighborBridgeCount - remainingBridges == 0) {
            return findFirstWithLessBridges(possibleNeighborBridges, 2);
        }

        if (possibleNeighborBridgeCount - remainingBridges == 1) {
            return findFirstWithLessBridges(possibleNeighborBridges, 1);
        }

        return null;
    }

    private Bridge findFirstWithLessBridges(List<Bridge> bridges, int minBridgeCount) {
        return bridges.stream().filter(bridge -> bridge.getBridgeCount() < minBridgeCount).findFirst().orElse(null);
    }

    private List<Bridge> getPossibleNeighborBridges(Island island, BridgesPuzzle puzzle) {
        List<Bridge> result = new ArrayList<>(4);

        for (Orientation orientation : Orientation.values()) {
            Bridge bridge = puzzle.getConnectedBridge(island, orientation);
            if (bridge != null && bridge.getBridgeCount() < 2 && !puzzle.causesCrossing(bridge)) {
                Island other = getOtherIsland(bridge, island);
                if ((island.getRequiredBridges() == 1 && other.getRequiredBridges() == 1 && puzzle.getIslands().size() != 2) || puzzle.getRemainingBridgeCount(other) == 0) {
                    continue;
                }
                result.add(bridge);
            }
        }

        return result;
    }

    private Island getOtherIsland(Bridge bridge, Island island) {
        if (bridge.getStartIsland() == island) {
            return bridge.getEndIsland();
        }
        return bridge.getStartIsland();
    }

    private int getMaxPossibleBridges(List<Bridge> bridges, BridgesPuzzle puzzle) {
        int sum = 0;
        for (Bridge bridge : bridges) {
            int val = 2 - bridge.getBridgeCount();
            if (puzzle.getRemainingBridgeCount(bridge.getStartIsland()) == 1 || puzzle.getRemainingBridgeCount(bridge.getEndIsland()) == 1) {
                val = Math.min(val, 1);
            }
            sum += val;
        }
        return sum;
    }
}
