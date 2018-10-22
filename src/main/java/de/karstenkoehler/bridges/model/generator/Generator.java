package de.karstenkoehler.bridges.model.generator;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.PuzzleSpecification;

public interface Generator {
    BridgesPuzzle generate(PuzzleSpecification spec);
}
