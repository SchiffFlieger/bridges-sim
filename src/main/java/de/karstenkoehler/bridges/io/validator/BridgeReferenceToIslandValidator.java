package de.karstenkoehler.bridges.io.validator;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Bridge;

/**
 * Checks if the island references of every defined bridge are valid. For the island reference to be valid
 * it needs to be in the range [0, island count - 1].
 */
public class BridgeReferenceToIslandValidator implements Validator {
    private static final int MIN_ISLAND_ID = 0;

    @Override
    public void validate(BridgesPuzzle puzzle) throws ValidateException {
        final int MAX_ISLAND_ID = puzzle.getIslands().size() - 1;
        for (Bridge bridge : puzzle.getBridges()) {
            checkIslandsExist(bridge.getStartIsland(), bridge.getId(), MAX_ISLAND_ID);
            checkIslandsExist(bridge.getEndIsland(), bridge.getId(), MAX_ISLAND_ID);
        }
    }

    private void checkIslandsExist(int value, int bridgeId, int maxIslands) throws ValidateException {
        if (value < MIN_ISLAND_ID || value > maxIslands) {
            throw new ValidateException(String.format("bridge %d connects to non-existing island %d.",
                    bridgeId, value));
        }
    }
}
