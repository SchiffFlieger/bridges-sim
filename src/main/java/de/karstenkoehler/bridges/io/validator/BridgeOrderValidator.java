package de.karstenkoehler.bridges.io.validator;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;

/**
 * Checks if bridges are sorted correctly. They must first be sorted by node1 and then by node2.
 */
public class BridgeOrderValidator implements Validator {

    @Override
    public void validate(BridgesPuzzle puzzle) throws ValidateException {
        int prevN1 = -1;
        int prevN2 = -1;

        for (Connection bridge : puzzle.getConnections()) {
            if (bridge.getStartIsland().getId() < prevN1) {
                throw new ValidateException("The bridge sort order is not valid.");
            } else if (bridge.getStartIsland().getId() > prevN1) {
                prevN1 = bridge.getStartIsland().getId();
                prevN2 = -1;
            }

            if (bridge.getEndIsland().getId() < prevN2) {
                throw new ValidateException("The bridge sort order is not valid.");
            } else {
                prevN2 = bridge.getEndIsland().getId();
            }
        }
    }
}
