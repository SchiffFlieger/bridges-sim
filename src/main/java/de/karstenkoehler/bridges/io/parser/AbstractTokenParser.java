package de.karstenkoehler.bridges.io.parser;

import de.karstenkoehler.bridges.io.parser.token.Token;
import de.karstenkoehler.bridges.io.parser.token.Tokenizer;

abstract class AbstractTokenParser {
    private final Tokenizer tokenizer;
    Token current;

    AbstractTokenParser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    void consume(Token.Type type) throws ParseException {
        check(type);
        this.current = this.tokenizer.next();
    }

    void check(Token.Type type) throws ParseException {
        if (this.token().getType() != type) {
            throw new ParseException(String.format("expected: %s, found: %s", type, current.getType()));
        }
    }

    Token token() throws ParseException {
        if (this.current == null) {
            this.current = this.tokenizer.next();
        }
        return this.current;
    }
}
