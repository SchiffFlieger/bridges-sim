package de.karstenkoehler.bridges.io.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.model.Edge;

/**
 * Checks if every defined bridge connects two different islands. Two islands are different if
 * they do not share the same id.
 */
public class BridgesConnectDifferentIslandsValidator implements Validator {
    @Override
    public void validate(ParseResult result) throws ValidateException {
        for (Edge bridge : result.getBridges()) {
            if (bridge.getNode1() == bridge.getNode2()) {
                throw new ValidateException(String.format("bridge %d connects to the same island twice.", bridge.getId()));
            }
        }
    }
}
