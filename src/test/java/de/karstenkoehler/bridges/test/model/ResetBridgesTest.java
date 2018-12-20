package de.karstenkoehler.bridges.test.model;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.Island;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ResetBridgesTest {
    @Test
    public void test() {
        List<Island> islands = Arrays.asList(
                new Island(0, 0, 0, 1),
                new Island(1, 0, 2, 4),
                new Island(2, 0, 5, 3),
                new Island(3, 2, 0, 4),
                new Island(4, 2, 2, 7),
                new Island(5, 2, 4, 3),
                new Island(6, 3, 1, 2),
                new Island(7, 3, 3, 2),
                new Island(8, 3, 5, 3),
                new Island(9, 4, 0, 2),
                new Island(10, 4, 2, 1),
                new Island(11, 4, 4, 1),
                new Island(12, 5, 1, 3),
                new Island(13, 5, 3, 5),
                new Island(14, 5, 5, 3)
        );

        List<Connection> bridges = Arrays.asList(
                // vertical
                new Connection(islands.get(0), islands.get(1), 0),
                new Connection(islands.get(1), islands.get(2), 1),
                new Connection(islands.get(3), islands.get(4), 2),
                new Connection(islands.get(4), islands.get(5), 0),
                new Connection(islands.get(6), islands.get(7), 0),
                new Connection(islands.get(7), islands.get(8), 0),
                new Connection(islands.get(9), islands.get(10), 0),
                new Connection(islands.get(10), islands.get(11), 0),
                new Connection(islands.get(12), islands.get(13), 0),
                new Connection(islands.get(13), islands.get(14), 1),

                // horizontal
                new Connection(islands.get(0), islands.get(3), 1),
                new Connection(islands.get(3), islands.get(9), 0),
                new Connection(islands.get(6), islands.get(12), 0),
                new Connection(islands.get(1), islands.get(4), 0),
                new Connection(islands.get(4), islands.get(10), 1),
                new Connection(islands.get(7), islands.get(13), 2),
                new Connection(islands.get(5), islands.get(11), 1),
                new Connection(islands.get(2), islands.get(8), 0),
                new Connection(islands.get(8), islands.get(14), 0)
        );

        BridgesPuzzle puzzle = new BridgesPuzzle(islands, bridges, 6, 6);
        puzzle.fillMissingConnections();
        puzzle.restart();

        for (Island island : islands) {
            assertEquals(island.getRequiredBridges(), puzzle.getRemainingBridgeCount(island));
        }
    }
}
