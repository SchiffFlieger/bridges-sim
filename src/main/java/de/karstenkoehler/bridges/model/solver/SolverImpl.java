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
            Bridge save = findSaveBridge(island, puzzle);
            if (save != null) {
                return save;
            }
        }

        return null;
    }

    private Bridge findSaveBridge(Island island, BridgesPuzzle puzzle) {
        int remaining = puzzle.getRemainingBridgeCount(island);
        if (remaining == 0) {
            return null;
        }

        List<Bridge> neighbors = getPossibleNeighborBridges(island, puzzle);
        int calc = (neighbors.size() * 2) - remaining;
//        System.out.printf("neighbors: %d, remaining: %d, calc: %d%n", neighbors.size(), puzzle.getRemainingBridgeCount(island), calc);

        if (calc == 0) {
            // every adjacent island has double bridge
            Bridge bridge = assureAtLeastXBridges(neighbors, 2);
            if (bridge != null) return bridge;
        }
        if (calc == 1) {
            // every adjacent island has at least single bridge
            Bridge bridge = assureAtLeastXBridges(neighbors, 2);
            if (bridge != null) return bridge;
        }

        return null;
    }

    private Bridge assureAtLeastXBridges(List<Bridge> bridges, int minBridgeCount) {
        for (Bridge bridge : bridges) {
            if (bridge != null && bridge.getBridgeCount() < minBridgeCount) {
                return bridge;
            }
        }
        return null;
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
