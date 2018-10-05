package de.karstenkoehler.bridges.io;

import de.karstenkoehler.bridges.io.validators.Validator;
import de.karstenkoehler.bridges.model.Node;

public class DefaultValidator implements Validator {

    @Override
    public void validate(ParseResult result) throws ValidateException {
        checkIslandsHaveUniqueFields(result);
        checkIslandsOnAdjacentFields(result);
        // TODO PB-66
        // TODO PB-67
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
}
