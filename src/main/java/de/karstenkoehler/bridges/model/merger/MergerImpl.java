package de.karstenkoehler.bridges.model.merger;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.Island;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergerImpl implements Merger {
    @Override
    public BridgesPuzzle merge(BridgesPuzzle left, BridgesPuzzle right) {
        int width = left.getWidth() + right.getWidth() + 1;
        int height = left.getHeight() + right.getHeight() - 1;

        //  ii) linkes Rätsel übernehmen
        List<Island> islands = new ArrayList<>();
        List<Connection> connections = new ArrayList<>();

        // y offset berechnen
        Pair<Island, Island> pair = getMergeIslands(left, right);
        int yOffset = pair.getKey().getY() - pair.getValue().getY();
        pair.getKey().setRequiredBridges(pair.getKey().getRequiredBridges() + 1);
        pair.getValue().setRequiredBridges(pair.getValue().getRequiredBridges() + 1);

        if (yOffset > 0) {
            // rechtes Rätsel hochschieben
            addIslandsAndConnections(islands, left.getIslands(), 0, 0);
            addIslandsAndConnections(islands, right.getIslands(), left.getWidth() + 1, yOffset);
        } else if (yOffset < 0) {
            // linkes Rätsel hochschieben
            addIslandsAndConnections(islands, left.getIslands(), 0, -yOffset);
            addIslandsAndConnections(islands, right.getIslands(), left.getWidth() + 1, 0);
        } else {
            // gar nichts hochschieben
            addIslandsAndConnections(islands, left.getIslands(), 0, 0);
            addIslandsAndConnections(islands, right.getIslands(), left.getWidth() + 1, 0);
        }

        BridgesPuzzle puzzle = new BridgesPuzzle(islands, connections, width, height);
        puzzle.fillMissingConnections();
        return puzzle;
    }

    private void addIslandsAndConnections(List<Island> islands, List<Island> islandsToAdd, int xOffset, int yOffset) {
        int idOffset = islands.size();

        for (int i = 0; i < islandsToAdd.size(); i++) {
            int id = idOffset + i + 1;
            int x = islandsToAdd.get(i).getX() + xOffset;
            int y = islandsToAdd.get(i).getY() + yOffset;

            Island newIsland = new Island(id, x, y, islandsToAdd.get(i).getRequiredBridges());
            islands.add(newIsland);
        }
    }

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
