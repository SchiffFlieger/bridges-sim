package de.karstenkoehler.bridges.test.model;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Island;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class FillBridgesTest {
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

        Map<Integer, Island> bsp_6x6 = new HashMap<>();
        bsp_6x6.put(0, new Island(0, 0, 0, 1));
        bsp_6x6.put(1, new Island(1, 0, 2, 4));
        bsp_6x6.put(2, new Island(2, 0, 5, 3));
        bsp_6x6.put(3, new Island(3, 2, 0, 4));
        bsp_6x6.put(4, new Island(4, 2, 2, 7));
        bsp_6x6.put(5, new Island(5, 2, 4, 3));
        bsp_6x6.put(6, new Island(6, 3, 1, 2));
        bsp_6x6.put(7, new Island(7, 3, 3, 2));
        bsp_6x6.put(8, new Island(8, 3, 5, 3));
        bsp_6x6.put(9, new Island(9, 4, 0, 2));
        bsp_6x6.put(10, new Island(10, 4, 2, 1));
        bsp_6x6.put(11, new Island(11, 4, 4, 1));
        bsp_6x6.put(12, new Island(12, 5, 1, 3));
        bsp_6x6.put(13, new Island(13, 5, 3, 5));
        bsp_6x6.put(14, new Island(14, 5, 5, 3));


        return Arrays.asList(new Object[][]{
                {new BridgesPuzzle(bsp_5x5, new ArrayList<>(), 5, 5), 10},
                {new BridgesPuzzle(bsp_6x6, new ArrayList<>(), 6, 6), 19},
        });
    }

    @Parameterized.Parameter
    public BridgesPuzzle input;

    @Parameterized.Parameter(1)
    public int bridgeCount;

    @Test
    public void testFillBridges() {
        this.input.fillMissingBridges();

        assertEquals(bridgeCount, input.getBridges().size());
    }
}
