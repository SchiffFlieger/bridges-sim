package de.karstenkoehler.bridges.test.model;

import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Island;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class MarkCrossingBridgesTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
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

        // all valid
        Bridge a1 = new Bridge(bsp_6x6.get(0), bsp_6x6.get(1), 1);
        Bridge a2 = new Bridge(bsp_6x6.get(3), bsp_6x6.get(4), 1);
        Bridge b1 = new Bridge(bsp_6x6.get(0), bsp_6x6.get(3), 1);
        Bridge b2 = new Bridge(bsp_6x6.get(1), bsp_6x6.get(4), 1);
        Bridge c1 = new Bridge(bsp_6x6.get(0), bsp_6x6.get(3), 1);
        Bridge c2 = new Bridge(bsp_6x6.get(0), bsp_6x6.get(1), 1);

        // partially invalid
        Bridge d1 = new Bridge(bsp_6x6.get(9), bsp_6x6.get(10), 1);
        Bridge d2 = new Bridge(bsp_6x6.get(6), bsp_6x6.get(12), 1);
        Bridge d3 = new Bridge(bsp_6x6.get(0), bsp_6x6.get(1), 1);

        Bridge e1 = new Bridge(bsp_6x6.get(4), bsp_6x6.get(10), 1);
        Bridge e2 = new Bridge(bsp_6x6.get(6), bsp_6x6.get(7), 1);
        Bridge e3 = new Bridge(bsp_6x6.get(9), bsp_6x6.get(10), 1);

        Bridge f1 = new Bridge(bsp_6x6.get(5), bsp_6x6.get(11), 1);
        Bridge f2 = new Bridge(bsp_6x6.get(7), bsp_6x6.get(8), 1);

        // all valid
        Bridge g1 = new Bridge(bsp_6x6.get(9), bsp_6x6.get(10), 0);
        Bridge g2 = new Bridge(bsp_6x6.get(6), bsp_6x6.get(12), 1);
        Bridge g3 = new Bridge(bsp_6x6.get(0), bsp_6x6.get(1), 1);

        Bridge h1 = new Bridge(bsp_6x6.get(4), bsp_6x6.get(10), 1);
        Bridge h2 = new Bridge(bsp_6x6.get(6), bsp_6x6.get(7), 0);
        Bridge h3 = new Bridge(bsp_6x6.get(9), bsp_6x6.get(10), 1);

        Bridge i1 = new Bridge(bsp_6x6.get(5), bsp_6x6.get(11), 0);
        Bridge i2 = new Bridge(bsp_6x6.get(7), bsp_6x6.get(8), 1);

        return Arrays.asList(new Object[][]{
                {new BridgesPuzzle(bsp_6x6, Arrays.asList(a1, a2), 6, 6), Arrays.asList(a1, a2), Collections.emptyList()},
                {new BridgesPuzzle(bsp_6x6, Arrays.asList(b1, b2), 6, 6), Arrays.asList(b1, b2), Collections.emptyList()},
                {new BridgesPuzzle(bsp_6x6, Arrays.asList(c1, c2), 6, 6), Arrays.asList(c1, c2), Collections.emptyList()},

                {new BridgesPuzzle(bsp_6x6, Arrays.asList(d1, d2, d3), 6, 6), Collections.singletonList(d3), Arrays.asList(d1, d2)},
                {new BridgesPuzzle(bsp_6x6, Arrays.asList(e1, e2, e3), 6, 6), Collections.singletonList(e3), Arrays.asList(e1, e2)},
                {new BridgesPuzzle(bsp_6x6, Arrays.asList(f1, f2), 6, 6), Collections.emptyList(), Arrays.asList(f1, f2)},

                {new BridgesPuzzle(bsp_6x6, Arrays.asList(g1, g2, g3), 6, 6), Arrays.asList(g1, g2, g3), Collections.emptyList()},
                {new BridgesPuzzle(bsp_6x6, Arrays.asList(h1, h2, h3), 6, 6), Arrays.asList(h1, h2, h3), Collections.emptyList()},
                {new BridgesPuzzle(bsp_6x6, Arrays.asList(i1, i2), 6, 6), Arrays.asList(i1, i2), Collections.emptyList()},
        });
    }

    @Parameterized.Parameter
    public BridgesPuzzle input;

    @Parameterized.Parameter(1)
    public List<Bridge> valid;

    @Parameterized.Parameter(2)
    public List<Bridge> invalid;

    @Test
    public void testFillBridges() {
        this.input.markInvalidBridges();

        for (Bridge bridge : valid) {
            assertTrue(bridge.isValid());
        }
        for (Bridge bridge : invalid) {
            assertFalse(bridge.isValid());
        }
    }
}
