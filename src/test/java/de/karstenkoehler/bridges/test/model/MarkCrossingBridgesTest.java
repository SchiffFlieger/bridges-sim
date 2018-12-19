package de.karstenkoehler.bridges.test.model;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.Island;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class MarkCrossingBridgesTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
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

        // all valid
        Connection a1 = new Connection(bsp_6x6.get(0), bsp_6x6.get(1), 1);
        Connection a2 = new Connection(bsp_6x6.get(3), bsp_6x6.get(4), 1);
        Connection b1 = new Connection(bsp_6x6.get(0), bsp_6x6.get(3), 1);
        Connection b2 = new Connection(bsp_6x6.get(1), bsp_6x6.get(4), 1);
        Connection c1 = new Connection(bsp_6x6.get(0), bsp_6x6.get(3), 1);
        Connection c2 = new Connection(bsp_6x6.get(0), bsp_6x6.get(1), 1);

        // partially invalid
        Connection d1 = new Connection(bsp_6x6.get(9), bsp_6x6.get(10), 1);
        Connection d2 = new Connection(bsp_6x6.get(6), bsp_6x6.get(12), 1);
        Connection d3 = new Connection(bsp_6x6.get(0), bsp_6x6.get(1), 1);

        Connection e1 = new Connection(bsp_6x6.get(4), bsp_6x6.get(10), 1);
        Connection e2 = new Connection(bsp_6x6.get(6), bsp_6x6.get(7), 1);
        Connection e3 = new Connection(bsp_6x6.get(9), bsp_6x6.get(10), 1);

        Connection f1 = new Connection(bsp_6x6.get(5), bsp_6x6.get(11), 1);
        Connection f2 = new Connection(bsp_6x6.get(7), bsp_6x6.get(8), 1);

        // all valid
        Connection g1 = new Connection(bsp_6x6.get(9), bsp_6x6.get(10), 0);
        Connection g2 = new Connection(bsp_6x6.get(6), bsp_6x6.get(12), 1);
        Connection g3 = new Connection(bsp_6x6.get(0), bsp_6x6.get(1), 1);

        Connection h1 = new Connection(bsp_6x6.get(4), bsp_6x6.get(10), 1);
        Connection h2 = new Connection(bsp_6x6.get(6), bsp_6x6.get(7), 0);
        Connection h3 = new Connection(bsp_6x6.get(9), bsp_6x6.get(10), 1);

        Connection i1 = new Connection(bsp_6x6.get(5), bsp_6x6.get(11), 0);
        Connection i2 = new Connection(bsp_6x6.get(7), bsp_6x6.get(8), 1);

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
    public List<Connection> valid;

    @Parameterized.Parameter(2)
    public List<Connection> invalid;

    @Test
    public void testFillBridges() {
        this.input.markInvalidBridges();

        for (Connection bridge : valid) {
            assertTrue(bridge.isValid());
        }
        for (Connection bridge : invalid) {
            assertFalse(bridge.isValid());
        }
    }
}
