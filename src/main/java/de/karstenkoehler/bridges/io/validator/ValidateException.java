package de.karstenkoehler.bridges.io.validator;

/**
 * This exception indicates there was an issue while validating a puzzle.
 */
public class ValidateException extends Exception {
    public ValidateException(String message) {
        super(message);
    }
}
