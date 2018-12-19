package de.karstenkoehler.bridges.model.solver;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;

/**
 * A solver can calculate a safe bridge for a given {@link BridgesPuzzle}. The puzzle has to be in state
 * <code>not solved</code>, otherwise no safe bridges can be calculated. Implementations of this interface
 * should be reusable to solve multiple puzzles.
 */
public interface Solver {
    /**
     * Calculates a safe bridge for the given puzzle. Safe means that the bridges count can be incremented
     * without any issues occurring during this or future steps. The caller of this method is responsible
     * for incrementing the bridge count. Returns <code>null</code> if there are no more safe bridges in
     * the puzzle.
     *
     * @param puzzle the puzzle to solve
     * @return a bridge that's count can safely be incremented
     */
    Connection nextSafeBridge(BridgesPuzzle puzzle);
}
