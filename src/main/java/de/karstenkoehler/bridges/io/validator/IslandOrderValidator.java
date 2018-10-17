package de.karstenkoehler.bridges.io.validator;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Island;

/**
 * Checks if islands are sorted correctly. They must first be sorted by x coordinate and then by y coordinate.
 */
public class IslandOrderValidator implements Validator {

    @Override
    public void validate(BridgesPuzzle puzzle) throws ValidateException {
        int prevX = -1;
        int prevY = -1;

        for (int i = 0; i < puzzle.getIslands().size(); i++) {
            Island island = puzzle.getIslands().get(i);

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
