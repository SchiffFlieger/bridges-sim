package de.karstenkoehler.bridges.io.parser.token;

import de.karstenkoehler.bridges.io.parser.ParseException;

/**
 * A tokenizer splits a given input string into its individual components (tokens).
 */
public interface Tokenizer {
    /**
     * Returns the next token, that has not already been read.
     * <p>
     * If the whole input has been processed, a special 'end of file'
     * token should be returned. All subsequent calls to this method should return this special token.
     *
     * @return the next unread token
     * @throws ParseException if the input string contains unexpected characters and no valid token can be derived
     */
    Token next() throws ParseException;
}
