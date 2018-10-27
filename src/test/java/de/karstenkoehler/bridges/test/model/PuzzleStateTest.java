package de.karstenkoehler.bridges.test.model;

import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Island;
import de.karstenkoehler.bridges.model.PuzzleState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class PuzzleStateTest {
    private static final int SIZE = 5;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Map<Integer, Island> bsp_5x5 = new HashMap<>();
        bsp_5x5.put(0, new Island(0, 0, 0, 3));
        bsp_5x5.put(1, new Island(1, 0, 2, 4));
        bsp_5x5.put(2, new Island(2, 0, 4, 2));
        bsp_5x5.put(3, new Island(3, 2, 0, 3));
        bsp_5x5.put(4, new Island(4, 2, 3, 2));
        bsp_5x5.put(5, new Island(5, 3, 2, 1));
        bsp_5x5.put(6, new Island(6, 3, 4, 1));
        bsp_5x5.put(7, new Island(7, 4, 0, 3));
        bsp_5x5.put(8, new Island(8, 4, 3, 3));

        // bridges required to solve the puzzle
        Bridge bridge1 = new Bridge(bsp_5x5.get(0), bsp_5x5.get(1), 2);
        Bridge bridge2 = new Bridge(bsp_5x5.get(0), bsp_5x5.get(3), 1);
        Bridge bridge3 = new Bridge(bsp_5x5.get(1), bsp_5x5.get(2), 1);
        Bridge bridge4 = new Bridge(bsp_5x5.get(1), bsp_5x5.get(5), 1);
        Bridge bridge5 = new Bridge(bsp_5x5.get(2), bsp_5x5.get(6), 1);
        Bridge bridge6 = new Bridge(bsp_5x5.get(3), bsp_5x5.get(7), 2);
        Bridge bridge7 = new Bridge(bsp_5x5.get(4), bsp_5x5.get(8), 2);
        Bridge bridge8 = new Bridge(bsp_5x5.get(7), bsp_5x5.get(8), 1);

        // other bridges
        Bridge bridge5b = new Bridge(bsp_5x5.get(2), bsp_5x5.get(6), 2);
        Bridge bridge8b = new Bridge(bsp_5x5.get(7), bsp_5x5.get(8), 2);
        Bridge bridge9 = new Bridge(bsp_5x5.get(3), bsp_5x5.get(4), 1);
        Bridge bridge10 = new Bridge(bsp_5x5.get(5), bsp_5x5.get(6), 1);

        Map<Integer, Island> bsp_isolation_3 = new HashMap<>();
        bsp_isolation_3.put(0, new Island(0, 0, 0, 1));
        bsp_isolation_3.put(1, new Island(1, 0, 3, 1));
        bsp_isolation_3.put(2, new Island(2, 3, 0, 2));
        bsp_isolation_3.put(3, new Island(3, 3, 3, 2));

        // bridges required to solve the puzzle
        Bridge isoBridge1 = new Bridge(bsp_isolation_3.get(0), bsp_isolation_3.get(2), 1);
        Bridge isoBridge2 = new Bridge(bsp_isolation_3.get(1), bsp_isolation_3.get(3), 1);
        Bridge isoBridge3 = new Bridge(bsp_isolation_3.get(2), bsp_isolation_3.get(3), 1);

        // other bridges
        Bridge isoBridge3b = new Bridge(bsp_isolation_3.get(2), bsp_isolation_3.get(3), 2);
        Bridge isoBridge4 = new Bridge(bsp_isolation_3.get(0), bsp_isolation_3.get(1), 1);


        return Arrays.asList(new Object[][]{
                // not solved
                {new BridgesPuzzle(bsp_5x5, new ArrayList<>(), SIZE, SIZE), PuzzleState.NOT_SOLVED},
                {new BridgesPuzzle(bsp_5x5, new ArrayList<>(Arrays.asList(bridge1, bridge2, bridge3)), SIZE, SIZE), PuzzleState.NOT_SOLVED},
                {new BridgesPuzzle(bsp_5x5, new ArrayList<>(Arrays.asList(bridge4, bridge5, bridge6)), SIZE, SIZE), PuzzleState.NOT_SOLVED},
                {new BridgesPuzzle(bsp_5x5, new ArrayList<>(Arrays.asList(bridge4, bridge5, bridge6)), SIZE, SIZE), PuzzleState.NOT_SOLVED},
                {new BridgesPuzzle(bsp_isolation_3, new ArrayList<>(Arrays.asList(isoBridge1, isoBridge2)), SIZE, SIZE), PuzzleState.NOT_SOLVED},
                {new BridgesPuzzle(bsp_isolation_3, new ArrayList<>(Arrays.asList(isoBridge2, isoBridge3)), SIZE, SIZE), PuzzleState.NOT_SOLVED},
                {new BridgesPuzzle(bsp_isolation_3, new ArrayList<>(Collections.singletonList(isoBridge4)), SIZE, SIZE), PuzzleState.NOT_SOLVED},

                // solved
                {new BridgesPuzzle(bsp_5x5, new ArrayList<>(Arrays.asList(bridge1, bridge2, bridge3, bridge4, bridge5, bridge6, bridge7, bridge8)), SIZE, SIZE), PuzzleState.SOLVED},
                {new BridgesPuzzle(bsp_isolation_3, new ArrayList<>(Arrays.asList(isoBridge1, isoBridge2, isoBridge3)), SIZE, SIZE), PuzzleState.SOLVED},

                // island has too many bridges
                {new BridgesPuzzle(bsp_5x5, new ArrayList<>(Collections.singletonList(bridge5b)), SIZE, SIZE), PuzzleState.ERROR},
                {new BridgesPuzzle(bsp_5x5, new ArrayList<>(Arrays.asList(bridge8b, bridge6, bridge7)), SIZE, SIZE), PuzzleState.ERROR},
                {new BridgesPuzzle(bsp_5x5, new ArrayList<>(Arrays.asList(bridge2, bridge6, bridge9)), SIZE, SIZE), PuzzleState.ERROR},
                {new BridgesPuzzle(bsp_isolation_3, new ArrayList<>(Arrays.asList(isoBridge1, isoBridge4)), SIZE, SIZE), PuzzleState.ERROR},

                // bridges are crossing
                {new BridgesPuzzle(bsp_5x5, new ArrayList<>(Arrays.asList(bridge7, bridge10)), SIZE, SIZE), PuzzleState.ERROR},
                {new BridgesPuzzle(bsp_5x5, new ArrayList<>(Arrays.asList(bridge4, bridge9)), SIZE, SIZE), PuzzleState.ERROR},

                // solved, but not connected
                {new BridgesPuzzle(bsp_isolation_3, new ArrayList<>(Arrays.asList(isoBridge4, isoBridge3b)), SIZE, SIZE), PuzzleState.NO_LONGER_SOLVABLE},

        });
    }

    @Parameterized.Parameter
    public BridgesPuzzle input;

    @Parameterized.Parameter(1)
    public PuzzleState expectedState;

    @Test
    public void testFillBridges() {
        input.fillMissingBridges();
        assertEquals(expectedState, input.getState());
    }
}
