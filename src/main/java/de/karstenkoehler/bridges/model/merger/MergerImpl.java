package de.karstenkoehler.bridges.model.merger;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.Island;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This is an implementation of the {@link Merger} interface. The two given puzzles
 * are placed next to each other. Then, one of them is shifted downwards until two
 * islands of the puzzles line up for a horizontal bridge.
 */
public class MergerImpl implements Merger {

    /**
     * @see Merger#merge(BridgesPuzzle, BridgesPuzzle)
     */
    @Override
    public BridgesPuzzle merge(BridgesPuzzle left, BridgesPuzzle right) {
        int width = left.getWidth() + right.getWidth() + 1;
        int height = left.getHeight() + right.getHeight() - 1;

        List<Island> islands = new ArrayList<>();
        List<Connection> connections = new ArrayList<>();

        // choose an appropriate island as merge point
        Pair<Island, Island> pair = getMergeIslands(left, right);
        int yOffset = pair.getKey().getY() - pair.getValue().getY();
        pair.getKey().setRequiredBridges(pair.getKey().getRequiredBridges() + 1);
        pair.getValue().setRequiredBridges(pair.getValue().getRequiredBridges() + 1);

        if (yOffset > 0) {
            // apply vertical offset to right puzzle
            addIslandsAndConnections(islands, connections, left.getIslands(), left.getConnections(), 0, 0);
            addIslandsAndConnections(islands, connections, right.getIslands(), right.getConnections(), left.getWidth() + 1, yOffset);
        } else if (yOffset < 0) {
            // apply vertical offset to left puzzle
            addIslandsAndConnections(islands, connections, left.getIslands(), left.getConnections(), 0, -yOffset);
            addIslandsAndConnections(islands, connections, right.getIslands(), right.getConnections(), left.getWidth() + 1, 0);
        } else {
            // apply no vertical offset
            addIslandsAndConnections(islands, connections, left.getIslands(), left.getConnections(), 0, 0);
            addIslandsAndConnections(islands, connections, right.getIslands(), right.getConnections(), left.getWidth() + 1, 0);
        }

        BridgesPuzzle puzzle = new BridgesPuzzle(islands, connections, width, height);
        puzzle.fillMissingConnections();
        return puzzle;
    }

    /**
     * @param islands          the islands of the new puzzle
     * @param connections      the connections of the new puzzle
     * @param islandsToAdd     the islands to add to the new puzzle
     * @param connectionsToAdd the connections to add to the new puzzle
     * @param xOffset          the horizontal offset to apply to the islands
     * @param yOffset          the vertical offset to apply to the islands
     */
    private void addIslandsAndConnections(List<Island> islands, List<Connection> connections, List<Island> islandsToAdd, List<Connection> connectionsToAdd, int xOffset, int yOffset) {
        int idOffset = islands.size();

        // add new islands with offset
        for (int i = 0; i < islandsToAdd.size(); i++) {
            int id = idOffset + i;
            int x = islandsToAdd.get(i).getX() + xOffset;
            int y = islandsToAdd.get(i).getY() + yOffset;

            Island newIsland = new Island(id, x, y, islandsToAdd.get(i).getRequiredBridges());
            islands.add(newIsland);
        }

        // add existing bridges to new islands
        for (int i = 0; i < connectionsToAdd.size(); i++) {
            Connection bridge = connectionsToAdd.get(i);
            if (bridge.getBridgeCount() == 0) {
                continue;
            }

            int startId = bridge.getStartIsland().getId() + idOffset;
            Island start = islands.get(startId);

            int endId = bridge.getEndIsland().getId() + idOffset;
            Island end = islands.get(endId);

            Connection newBridge = new Connection(start, end, bridge.getBridgeCount());
            connections.add(newBridge);
        }
    }

    /**
     * Returns the rightmost island of the left puzzle and the leftmost island of the right puzzle.
     *
     * @param left  the puzzle on the left side
     * @param right the puzzle on the right side
     * @return a pair of the found islands
     */
    private Pair<Island, Island> getMergeIslands(BridgesPuzzle left, BridgesPuzzle right) {
        Island rightmostInLeftPuzzle = getRightmostInLeftPuzzle(left);
        Island leftmostInRightPuzzle = getLeftmostInRightPuzzle(right);

        return new Pair<>(rightmostInLeftPuzzle, leftmostInRightPuzzle);
    }

    private Island getRightmostInLeftPuzzle(BridgesPuzzle left) {
        int maxX = left.getIslands().stream().mapToInt(Island::getX).max().orElse(-1);
        return left.getIslands().stream().filter(island -> island.getX() == maxX).min(Comparator.comparingInt(Island::getY)).orElse(null);
    }

    private Island getLeftmostInRightPuzzle(BridgesPuzzle right) {
        int minX = right.getIslands().stream().mapToInt(Island::getX).min().orElse(-1);
        return right.getIslands().stream().filter(island -> island.getX() == minX).min(Comparator.comparingInt(Island::getY)).orElse(null);
    }
}
