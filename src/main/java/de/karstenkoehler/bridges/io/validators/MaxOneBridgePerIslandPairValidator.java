package de.karstenkoehler.bridges.io.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.model.Edge;

/**
 * Checks if there is only one edge connecting the same two nodes. Double
 * bridges can be set in the corresponding edge.
 */
public class MaxOneBridgePerIslandPairValidator implements Validator {
    @Override
    public void validate(ParseResult result) throws ValidateException {
        for (Edge bridge : result.getBridges()) {
            for (Edge other : result.getBridges()) {
                if (bridge != other && (sameDirection(bridge, other) || oppositeDirection(bridge, other))) {
                    throw new ValidateException(String.format("bridges %d and %d connect the same islands.",
                            bridge.getId(), other.getId()));
                }
            }
        }
    }

    private boolean sameDirection(Edge bridge, Edge other) {
        return bridge.getNode1() == other.getNode1() && bridge.getNode2() == other.getNode2();
    }

    private boolean oppositeDirection(Edge bridge, Edge other) {
        return bridge.getNode1() == other.getNode2() && bridge.getNode2() == other.getNode1();
    }
}
