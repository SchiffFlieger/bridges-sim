package de.karstenkoehler.bridges.io.parser;

/**
 * This exception indicates there was an issue while parsing an input string.
 */
public class ParseException extends Exception {
    public ParseException(String message) {
        super(message);
    }
}
