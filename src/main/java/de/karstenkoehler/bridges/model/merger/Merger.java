package de.karstenkoehler.bridges.model.merger;

import de.karstenkoehler.bridges.model.BridgesPuzzle;

/**
 * A merger merges two puzzles to a single one. Existing bridges should be adopted in the new
 * puzzle. An island's position may change to create a valid bridge between the puzzles.
 */
public interface Merger {
    /**
     * Merges two puzzles to a single new puzzle.
     *
     * @param left  the puzzle resulting on the left side
     * @param right the puzzle resulting on the right side
     * @return a new puzzle created from the given puzzles
     */
    BridgesPuzzle merge(BridgesPuzzle left, BridgesPuzzle right);
}