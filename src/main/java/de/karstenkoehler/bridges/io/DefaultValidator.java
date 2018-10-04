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
        checkIslandsHaveUniqueFields(result);
        checkIslandsOnAdjacentFields(result);
        checkBridgesNotDiagonal(result);
        checkBridgesConnectDifferentIslands(result);
        // TODO PB-66
        // TODO PB-67
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

    private void checkIslandsHaveUniqueFields(ParseResult result) throws ValidateException {
        for (Node island : result.getIslands().values()) {
            for (Node other : result.getIslands().values()) {
                if (island.getId() != other.getId() && island.getX() == other.getX() && island.getY() == other.getY()) {
                    throw new ValidateException(String.format("there are two islands on field (%d, %d).",
                            island.getX(), island.getY()));
                }
            }
        }
    }

    private void checkIslandsOnAdjacentFields(ParseResult result) throws ValidateException {
        for (Node island : result.getIslands().values()) {
            for (Node other : result.getIslands().values()) {
                if (island != other) {
                    if (island.getX() == other.getX() && Math.abs(island.getY() - other.getY()) < 2) {
                        throw new ValidateException(String.format("island (%d, %d) is adjacent to island (%d, %d). there must be at least one free space in between.",
                                island.getX(), island.getY(), other.getX(), other.getY()));
                    }
                }
            }
        }
    }

    private void checkBridgesNotDiagonal(ParseResult result) throws ValidateException {
        for (Edge bridge : result.getBridges()) {
            Node a = result.getIslands().get(bridge.getNode1());
            Node b = result.getIslands().get(bridge.getNode2());
            if (a.getX() != b.getX() && a.getY() != b.getY()) {
                throw new ValidateException(String.format("bridge %d is neither horizontal or vertical.", bridge.getId()));
            }
        }
    }

    private void checkBridgesConnectDifferentIslands(ParseResult result) throws ValidateException {
        for (Edge bridge : result.getBridges()) {
            if (bridge.getNode1() == bridge.getNode2()) {
                throw new ValidateException(String.format("bridge %d connects to the same island twice.", bridge.getId()));
            }
        }
    }
}
