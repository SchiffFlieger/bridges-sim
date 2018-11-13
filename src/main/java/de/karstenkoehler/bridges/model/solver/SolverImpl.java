package de.karstenkoehler.bridges.model.solver;

import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Island;
import de.karstenkoehler.bridges.model.Orientation;

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
        int neighbors = getNeighborCount(island, puzzle);

        if ((neighbors * 2) - island.getRequiredBridges() == 0) {
            // every adjacent island has double bridge
            Bridge bridge = assureAtLeastXBridges(island, puzzle, 2);
            if (bridge != null) return bridge;
        }
        if ((neighbors * 2) - island.getRequiredBridges() == 1) {
            // every adjacent island has at least single bridge
            Bridge bridge = assureAtLeastXBridges(island, puzzle, 1);
            if (bridge != null) return bridge;
        }

        return null;
    }

    private Bridge assureAtLeastXBridges(Island island, BridgesPuzzle puzzle, int minBridgeCount) {
        for (Orientation orientation : Orientation.values()) {
            Bridge bridge = puzzle.getConnectedBridge(island, orientation);
            if (bridge != null && bridge.getBridgeCount() < minBridgeCount) {
                return bridge;
            }
        }
        return null;
    }

    // TODO move to bridge class
    private int getNeighborCount(Island island, BridgesPuzzle puzzle) {
        int neighbors = 0;
        for (Orientation orientation : Orientation.values()) {
            if (puzzle.getConnectedBridge(island, orientation) != null) {
                neighbors++;
            }
        }
        return neighbors;
    }
}
