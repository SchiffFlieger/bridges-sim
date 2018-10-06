package de.karstenkoehler.bridges.io.parser;

import de.karstenkoehler.bridges.io.ParseResult;

public interface Parser {
    ParseResult parse() throws ParseException;
}
