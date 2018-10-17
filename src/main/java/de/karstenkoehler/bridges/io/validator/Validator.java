package de.karstenkoehler.bridges.io.validator;

import de.karstenkoehler.bridges.model.BridgesPuzzle;

public interface Validator {
    /**
     * Applies some kind of validation to the parameter.
     *
     * @param puzzle the structure to validate
     * @throws ValidateException indicates that some value of the structure is not valid
     */
    void validate(final BridgesPuzzle puzzle) throws ValidateException;
}
