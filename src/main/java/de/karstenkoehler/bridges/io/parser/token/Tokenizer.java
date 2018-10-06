package de.karstenkoehler.bridges.io.parser.token;

import de.karstenkoehler.bridges.io.parser.ParseException;

public interface Tokenizer {
    Token next() throws ParseException;
}
