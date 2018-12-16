package de.karstenkoehler.bridges.io.validator;

import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Island;

import java.util.List;

/**
 * Checks if any defined bridge runs across another island. This third island must be between
 * the two connected islands. Does not throw an exception if the bridge is diagonal or
 * connects to the same island twice.
 */
public class BridgesDoNotCrossIslandValidator implements Validator {
    @Override
    public void validate(BridgesPuzzle puzzle) throws ValidateException {
        for (Bridge bridge : puzzle.getBridges()) {
            if (bridgeCrossesIsland(bridge, puzzle.getIslands())) {
                throw new ValidateException("A bridge runs over another island.");
            }
        }
    }

    private boolean bridgeCrossesIsland(Bridge bridge, List<Island> islands) {
        Island startIsland = bridge.getStartIsland();
        Island endIsland = bridge.getEndIsland();

        int x = startIsland.getX();
        int y = startIsland.getY();

        int dx = Math.min(1, Math.max(-1, endIsland.getX() - startIsland.getX()));
        int dy = Math.min(1, Math.max(-1, endIsland.getY() - startIsland.getY()));

        x += dx;
        y += dy;
        while (shouldContinue(endIsland, x, y, dx, dy)) {
            if (existsIslandWithCoordinates(x, y, islands)) {
                return true;
            }

            x += dx;
            y += dy;
        }
        return false;
    }

    private boolean shouldContinue(Island endIsland, int x, int y, int dx, int dy) {
        if (dx == dy) { // bridge is either diagonal or connects the same island twice
            return false;
        }
        if (dx < 0) { // left
            return x > endIsland.getX();
        }
        if (dx > 0) { // right
            return x < endIsland.getX();
        }
        if (dy < 0) { // up
            return y > endIsland.getY();
        }

        // down
        return y < endIsland.getY();
    }

    private boolean existsIslandWithCoordinates(int x, int y, List<Island> islands) {
        for (Island island : islands) {
            if (x == island.getX() && y == island.getY()) {
                return true;
            }
        }
        return false;
    }
}
