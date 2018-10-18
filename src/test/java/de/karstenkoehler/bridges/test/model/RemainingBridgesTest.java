package de.karstenkoehler.bridges.test.model;

import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Island;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class RemainingBridgesTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Island island0 = new Island(0, 0, 0, 1);
        Island island1 = new Island(1, 0, 2, 4);
        Island island2 = new Island(2, 0, 5, 3);
        Island island3 = new Island(3, 2, 0, 4);
        Island island4 = new Island(4, 2, 2, 7);
        Island island5 = new Island(5, 2, 4, 3);
        Island island6 = new Island(6, 3, 1, 2);
        Island island7 = new Island(7, 3, 3, 2);
        Island island8 = new Island(8, 3, 5, 3);
        Island island9 = new Island(9, 4, 0, 2);
        Island island10 = new Island(10, 4, 2, 1);
        Island island11 = new Island(11, 4, 4, 1);
        Island island12 = new Island(12, 5, 1, 3);
        Island island13 = new Island(13, 5, 3, 5);
        Island island14 = new Island(14, 5, 5, 3);

        Map<Integer, Island> bsp_6x6 = new HashMap<>();
        bsp_6x6.put(0, island0);
        bsp_6x6.put(1, island1);
        bsp_6x6.put(2, island2);
        bsp_6x6.put(3, island3);
        bsp_6x6.put(4, island4);
        bsp_6x6.put(5, island5);
        bsp_6x6.put(6, island6);
        bsp_6x6.put(7, island7);
        bsp_6x6.put(8, island8);
        bsp_6x6.put(9, island9);
        bsp_6x6.put(10, island10);
        bsp_6x6.put(11, island11);
        bsp_6x6.put(12, island12);
        bsp_6x6.put(13, island13);
        bsp_6x6.put(14, island14);

        List<Bridge> bridges = Arrays.asList(
                // vertical
                new Bridge(0, bsp_6x6.get(0), bsp_6x6.get(1), 0),
                new Bridge(1, bsp_6x6.get(1), bsp_6x6.get(2), 1),
                new Bridge(2, bsp_6x6.get(3), bsp_6x6.get(4), 2),
                new Bridge(3, bsp_6x6.get(4), bsp_6x6.get(5), 0),
                new Bridge(4, bsp_6x6.get(6), bsp_6x6.get(7), 0),
                new Bridge(5, bsp_6x6.get(7), bsp_6x6.get(8), 0),
                new Bridge(6, bsp_6x6.get(9), bsp_6x6.get(10), 0),
                new Bridge(7, bsp_6x6.get(10), bsp_6x6.get(11), 0),
                new Bridge(8, bsp_6x6.get(12), bsp_6x6.get(13), 0),
                new Bridge(9, bsp_6x6.get(13), bsp_6x6.get(14), 1),

                // horizontal
                new Bridge(10, bsp_6x6.get(0), bsp_6x6.get(3), 1),
                new Bridge(11, bsp_6x6.get(3), bsp_6x6.get(9), 0),
                new Bridge(12, bsp_6x6.get(6), bsp_6x6.get(12), 0),
                new Bridge(13, bsp_6x6.get(1), bsp_6x6.get(4), 0),
                new Bridge(14, bsp_6x6.get(4), bsp_6x6.get(10), 1),
                new Bridge(15, bsp_6x6.get(7), bsp_6x6.get(13), 2),
                new Bridge(16, bsp_6x6.get(5), bsp_6x6.get(11), 1),
                new Bridge(17, bsp_6x6.get(2), bsp_6x6.get(8), 0),
                new Bridge(18, bsp_6x6.get(8), bsp_6x6.get(14), 0)
        );

        BridgesPuzzle puzzle = new BridgesPuzzle(bsp_6x6, bridges, 6, 6);

        return Arrays.asList(new Object[][]{
                {puzzle, island0, 0},
                {puzzle, island1, 3},
                {puzzle, island2, 2},
                {puzzle, island3, 1},
                {puzzle, island4, 4},
                {puzzle, island5, 2},
                {puzzle, island6, 2},
                {puzzle, island7, 0},
                {puzzle, island8, 3},
                {puzzle, island9, 2},
                {puzzle, island10, 0},
                {puzzle, island11, 0},
                {puzzle, island12, 3},
                {puzzle, island13, 2},
                {puzzle, island14, 2},
        });
    }

    @Parameterized.Parameter
    public BridgesPuzzle input;

    @Parameterized.Parameter(1)
    public Island island;

    @Parameterized.Parameter(2)
    public int remaining;

    @Test
    public void testFillBridges() {
        this.input.fillMissingBridges();
        assertEquals(remaining, input.getRemainingBridgeCount(island));
    }
}
