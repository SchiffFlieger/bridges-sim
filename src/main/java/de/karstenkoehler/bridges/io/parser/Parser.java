package de.karstenkoehler.bridges.io.parser;

import de.karstenkoehler.bridges.model.BridgesPuzzle;

/**
 * A parser reads some kind of input and creates a {@link BridgesPuzzle} from the obtained information. The kind
 * of input depends on the specific implementation of the parser.
 */
public interface Parser {
    /**
     * Creates a {@link BridgesPuzzle} from some kind of input information.
     *
     * @return the created puzzle
     * @throws ParseException if the input information was insufficient or contained some kind of error
     */
    BridgesPuzzle parse() throws ParseException;
}
