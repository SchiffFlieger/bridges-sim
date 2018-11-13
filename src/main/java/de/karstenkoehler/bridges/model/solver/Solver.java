package de.karstenkoehler.bridges.model.solver;

import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;

public interface Solver {
    Bridge nextSafeBridge(BridgesPuzzle puzzle);
}
