package de.karstenkoehler.bridges.io.validator;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;

/**
 * Checks if the island references of every bridge is sorted correctly. The lesser
 * island id must be first.
 */
public class BridgeReferenceOrderValidator implements Validator {

    @Override
    public void validate(BridgesPuzzle puzzle) throws ValidateException {
        for (Connection bridge : puzzle.getConnections()) {
            if (bridge.getStartIsland().getId() > bridge.getEndIsland().getId()) {
                throw new ValidateException("A bridge has its island references not sorted correctly.");
            }
        }
    }
}
