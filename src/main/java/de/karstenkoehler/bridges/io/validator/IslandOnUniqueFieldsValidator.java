package de.karstenkoehler.bridges.io.validator;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Island;

/**
 * Checks if all islands are on separate fields. Islands are on separate fields
 * if they do not share the same x and y coordinates.
 */
public class IslandOnUniqueFieldsValidator implements Validator {

    // TODO merge with NoIslandsOnAdjacentFieldsValidator

    /**
     * @see Validator#validate(BridgesPuzzle)
     */
    @Override
    public void validate(BridgesPuzzle puzzle) throws ValidateException {
        for (Island island : puzzle.getIslands()) {
            for (Island other : puzzle.getIslands()) {
                if (islandsOnSameField(island, other)) {
                    throw new ValidateException(String.format("there are two islands on field (%d, %d).",
                            island.getX(), island.getY()));
                }
            }
        }
    }

    private boolean islandsOnSameField(Island island, Island other) {
        return island.getId() != other.getId() && island.getX() == other.getX() && island.getY() == other.getY();
    }
}
