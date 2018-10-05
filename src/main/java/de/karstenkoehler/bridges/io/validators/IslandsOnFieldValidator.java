package de.karstenkoehler.bridges.io.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.ValidateException;
import de.karstenkoehler.bridges.model.Node;

public class IslandsOnFieldValidator implements Validator {
    private static final int MIN_COORDINATE = 0;

    @Override
    public void validate(ParseResult result) throws ValidateException {
        for (Node island : result.getIslands().values()) {
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
