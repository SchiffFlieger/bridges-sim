package de.karstenkoehler.bridges.io.validator;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Bridge;

/**
 * Checks if every defined bridge connects two different islands. Two islands are different if
 * they do not share the same id.
 */
public class BridgesConnectDifferentIslandsValidator implements Validator {
    @Override
    public void validate(BridgesPuzzle puzzle) throws ValidateException {
        for (Bridge bridge : puzzle.getBridges()) {
            if (bridge.getStartIsland() == bridge.getEndIsland()) {
                throw new ValidateException(String.format("bridge %d connects to the same island twice.", bridge.getId()));
            }
        }
    }
}