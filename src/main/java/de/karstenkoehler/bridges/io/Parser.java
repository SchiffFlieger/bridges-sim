package de.karstenkoehler.bridges.io;

public interface Parser {
    ParseResult parse(final String input) throws ParseException;
}
