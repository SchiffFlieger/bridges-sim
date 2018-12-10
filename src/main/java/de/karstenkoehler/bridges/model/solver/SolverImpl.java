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
            Bridge save = findNextExtended(island, puzzle);
            if (save != null) {
                return save;
            }
        }

        return null;
    }

    private Bridge findNextExtended(Island island, BridgesPuzzle puzzle) {
        int remainingBridges = puzzle.getRemainingBridgeCount(island);
        if (remainingBridges == 0) {
            return null;
        }

        List<Bridge> possibleNeighborBridges = getPossibleNeighborBridges(island, puzzle);
        int possibleNeighborBridgeCount = possibleNeighborBridges.stream().mapToInt((bridge) -> bridgesNeeded(island, puzzle, bridge)).sum();

        if (possibleNeighborBridgeCount - remainingBridges == 0) {
            return findFirstPossibleBridge(possibleNeighborBridges);
        }

        if (possibleNeighborBridgeCount - remainingBridges == 1) {
            return possibleNeighborBridges.stream().filter(bridge -> bridgesNeeded(island, puzzle, bridge) == 2).findFirst().orElse(null);
        }

        return null;
    }

    private int bridgesNeeded(Island island, BridgesPuzzle puzzle, Bridge bridge) {
        int bridgesPossible = 2 - bridge.getBridgeCount();
        int bridgesNeededByOtherIsland = puzzle.getRemainingBridgeCount(getOtherIsland(bridge, island));

        return Math.min(bridgesNeededByOtherIsland, bridgesPossible);
    }

    private Bridge findFirstPossibleBridge(List<Bridge> bridges) {
        return bridges.stream().filter(bridge -> bridge.getBridgeCount() < 2).findFirst().orElse(null);
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
}
