package de.karstenkoehler.bridges.io.validator;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Island;

/**
 * Checks if there are any islands on directly adjacent or the exactly same fields. Adjacent means
 * that either the x or y coordinate of the islands deviate by one. If both deviate by one,
 * they are diagonal adjacent, which is allowed.
 */
public class IslandPlacementValidator implements Validator {
    /**
     * @see Validator#validate(BridgesPuzzle)
     */
    @Override
    public void validate(BridgesPuzzle puzzle) throws ValidateException {
        int size = puzzle.getIslands().size();

        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                Island island = puzzle.getIslands().get(i);
                Island other = puzzle.getIslands().get(j);

                if (islandsOnSameField(island, other)) {
                    throw new ValidateException(String.format("There are two islands in position (%d, %d).",
                            island.getX(), island.getY()));

                }
                if (island != other && (adjacentOnX(island, other) || adjacentOnY(island, other))) {
                    throw new ValidateException(String.format("Island (%d, %d) is adjacent to island (%d, %d). There must be at least one free space in between.",
                            island.getX(), island.getY(), other.getX(), other.getY()));
                }
            }
        }
    }

    private boolean islandsOnSameField(Island island, Island other) {
        return island.getId() != other.getId() && island.getX() == other.getX() && island.getY() == other.getY();
    }

    private boolean adjacentOnX(Island island, Island other) {
        return island.getX() == other.getX() && Math.abs(island.getY() - other.getY()) < 2;
    }

    private boolean adjacentOnY(Island island, Island other) {
        return island.getY() == other.getY() && Math.abs(island.getX() - other.getX()) < 2;
    }
}
