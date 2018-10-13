package de.karstenkoehler.bridges.io.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.model.Edge;

/**
 * Checks if bridges are sorted correctly. They must first be sorted by node1 and then by node2.
 */
public class BridgeOrderValidator implements Validator {

    @Override
    public void validate(ParseResult result) throws ValidateException {
        int prevN1 = -1;
        int prevN2 = -1;

        for (Edge bridge : result.getBridges()) {
            if (bridge.getNode1() < prevN1) {
                throw new ValidateException(String.format("bridge sort order not valid. check bridge %d.", bridge.getId()));
            } else if (bridge.getNode1() > prevN1) {
                prevN1 = bridge.getNode1();
                prevN2 = -1;
            }

            if (bridge.getNode2() < prevN2) {
                throw new ValidateException(String.format("bridge sort order not valid. check bridge %d.", bridge.getId()));
            } else {
                prevN2 = bridge.getNode2();
            }
        }
    }
}
