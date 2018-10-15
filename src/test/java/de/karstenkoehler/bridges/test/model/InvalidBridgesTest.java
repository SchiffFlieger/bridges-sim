package de.karstenkoehler.bridges.test.model;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.model.Edge;
import de.karstenkoehler.bridges.model.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class InvalidBridgesTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Map<Integer, Node> bsp_6x6 = new HashMap<>();
        bsp_6x6.put(0, new Node(0, 0, 0, 1));
        bsp_6x6.put(1, new Node(1, 0, 2, 4));
        bsp_6x6.put(2, new Node(2, 0, 5, 3));
        bsp_6x6.put(3, new Node(3, 2, 0, 4));
        bsp_6x6.put(4, new Node(4, 2, 2, 7));
        bsp_6x6.put(5, new Node(5, 2, 4, 3));
        bsp_6x6.put(6, new Node(6, 3, 1, 2));
        bsp_6x6.put(7, new Node(7, 3, 3, 2));
        bsp_6x6.put(8, new Node(8, 3, 5, 3));
        bsp_6x6.put(9, new Node(9, 4, 0, 2));
        bsp_6x6.put(10, new Node(10, 4, 2, 1));
        bsp_6x6.put(11, new Node(11, 4, 4, 1));
        bsp_6x6.put(12, new Node(12, 5, 1, 3));
        bsp_6x6.put(13, new Node(13, 5, 3, 5));
        bsp_6x6.put(14, new Node(14, 5, 5, 3));

        // all valid
        Edge a1 = new Edge(0, 0, 1, 1);
        Edge a2 = new Edge(1, 3, 4, 1);
        Edge b1 = new Edge(0, 0, 3, 1);
        Edge b2 = new Edge(1, 1, 4, 1);
        Edge c1 = new Edge(0, 0, 3, 1);
        Edge c2 = new Edge(1, 0, 1, 1);

        // partially invalid
        Edge d1 = new Edge(0, 9, 10, 1);
        Edge d2 = new Edge(1, 6, 12, 1);
        Edge d3 = new Edge(2, 0, 1, 1);

        Edge e1 = new Edge(0, 4, 10, 1);
        Edge e2 = new Edge(1, 6, 7, 1);
        Edge e3 = new Edge(2, 9, 10, 1);

        Edge f1 = new Edge(0, 5, 11, 1);
        Edge f2 = new Edge(1, 7, 8, 1);

        // all valid
        Edge g1 = new Edge(0, 9, 10, 0);
        Edge g2 = new Edge(1, 6, 12, 1);
        Edge g3 = new Edge(2, 0, 1, 1);

        Edge h1 = new Edge(0, 4, 10, 1);
        Edge h2 = new Edge(1, 6, 7, 0);
        Edge h3 = new Edge(2, 9, 10, 1);

        Edge i1 = new Edge(0, 5, 11, 0);
        Edge i2 = new Edge(1, 7, 8, 1);

        return Arrays.asList(new Object[][]{
                {new ParseResult(bsp_6x6, Arrays.asList(a1, a2), 6, 6), Arrays.asList(a1, a2), Collections.emptyList()},
                {new ParseResult(bsp_6x6, Arrays.asList(b1, b2), 6, 6), Arrays.asList(b1, b2), Collections.emptyList()},
                {new ParseResult(bsp_6x6, Arrays.asList(c1, c2), 6, 6), Arrays.asList(c1, c2), Collections.emptyList()},

                {new ParseResult(bsp_6x6, Arrays.asList(d1, d2, d3), 6, 6), Collections.singletonList(d3), Arrays.asList(d1, d2)},
                {new ParseResult(bsp_6x6, Arrays.asList(e1, e2, e3), 6, 6), Collections.singletonList(e3), Arrays.asList(e1, e2)},
                {new ParseResult(bsp_6x6, Arrays.asList(f1, f2), 6, 6), Collections.emptyList(), Arrays.asList(f1, f2)},

                {new ParseResult(bsp_6x6, Arrays.asList(g1, g2, g3), 6, 6), Arrays.asList(g1, g2, g3), Collections.emptyList()},
                {new ParseResult(bsp_6x6, Arrays.asList(h1, h2, h3), 6, 6), Arrays.asList(h1, h2, h3), Collections.emptyList()},
                {new ParseResult(bsp_6x6, Arrays.asList(i1, i2), 6, 6), Arrays.asList(i1, i2), Collections.emptyList()},
        });
    }

    @Parameterized.Parameter
    public ParseResult input;

    @Parameterized.Parameter(1)
    public List<Edge> valid;

    @Parameterized.Parameter(2)
    public List<Edge> invalid;

    @Test
    public void testFillBridges() {
        this.input.markInvalidBridges();

        for (Edge bridge : valid) {
            assertTrue(bridge.isValid());
        }
        for (Edge bridge : invalid) {
            assertFalse(bridge.isValid());
        }
    }
}
