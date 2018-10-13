package de.karstenkoehler.bridges.io.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.ValidateException;
import de.karstenkoehler.bridges.model.Node;

/**
 * Checks if all islands are on separate fields. Islands are on separate fields
 * if they do not share the same x and y coordinates.
 */
public class IslandOnUniqueFieldsValidator implements Validator {
    @Override
    public void validate(ParseResult result) throws ValidateException {
        for (Node island : result.getIslands()) {
            for (Node other : result.getIslands()) {
                if (islandsOnSameField(island, other)) {
                    throw new ValidateException(String.format("there are two islands on field (%d, %d).",
                            island.getX(), island.getY()));
                }
            }
        }
    }

    private boolean islandsOnSameField(Node island, Node other) {
        return island.getId() != other.getId() && island.getX() == other.getX() && island.getY() == other.getY();
    }
}
