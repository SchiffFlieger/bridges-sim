package de.karstenkoehler.bridges.io;

import de.karstenkoehler.bridges.model.Edge;
import de.karstenkoehler.bridges.model.Node;

public class DefaultValidator implements ResultValidator {

    private final int MIN_FIELD_DIMENSION = 4;
    private final int MAX_FIELD_DIMENSION = 25;

    private final int MIN_BRIDGES_PER_ISLAND = 1;
    private final int MAX_BRIDGES_PER_ISLAND = 8;

    private final int MIN_COORDINATE = 0;
    private final int MIN_ISLAND_ID = 0;


    @Override
    public void validate(ParseResult result) throws ValidateException {
        checkFieldSize(result);
        checkRequiredBridgesCount(result);
        checkIslandsAreOnField(result);
        checkBridgeReferencesToIslands(result);
        // TODO PB-63
        // TODO PB-65
    }

    private void checkFieldSize(ParseResult result) throws ValidateException {
        if (result.getWidth() < MIN_FIELD_DIMENSION || result.getWidth() > MAX_FIELD_DIMENSION) {
            throw new ValidateException(String.format("field width is %d. should be in range %d to %d.",
                    result.getWidth(), MIN_FIELD_DIMENSION, MAX_FIELD_DIMENSION));
        }
        if (result.getHeight() < MIN_FIELD_DIMENSION || result.getHeight() > MAX_FIELD_DIMENSION) {
            throw new ValidateException(String.format("field height is %d. should be in range %d to %d.",
                    result.getHeight(), MIN_FIELD_DIMENSION, MAX_FIELD_DIMENSION));
        }
    }

    private void checkRequiredBridgesCount(ParseResult result) throws ValidateException {
        for (Node island : result.getIslands().values()) {
            if (island.getRequiredBridges() < MIN_BRIDGES_PER_ISLAND || island.getRequiredBridges() > MAX_BRIDGES_PER_ISLAND) {
                throw new ValidateException(String.format("island at position (%d, %d) requires %d bridges. should be in range %d to %d.",
                        island.getX(), island.getY(), island.getRequiredBridges(), MIN_BRIDGES_PER_ISLAND, MAX_BRIDGES_PER_ISLAND));
            }
        }
    }

    private void checkIslandsAreOnField(ParseResult result) throws ValidateException {
        for (Node island : result.getIslands().values()) {
            if (island.getX() < MIN_COORDINATE || island.getX() > result.getWidth() - 1) {
                throw new ValidateException(String.format("island at position (%d, %d) is off the field.",
                        island.getX(), island.getY()));
            }
            if (island.getY() < MIN_COORDINATE || island.getY() > result.getHeight() - 1) {
                throw new ValidateException(String.format("island at position (%d, %d) is off the field.",
                        island.getX(), island.getY()));
            }
        }
    }

    private void checkBridgeReferencesToIslands(ParseResult result) throws ValidateException {
        final int MAX_ISLAND_ID = result.getIslands().size() - 1;
        for (Edge bridge : result.getBridges()) {
            if (bridge.getNode1() < MIN_ISLAND_ID || bridge.getNode1() > MAX_ISLAND_ID) {
                throw new ValidateException(String.format("bridge %d connects to non-existing island %d.",
                        bridge.getId(), bridge.getNode1()));
            }
            if (bridge.getNode2() < MIN_ISLAND_ID || bridge.getNode2() > MAX_ISLAND_ID) {
                throw new ValidateException(String.format("bridge %d connects to non-existing island %d.",
                        bridge.getId(), bridge.getNode2()));
            }
        }
    }
}
