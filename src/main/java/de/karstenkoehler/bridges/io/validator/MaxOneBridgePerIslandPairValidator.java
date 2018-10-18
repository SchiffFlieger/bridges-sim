package de.karstenkoehler.bridges.io.validator;

import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;

/**
 * Checks if there is only one edge connecting the same two nodes. Double
 * bridges can be set in the corresponding edge.
 */
public class MaxOneBridgePerIslandPairValidator implements Validator {
    @Override
    public void validate(BridgesPuzzle puzzle) throws ValidateException {
        for (Bridge bridge : puzzle.getBridges()) {
            for (Bridge other : puzzle.getBridges()) {
                if (bridge != other && (sameDirection(bridge, other) || oppositeDirection(bridge, other))) {
                    throw new ValidateException(String.format("bridges %d and %d connect the same islands.",
                            bridge.getId(), other.getId()));
                }
            }
        }
    }

    private boolean sameDirection(Bridge bridge, Bridge other) {
        return bridge.getStartIsland() == other.getStartIsland() && bridge.getEndIsland() == other.getEndIsland();
    }

    private boolean oppositeDirection(Bridge bridge, Bridge other) {
        return bridge.getStartIsland() == other.getEndIsland() && bridge.getEndIsland() == other.getStartIsland();
    }
}