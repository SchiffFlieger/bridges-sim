package de.karstenkoehler.bridges.io.validator;

import de.karstenkoehler.bridges.model.BridgesPuzzle;

/**
 * A validator executes some kind of validation process on the puzzle.
 */
public interface Validator {
    /**
     * Applies some kind of validation to the puzzle.
     *
     * @param puzzle the puzzle to validate
     * @throws ValidateException indicates that some properties of the puzzle are not valid
     */
    void validate(final BridgesPuzzle puzzle) throws ValidateException;
}
