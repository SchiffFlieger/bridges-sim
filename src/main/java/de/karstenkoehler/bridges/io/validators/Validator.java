package de.karstenkoehler.bridges.io.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.ValidateException;

public interface Validator {
    /**
     * Applies some kind of validation to the parameter.
     *
     * @param result the structure to validate
     * @throws ValidateException indicates that some value of the structure is not valid
     */
    void validate(final ParseResult result) throws ValidateException;
}
