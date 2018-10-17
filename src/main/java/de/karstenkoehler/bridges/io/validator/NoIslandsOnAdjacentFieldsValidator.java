package de.karstenkoehler.bridges.io.validator;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Island;

/**
 * Checks if there are any islands on directly adjacent fields. Adjacent means
 * that either the x or y coordinate of the islands deviate by one. If both
 * deviate by one, they are diagonal adjacent, which is allowed.
 */
public class NoIslandsOnAdjacentFieldsValidator implements Validator {
    @Override
    public void validate(BridgesPuzzle puzzle) throws ValidateException {
        for (Island island : puzzle.getIslands()) {
            for (Island other : puzzle.getIslands()) {
                if (island != other && (adjacentOnX(island, other) || adjacentOnY(island, other))) {
                    throw new ValidateException(String.format("island (%d, %d) is adjacent to island (%d, %d). there must be at least one free space in between.",
                            island.getX(), island.getY(), other.getX(), other.getY()));
                }
            }
        }
    }

    private boolean adjacentOnX(Island island, Island other) {
        return island.getX() == other.getX() && Math.abs(island.getY() - other.getY()) < 2;
    }

    private boolean adjacentOnY(Island island, Island other) {
        return island.getY() == other.getY() && Math.abs(island.getX() - other.getX()) < 2;
    }
}
