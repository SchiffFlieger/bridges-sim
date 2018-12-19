package de.karstenkoehler.bridges.io.validator;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;

/**
 * Checks if there is only one edge connecting the same two nodes. Double
 * bridges can be set in the corresponding edge.
 */
public class MaxOneBridgePerIslandPairValidator implements Validator {
    /**
     * @see Validator#validate(BridgesPuzzle)
     */
    @Override
    public void validate(BridgesPuzzle puzzle) throws ValidateException {
        for (Connection bridge : puzzle.getConnections()) {
            for (Connection other : puzzle.getConnections()) {
                if (bridge != other && (sameDirection(bridge, other) || oppositeDirection(bridge, other))) {
                    throw new ValidateException("Two bridges connect the same island pair.");
                }
            }
        }
    }

    private boolean sameDirection(Connection bridge, Connection other) {
        return bridge.getStartIsland() == other.getStartIsland() && bridge.getEndIsland() == other.getEndIsland();
    }

    private boolean oppositeDirection(Connection bridge, Connection other) {
        return bridge.getStartIsland() == other.getEndIsland() && bridge.getEndIsland() == other.getStartIsland();
    }
}
