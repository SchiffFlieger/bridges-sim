package de.karstenkoehler.bridges.io.serializer;

import de.karstenkoehler.bridges.model.BridgesPuzzle;

public interface Serializer {
    String serialize(BridgesPuzzle puzzle);
}
