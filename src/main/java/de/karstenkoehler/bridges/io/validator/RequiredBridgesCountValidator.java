package de.karstenkoehler.bridges.io.validator;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Island;

/**
 * Checks if the number of bridges required ist within the valid range.
 * Valid are all integers from 1 to 8.
 */
public class RequiredBridgesCountValidator implements Validator {
    private static final int MIN_BRIDGES_PER_ISLAND = 1;
    private static final int MAX_BRIDGES_PER_ISLAND = 8;

    @Override
    public void validate(BridgesPuzzle puzzle) throws ValidateException {
        for (Island island : puzzle.getIslands()) {
            if (requiredBridgesOutOfRange(island)) {
                throw new ValidateException(String.format("island at position (%d, %d) requires %d bridges. should be in range %d to %d.",
                        island.getX(), island.getY(), island.getRequiredBridges(), MIN_BRIDGES_PER_ISLAND, MAX_BRIDGES_PER_ISLAND));
            }
        }
    }

    private boolean requiredBridgesOutOfRange(Island island) {
        return island.getRequiredBridges() < MIN_BRIDGES_PER_ISLAND || island.getRequiredBridges() > MAX_BRIDGES_PER_ISLAND;
    }
}
