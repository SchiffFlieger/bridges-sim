package de.karstenkoehler.bridges.io.parser;

import de.karstenkoehler.bridges.model.BridgesPuzzle;

public interface Parser {
    BridgesPuzzle parse() throws ParseException;
}
