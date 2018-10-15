package de.karstenkoehler.bridges.test.model;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.model.Edge;
import de.karstenkoehler.bridges.model.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class RemainingBridgesTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Node island0 = new Node(0, 0, 0, 1);
        Node island1 = new Node(1, 0, 2, 4);
        Node island2 = new Node(2, 0, 5, 3);
        Node island3 = new Node(3, 2, 0, 4);
        Node island4 = new Node(4, 2, 2, 7);
        Node island5 = new Node(5, 2, 4, 3);
        Node island6 = new Node(6, 3, 1, 2);
        Node island7 = new Node(7, 3, 3, 2);
        Node island8 = new Node(8, 3, 5, 3);
        Node island9 = new Node(9, 4, 0, 2);
        Node island10 = new Node(10, 4, 2, 1);
        Node island11 = new Node(11, 4, 4, 1);
        Node island12 = new Node(12, 5, 1, 3);
        Node island13 = new Node(13, 5, 3, 5);
        Node island14 = new Node(14, 5, 5, 3);

        Map<Integer, Node> bsp_6x6 = new HashMap<>();
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

        List<Edge> bridges = Arrays.asList(
                // vertical
                new Edge(0, 0, 1, 0),
                new Edge(1, 1, 2, 1),
                new Edge(2, 3, 4, 2),
                new Edge(3, 4, 5, 0),
                new Edge(4, 6, 7, 0),
                new Edge(5, 7, 8, 0),
                new Edge(6, 9, 10, 0),
                new Edge(7, 10, 11, 0),
                new Edge(8, 12, 13, 0),
                new Edge(9, 13, 14, 1),

                // horizontal
                new Edge(10, 0, 3, 1),
                new Edge(11, 3, 9, 0),
                new Edge(12, 6, 12, 0),
                new Edge(13, 1, 4, 0),
                new Edge(14, 4, 10, 1),
                new Edge(15, 7, 13, 2),
                new Edge(16, 5, 11, 1),
                new Edge(17, 2, 8, 0),
                new Edge(18, 8, 14, 0)
        );

        ParseResult result = new ParseResult(bsp_6x6, bridges, 6, 6);

        return Arrays.asList(new Object[][]{
                {result, island0, 0},
                {result, island1, 3},
                {result, island2, 2},
                {result, island3, 1},
                {result, island4, 4},
                {result, island5, 2},
                {result, island6, 2},
                {result, island7, 0},
                {result, island8, 3},
                {result, island9, 2},
                {result, island10, 0},
                {result, island11, 0},
                {result, island12, 3},
                {result, island13, 2},
                {result, island14, 2},
        });
    }

    @Parameterized.Parameter
    public ParseResult input;

    @Parameterized.Parameter(1)
    public Node island;

    @Parameterized.Parameter(2)
    public int remaining;

    @Test
    public void testFillBridges() {
        this.input.fillMissingBridges();
        assertEquals(remaining, input.getRemainingBridgeCount(island));
    }
}
