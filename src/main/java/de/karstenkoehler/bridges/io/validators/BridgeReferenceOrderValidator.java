package de.karstenkoehler.bridges.io.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.ValidateException;
import de.karstenkoehler.bridges.model.Edge;

/**
 * Checks if the island references of every bridge is sorted correctly. The lesser
 * island id must be first.
 */
public class BridgeReferenceOrderValidator implements Validator {

    @Override
    public void validate(ParseResult result) throws ValidateException {
        for (Edge bridge : result.getBridges()) {
            if (bridge.getNode1() > bridge.getNode2()) {
                throw new ValidateException("");
            }
        }
    }
}
