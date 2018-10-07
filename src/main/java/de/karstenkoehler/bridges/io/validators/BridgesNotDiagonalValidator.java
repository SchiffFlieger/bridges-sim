package de.karstenkoehler.bridges.io.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.ValidateException;
import de.karstenkoehler.bridges.model.Edge;
import de.karstenkoehler.bridges.model.Node;

/**
 * Checks if every defined bridge is either horizontal or vertical. This means the
 * two islands of the bridge must share either their x or y coordinate.
 */
public class BridgesNotDiagonalValidator implements Validator {
    @Override
    public void validate(ParseResult result) throws ValidateException {
        for (Edge bridge : result.getBridges()) {
            Node a = result.getIslands().get(bridge.getNode1());
            Node b = result.getIslands().get(bridge.getNode2());
            if (isDiagonal(a, b)) {
                throw new ValidateException(String.format("bridge %d is neither horizontal or vertical.", bridge.getId()));
            }
        }
    }

    private boolean isDiagonal(Node a, Node b) {
        return a.getX() != b.getX() && a.getY() != b.getY();
    }
}
