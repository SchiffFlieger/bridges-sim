package de.karstenkoehler.bridges.test.serializer;

import de.karstenkoehler.bridges.io.serializer.Serializer;
import de.karstenkoehler.bridges.io.serializer.SerializerImpl;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.Island;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class StringSerializationTest {

    private static final String withBridges = "FIELD\r\n5 x 5 | 9\r\n\r\nISLANDS\r\n( 0, 0 | 3 )\r\n( 0, 2 | 4 )\r\n( 0, 4 | 2 )\r\n( 2, 0 | 3 )\r\n( 2, 3 | 2 )\r\n( 3, 2 | 1 )\r\n( 3, 4 | 1 )\r\n( 4, 0 | 3 )\r\n( 4, 3 | 3 )\r\n\r\nBRIDGES\r\n( 0, 1 | true )\r\n( 0, 3 | false )\r\n( 1, 2 | false )\r\n( 1, 5 | false )\r\n( 2, 6 | false )\r\n( 3, 7 | true )\r\n( 4, 8 | true )\r\n( 7, 8 | false )\r\n";
    private static final String withoutBridges = "FIELD\r\n5 x 5 | 9\r\n\r\nISLANDS\r\n( 0, 0 | 3 )\r\n( 0, 2 | 4 )\r\n( 0, 4 | 2 )\r\n( 2, 0 | 3 )\r\n( 2, 3 | 2 )\r\n( 3, 2 | 1 )\r\n( 3, 4 | 1 )\r\n( 4, 0 | 3 )\r\n( 4, 3 | 3 )\r\n";
    private static final String withEmptyBridges = "FIELD\r\n5 x 5 | 9\r\n\r\nISLANDS\r\n( 0, 0 | 3 )\r\n( 0, 2 | 4 )\r\n( 0, 4 | 2 )\r\n( 2, 0 | 3 )\r\n( 2, 3 | 2 )\r\n( 3, 2 | 1 )\r\n( 3, 4 | 1 )\r\n( 4, 0 | 3 )\r\n( 4, 3 | 3 )\r\n";
    private static final String withPartialBridges = "FIELD\r\n5 x 5 | 9\r\n\r\nISLANDS\r\n( 0, 0 | 3 )\r\n( 0, 2 | 4 )\r\n( 0, 4 | 2 )\r\n( 2, 0 | 3 )\r\n( 2, 3 | 2 )\r\n( 3, 2 | 1 )\r\n( 3, 4 | 1 )\r\n( 4, 0 | 3 )\r\n( 4, 3 | 3 )\r\n\r\nBRIDGES\r\n( 0, 1 | true )\r\n( 1, 2 | false )\r\n( 2, 6 | false )\r\n( 4, 8 | true )\r\n";

    @Parameterized.Parameters()
    public static Collection<Object[]> data() {
        List<Island> islands = Arrays.asList(
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

        Connection bridge1 = new Connection(islands.get(0), islands.get(1), 2);
        Connection bridge2 = new Connection(islands.get(0), islands.get(3), 1);
        Connection bridge3 = new Connection(islands.get(1), islands.get(2), 1);
        Connection bridge4 = new Connection(islands.get(1), islands.get(5), 1);
        Connection bridge5 = new Connection(islands.get(2), islands.get(6), 1);
        Connection bridge6 = new Connection(islands.get(3), islands.get(7), 2);
        Connection bridge7 = new Connection(islands.get(4), islands.get(8), 2);
        Connection bridge8 = new Connection(islands.get(7), islands.get(8), 1);

        Connection emptyBridge1 = new Connection(islands.get(0), islands.get(1), 0);
        Connection emptyBridge2 = new Connection(islands.get(0), islands.get(3), 0);
        Connection emptyBridge3 = new Connection(islands.get(1), islands.get(2), 0);
        Connection emptyBridge4 = new Connection(islands.get(1), islands.get(5), 0);
        Connection emptyBridge5 = new Connection(islands.get(2), islands.get(6), 0);
        Connection emptyBridge6 = new Connection(islands.get(3), islands.get(7), 0);
        Connection emptyBridge7 = new Connection(islands.get(4), islands.get(8), 0);
        Connection emptyBridge8 = new Connection(islands.get(7), islands.get(8), 0);


        return Arrays.asList(new Object[][]{
                {new BridgesPuzzle(islands, Arrays.asList(bridge1, bridge2, bridge3, bridge4, bridge5, bridge6, bridge7, bridge8), 5, 5), withBridges},
                {new BridgesPuzzle(islands, new ArrayList<>(), 5, 5), withoutBridges},
                {new BridgesPuzzle(islands, Arrays.asList(emptyBridge1, emptyBridge2, emptyBridge3, emptyBridge4, emptyBridge5, emptyBridge6, emptyBridge7, emptyBridge8), 5, 5), withEmptyBridges},
                {new BridgesPuzzle(islands, Arrays.asList(bridge1, emptyBridge2, bridge3, emptyBridge4, bridge5, emptyBridge6, bridge7, emptyBridge8), 5, 5), withPartialBridges},
        });
    }

    @Parameterized.Parameter
    public BridgesPuzzle input;

    @Parameterized.Parameter(1)
    public String expected;

    private static final Serializer serializer = new SerializerImpl();

    @Test
    public void test() {
        assertEquals(expected, serializer.serialize(input));
    }

}
