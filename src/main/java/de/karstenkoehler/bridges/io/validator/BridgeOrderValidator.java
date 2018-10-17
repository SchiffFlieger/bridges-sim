package de.karstenkoehler.bridges.io.validator;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Bridge;

/**
 * Checks if bridges are sorted correctly. They must first be sorted by node1 and then by node2.
 */
public class BridgeOrderValidator implements Validator {

    @Override
    public void validate(BridgesPuzzle puzzle) throws ValidateException {
        int prevN1 = -1;
        int prevN2 = -1;

        for (Bridge bridge : puzzle.getBridges()) {
            if (bridge.getStartIsland() < prevN1) {
                throw new ValidateException(String.format("bridge sort order not valid. check bridge %d.", bridge.getId()));
            } else if (bridge.getStartIsland() > prevN1) {
                prevN1 = bridge.getStartIsland();
                prevN2 = -1;
            }

            if (bridge.getEndIsland() < prevN2) {
                throw new ValidateException(String.format("bridge sort order not valid. check bridge %d.", bridge.getId()));
            } else {
                prevN2 = bridge.getEndIsland();
            }
        }
    }
}
