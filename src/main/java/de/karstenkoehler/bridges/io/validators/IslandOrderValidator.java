package de.karstenkoehler.bridges.io.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.model.Node;

/**
 * Checks if islands are sorted correctly. They must first be sorted by x coordinate and then by y coordinate.
 */
public class IslandOrderValidator implements Validator {

    @Override
    public void validate(ParseResult result) throws ValidateException {
        int prevX = -1;
        int prevY = -1;

        for (int i = 0; i < result.getIslands().size(); i++) {
            Node island = result.getIslands().get(i);

            if (island.getX() < prevX) {
                throw new ValidateException(String.format("island sort order not valid. check island %d.", island.getId()));
            } else if (island.getX() > prevX) {
                prevX = island.getX();
                prevY = -1;
            }

            if (island.getY() < prevY) {
                throw new ValidateException(String.format("island sort order not valid. check island %d.", island.getId()));
            } else {
                prevY = island.getY();
            }
        }
    }
}
