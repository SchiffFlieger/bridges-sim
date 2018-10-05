package de.karstenkoehler.bridges.io.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.ValidateException;
import de.karstenkoehler.bridges.model.Node;

public class NoIslandsOnAdjacentFieldsValidator implements Validator {
    @Override
    public void validate(ParseResult result) throws ValidateException {
        for (Node island : result.getIslands().values()) {
            for (Node other : result.getIslands().values()) {
                if (island != other && (adjacentOnX(island, other) || adjacentOnY(island, other))) {
                    throw new ValidateException(String.format("island (%d, %d) is adjacent to island (%d, %d). there must be at least one free space in between.",
                            island.getX(), island.getY(), other.getX(), other.getY()));
                }
            }
        }
    }

    private boolean adjacentOnX(Node island, Node other) {
        return island.getX() == other.getX() && Math.abs(island.getY() - other.getY()) < 2;
    }

    private boolean adjacentOnY(Node island, Node other) {
        return island.getY() == other.getY() && Math.abs(island.getX() - other.getX()) < 2;
    }
}
