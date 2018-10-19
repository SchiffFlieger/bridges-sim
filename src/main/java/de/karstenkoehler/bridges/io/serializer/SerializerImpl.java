package de.karstenkoehler.bridges.io.serializer;

import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Island;

public class SerializerImpl implements Serializer {
    @Override
    public String serialize(BridgesPuzzle puzzle) {
        StringBuilder builder = new StringBuilder();

        addField(puzzle, builder);
        addIslands(puzzle, builder);

        if (!hasBridges(puzzle)) {
            builder.append(System.lineSeparator());
            addBridges(puzzle, builder);
        }

        return builder.toString();
    }

    private void addField(BridgesPuzzle puzzle, StringBuilder builder) {
        builder.append("FIELD").append(System.lineSeparator());
        builder.append(String.format("%d x %d | %d%n", puzzle.getWidth(), puzzle.getHeight(), puzzle.getIslands().size()));
        builder.append(System.lineSeparator());
    }

    private void addIslands(BridgesPuzzle puzzle, StringBuilder builder) {
        builder.append("ISLANDS").append(System.lineSeparator());
        for (Island island : puzzle.getIslands()) {
            builder.append(String.format("( %d, %d | %d )%n", island.getX(), island.getY(), island.getRequiredBridges()));
        }
    }

    private void addBridges(BridgesPuzzle puzzle, StringBuilder builder) {
        builder.append("BRIDGES").append(System.lineSeparator());
        for (Bridge bridge : puzzle.getBridges()) {
            if (bridge.getBridgeCount() > 0) {
                builder.append(String.format("( %d, %d | %s )%n", bridge.getStartIsland().getId(), bridge.getEndIsland().getId(), bridge.getBridgeCount() == 2 ? "true" : "false"));
            }
        }
    }

    private boolean hasBridges(BridgesPuzzle puzzle) {
        return puzzle.getBridges().stream().mapToInt(Bridge::getBridgeCount).sum() == 0;
    }

}
