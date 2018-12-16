package de.karstenkoehler.bridges.io.parser;

import de.karstenkoehler.bridges.io.parser.token.Token;
import de.karstenkoehler.bridges.io.parser.token.Tokenizer;

/**
 * A base class for reading tokens from a {@link Tokenizer}. It offers methods to peek, check and consume
 * the tokens supplied by the tokenizer.
 */
abstract class AbstractTokenParser {
    private final Tokenizer tokenizer;
    private Token current;

    /**
     * @param tokenizer the tokenizer to read the tokens from
     */
    AbstractTokenParser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    /**
     * Consumes the token if it has the given type.
     *
     * @param type the expected type
     * @throws ParseException if the token does not match the given type
     */
    void consume(Token.Type type) throws ParseException {
        if (this.token().getType() != type) {
            throw new ParseException(String.format("Found unexpected token %s, expected: %s", current.getType(), type));
        }
        this.current = this.tokenizer.next();
    }

    /**
     * Peeks the current token. Multiple successive calls will return the same token as long as it has not been consumed.
     *
     * @return the current token to process
     * @throws ParseException if the next token could not be created
     */
    Token token() throws ParseException {
        if (this.current == null) {
            this.current = this.tokenizer.next();
        }
        return this.current;
    }
}
