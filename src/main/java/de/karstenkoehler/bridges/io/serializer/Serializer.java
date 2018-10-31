package de.karstenkoehler.bridges.io.serializer;

import de.karstenkoehler.bridges.io.parser.Parser;
import de.karstenkoehler.bridges.io.parser.token.Tokenizer;
import de.karstenkoehler.bridges.model.BridgesPuzzle;

/**
 * A serializer serializes a {@link BridgesPuzzle} as string. It is the counterpart to the {@link Tokenizer}
 * and {@link Parser} interfaces.
 */
public interface Serializer {
    /**
     * Serializes the given puzzle as string.
     *
     * @param puzzle the puzzle to serialize
     * @return the string representation of the puzzle
     */
    String serialize(BridgesPuzzle puzzle);
}
