package de.karstenkoehler.bridges.io.validator;

import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;

/**
 * Checks if the island references of every bridge is sorted correctly. The lesser
 * island id must be first.
 */
public class BridgeReferenceOrderValidator implements Validator {

    @Override
    public void validate(BridgesPuzzle puzzle) throws ValidateException {
        for (Bridge bridge : puzzle.getBridges()) {
            if (bridge.getStartIsland().getId() > bridge.getEndIsland().getId()) {
                throw new ValidateException("a bridge has its island references not sorted correctly");
            }
        }
    }
}
