package de.karstenkoehler.bridges.test.model;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.model.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class FillBridgesTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Map<Integer, Node> bsp_5x5 = new HashMap<>();
        bsp_5x5.put(0, new Node(0, 0, 0, 3));
        bsp_5x5.put(1, new Node(1, 0, 2, 4));
        bsp_5x5.put(2, new Node(2, 0, 4, 2));
        bsp_5x5.put(3, new Node(3, 2, 0, 3));
        bsp_5x5.put(4, new Node(4, 2, 3, 2));
        bsp_5x5.put(5, new Node(5, 3, 2, 1));
        bsp_5x5.put(6, new Node(6, 3, 4, 1));
        bsp_5x5.put(7, new Node(7, 4, 0, 3));
        bsp_5x5.put(8, new Node(8, 4, 3, 3));

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


        return Arrays.asList(new Object[][]{
                {new ParseResult(bsp_5x5, new ArrayList<>(), 5, 5), 10},
                {new ParseResult(bsp_6x6, new ArrayList<>(), 6, 6), 19},
        });
    }

    @Parameterized.Parameter
    public ParseResult input;

    @Parameterized.Parameter(1)
    public int bridgeCount;

    @Test
    public void testFillBridges() {
        this.input.fillMissingBridges();

        assertEquals(bridgeCount, input.getBridges().size());
    }
}
