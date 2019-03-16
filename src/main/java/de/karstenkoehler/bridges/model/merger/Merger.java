package de.karstenkoehler.bridges.model.merger;

import de.karstenkoehler.bridges.model.BridgesPuzzle;

public interface Merger {
    BridgesPuzzle merge(BridgesPuzzle left, BridgesPuzzle right);
}
