package de.karstenkoehler.bridges.io.validator;

import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Island;

/**
 * Checks if every defined bridge is either horizontal or vertical. This means the
 * two islands of the bridge must share either their x or y coordinate.
 */
public class BridgesNotDiagonalValidator implements Validator {
    @Override
    public void validate(BridgesPuzzle puzzle) throws ValidateException {
        for (Bridge bridge : puzzle.getBridges()) {
            Island a = puzzle.getIslands().get(bridge.getStartIsland());
            Island b = puzzle.getIslands().get(bridge.getEndIsland());
            if (isDiagonal(a, b)) {
                throw new ValidateException(String.format("bridge %d is neither horizontal or vertical.", bridge.getId()));
            }
        }
    }

    private boolean isDiagonal(Island a, Island b) {
        return a.getX() != b.getX() && a.getY() != b.getY();
    }
}
