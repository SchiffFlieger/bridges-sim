package de.karstenkoehler.bridges.io.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.ValidateException;
import de.karstenkoehler.bridges.model.Edge;

public class BridgeReferenceToIslandValidator implements Validator {
    private static final int MIN_ISLAND_ID = 0;

    @Override
    public void validate(ParseResult result) throws ValidateException {
        final int MAX_ISLAND_ID = result.getIslands().size() - 1;
        for (Edge bridge : result.getBridges()) {
            checkIslandsExist(bridge.getNode1(), bridge.getId(), MAX_ISLAND_ID);
            checkIslandsExist(bridge.getNode2(), bridge.getId(), MAX_ISLAND_ID);
        }
    }

    private void checkIslandsExist(int value, int bridgeId, int maxIslands) throws ValidateException {
        if (value < MIN_ISLAND_ID || value > maxIslands) {
            throw new ValidateException(String.format("bridge %d connects to non-existing island %d.",
                    bridgeId, value));
        }
    }
}
