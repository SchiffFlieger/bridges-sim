package de.karstenkoehler.bridges.model.generator;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.PuzzleSpecification;

/**
 * A generator is responsible for creating random {@link BridgesPuzzle}s. The generated puzzle must always have
 * at least one valid solution. Implementations of this interface should be reusable to create multiple puzzles.
 */
public interface Generator {
    /**
     * Generates a new random {@link BridgesPuzzle} from the given specification.
     *
     * @param spec the specification for the generated puzzle
     * @return the generated puzzle
     */
    BridgesPuzzle generate(PuzzleSpecification spec);
}
