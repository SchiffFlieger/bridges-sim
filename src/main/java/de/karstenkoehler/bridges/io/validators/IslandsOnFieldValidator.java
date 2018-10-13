package de.karstenkoehler.bridges.io.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.model.Node;

/**
 * Checks if all islands are placed within the bound of the field. A island is
 * within the bounds if the x coordinate is in range [0, field width - 1] and the
 * y coordinate is in range [0, field height - 1].
 */
public class IslandsOnFieldValidator implements Validator {
    private static final int MIN_COORDINATE = 0;

    @Override
    public void validate(ParseResult result) throws ValidateException {
        for (Node island : result.getIslands()) {
            checkValueInRange(island, island.getX(), result.getWidth());
            checkValueInRange(island, island.getY(), result.getHeight());
        }
    }

    private void checkValueInRange(Node island, int value, int max) throws ValidateException {
        if (value < MIN_COORDINATE || value > max - 1) {
            throw new ValidateException(String.format("island at position (%d, %d) is off the field.",
                    island.getX(), island.getY()));
        }
    }
}
