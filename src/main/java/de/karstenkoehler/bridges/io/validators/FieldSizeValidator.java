package de.karstenkoehler.bridges.io.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.ValidateException;

/**
 * Checks if the field's bounds are within the valid range. The dimensions on each axis
 * must be at least 4 and at most 25 fields.
 */
public class FieldSizeValidator implements Validator {
    private static final int MIN_FIELD_DIMENSION = 4;
    private static final int MAX_FIELD_DIMENSION = 25;

    @Override
    public void validate(ParseResult result) throws ValidateException {
        checkValue("width", result.getWidth());
        checkValue("height", result.getHeight());
    }

    private void checkValue(final String name, final int value) throws ValidateException {
        if (value < MIN_FIELD_DIMENSION || value > MAX_FIELD_DIMENSION) {
            throw new ValidateException(String.format("field %s is %d. should be in range %d to %d.",
                    name, value, MIN_FIELD_DIMENSION, MAX_FIELD_DIMENSION));
        }
    }
}
