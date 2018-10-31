package de.karstenkoehler.bridges.io.validator;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Island;

/**
 * Checks if all islands are placed within the bound of the field. A island is
 * within the bounds if the x coordinate is in range [0, field width - 1] and the
 * y coordinate is in range [0, field height - 1].
 */
public class IslandsOnFieldValidator implements Validator {
    private static final int MIN_COORDINATE = 0;

    /**
     * @see Validator#validate(BridgesPuzzle)
     */
    @Override
    public void validate(BridgesPuzzle puzzle) throws ValidateException {
        for (Island island : puzzle.getIslands()) {
            checkValueInRange(island, island.getX(), puzzle.getWidth());
            checkValueInRange(island, island.getY(), puzzle.getHeight());
        }
    }

    private void checkValueInRange(Island island, int value, int max) throws ValidateException {
        if (value < MIN_COORDINATE || value > max - 1) {
            throw new ValidateException(String.format("island at position (%d, %d) is off the field.",
                    island.getX(), island.getY()));
        }
    }
}
