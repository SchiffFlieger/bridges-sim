package de.karstenkoehler.bridges.test.model;

import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Island;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class FillBridgesTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        List<Island> bsp_5x5 = Arrays.asList(
                new Island(0, 0, 0, 3),
                new Island(1, 0, 2, 4),
                new Island(2, 0, 4, 2),
                new Island(3, 2, 0, 3),
                new Island(4, 2, 3, 2),
                new Island(5, 3, 2, 1),
                new Island(6, 3, 4, 1),
                new Island(7, 4, 0, 3),
                new Island(8, 4, 3, 3)
        );

        List<Island> bsp_6x6 = Arrays.asList(
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

        int prevN1 = -1;
        int prevN2 = -1;
        for (Bridge bridge : input.getBridges()) {
            assertFalse(bridge.getStartIsland().getId() < prevN1);
            if (bridge.getStartIsland().getId() > prevN1) {
                prevN1 = bridge.getStartIsland().getId();
                prevN2 = -1;
            }

            assertFalse(bridge.getEndIsland().getId() < prevN2);
            if (bridge.getEndIsland().getId() >= prevN2) {
                prevN2 = bridge.getEndIsland().getId();
            }
        }
    }
}
