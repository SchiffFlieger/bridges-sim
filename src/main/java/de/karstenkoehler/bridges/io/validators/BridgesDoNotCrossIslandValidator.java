package de.karstenkoehler.bridges.io.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.ValidateException;
import de.karstenkoehler.bridges.model.Edge;
import de.karstenkoehler.bridges.model.Node;

import java.util.Map;

/**
 * Checks if any defined bridge runs across another island. This third island must be between
 * the two connected islands. Does not throw an exception if the bridge is diagonal or
 * connects to the same island twice.
 */
public class BridgesDoNotCrossIslandValidator implements Validator {
    @Override
    public void validate(ParseResult result) throws ValidateException {
        for (Edge bridge : result.getBridges()) {
            if (bridgeCrossesIsland(bridge, result.getIslands())) {
                throw new ValidateException(String.format("bridge %d runs over another island.", bridge.getId()));
            }
        }
    }

    private boolean bridgeCrossesIsland(Edge bridge, Map<Integer, Node> islands) {
        Node startIsland = islands.get(bridge.getNode1());
        Node endIsland = islands.get(bridge.getNode2());

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

    private boolean shouldContinue(Node endIsland, int x, int y, int dx, int dy) {
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

    private boolean existsIslandWithCoordinates(int x, int y, Map<Integer, Node> islands) {
        for (Node island : islands.values()) {
            if (x == island.getX() && y == island.getY()) {
                return true;
            }
        }
        return false;
    }
}
