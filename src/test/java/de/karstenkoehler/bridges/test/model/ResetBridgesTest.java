package de.karstenkoehler.bridges.test.model;

import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Island;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ResetBridgesTest {
    @Test
    public void test() {
        Map<Integer, Island> islands = new HashMap<>();
        islands.put(0, new Island(0, 0, 0, 1));
        islands.put(1, new Island(1, 0, 2, 4));
        islands.put(2, new Island(2, 0, 5, 3));
        islands.put(3, new Island(3, 2, 0, 4));
        islands.put(4, new Island(4, 2, 2, 7));
        islands.put(5, new Island(5, 2, 4, 3));
        islands.put(6, new Island(6, 3, 1, 2));
        islands.put(7, new Island(7, 3, 3, 2));
        islands.put(8, new Island(8, 3, 5, 3));
        islands.put(9, new Island(9, 4, 0, 2));
        islands.put(10, new Island(10, 4, 2, 1));
        islands.put(11, new Island(11, 4, 4, 1));
        islands.put(12, new Island(12, 5, 1, 3));
        islands.put(13, new Island(13, 5, 3, 5));
        islands.put(14, new Island(14, 5, 5, 3));

        List<Bridge> bridges = Arrays.asList(
                // vertical
                new Bridge(0, islands.get(0), islands.get(1), 0),
                new Bridge(1, islands.get(1), islands.get(2), 1),
                new Bridge(2, islands.get(3), islands.get(4), 2),
                new Bridge(3, islands.get(4), islands.get(5), 0),
                new Bridge(4, islands.get(6), islands.get(7), 0),
                new Bridge(5, islands.get(7), islands.get(8), 0),
                new Bridge(6, islands.get(9), islands.get(10), 0),
                new Bridge(7, islands.get(10), islands.get(11), 0),
                new Bridge(8, islands.get(12), islands.get(13), 0),
                new Bridge(9, islands.get(13), islands.get(14), 1),

                // horizontal
                new Bridge(10, islands.get(0), islands.get(3), 1),
                new Bridge(11, islands.get(3), islands.get(9), 0),
                new Bridge(12, islands.get(6), islands.get(12), 0),
                new Bridge(13, islands.get(1), islands.get(4), 0),
                new Bridge(14, islands.get(4), islands.get(10), 1),
                new Bridge(15, islands.get(7), islands.get(13), 2),
                new Bridge(16, islands.get(5), islands.get(11), 1),
                new Bridge(17, islands.get(2), islands.get(8), 0),
                new Bridge(18, islands.get(8), islands.get(14), 0)
        );

        BridgesPuzzle puzzle = new BridgesPuzzle(islands, bridges, 6, 6);
        puzzle.fillMissingBridges();
        puzzle.restart();

        for (int i = 0; i < islands.size(); i++) {
            Island island = islands.get(i);
            assertEquals(island.getRequiredBridges(), puzzle.getRemainingBridgeCount(island));
        }
    }
}
