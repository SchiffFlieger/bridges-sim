package de.karstenkoehler.bridges.io.validator;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.Island;

/**
 * Checks if every defined bridge is either horizontal or vertical. This means the
 * two islands of the bridge must share either their x or y coordinate.
 */
public class BridgesNotDiagonalValidator implements Validator {
    @Override
    public void validate(BridgesPuzzle puzzle) throws ValidateException {
        for (Connection bridge : puzzle.getConnections()) {
            if (isDiagonal(bridge.getStartIsland(), bridge.getEndIsland())) {
                throw new ValidateException("A bridge is neither horizontal or vertical.");
            }
        }
    }

    private boolean isDiagonal(Island a, Island b) {
        return a.getX() != b.getX() && a.getY() != b.getY();
    }
}
