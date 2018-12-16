package de.karstenkoehler.bridges.io.validator;

import de.karstenkoehler.bridges.model.BridgesPuzzle;

/**
 * Checks if the field's bounds are within the valid range. The dimensions on each axis
 * must be at least 4 and at most 25 fields.
 */
public class FieldSizeValidator implements Validator {
    private static final int MIN_FIELD_DIMENSION = 4;
    private static final int MAX_FIELD_DIMENSION = 25;

    /**
     * @see Validator#validate(BridgesPuzzle)
     */
    @Override
    public void validate(BridgesPuzzle puzzle) throws ValidateException {
        checkValue("width", puzzle.getWidth());
        checkValue("height", puzzle.getHeight());
    }

    private void checkValue(final String name, final int value) throws ValidateException {
        if (value < MIN_FIELD_DIMENSION || value > MAX_FIELD_DIMENSION) {
            throw new ValidateException(String.format("The field %s should be in range %d to %d.",
                    name, MIN_FIELD_DIMENSION, MAX_FIELD_DIMENSION));
        }
    }
}
