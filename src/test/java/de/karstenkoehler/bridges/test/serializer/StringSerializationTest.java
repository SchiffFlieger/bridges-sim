package de.karstenkoehler.bridges.test.serializer;

import de.karstenkoehler.bridges.io.serializer.Serializer;
import de.karstenkoehler.bridges.io.serializer.SerializerImpl;
import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Island;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class StringSerializationTest {

    private static final String withBridges = "FIELD\r\n5 x 5 | 9\r\n\r\nISLANDS\r\n( 0, 0 | 3 )\r\n( 0, 2 | 4 )\r\n( 0, 4 | 2 )\r\n( 2, 0 | 3 )\r\n( 2, 3 | 2 )\r\n( 3, 2 | 1 )\r\n( 3, 4 | 1 )\r\n( 4, 0 | 3 )\r\n( 4, 3 | 3 )\r\n\r\nBRIDGES\r\n( 0, 1 | true )\r\n( 0, 3 | false )\r\n( 1, 2 | false )\r\n( 1, 5 | false )\r\n( 2, 6 | false )\r\n( 3, 7 | true )\r\n( 4, 8 | true )\r\n( 7, 8 | false )\r\n";
    private static final String withoutBridges = "FIELD\r\n5 x 5 | 9\r\n\r\nISLANDS\r\n( 0, 0 | 3 )\r\n( 0, 2 | 4 )\r\n( 0, 4 | 2 )\r\n( 2, 0 | 3 )\r\n( 2, 3 | 2 )\r\n( 3, 2 | 1 )\r\n( 3, 4 | 1 )\r\n( 4, 0 | 3 )\r\n( 4, 3 | 3 )\r\n";
    private static final String withEmptyBridges = "FIELD\r\n5 x 5 | 9\r\n\r\nISLANDS\r\n( 0, 0 | 3 )\r\n( 0, 2 | 4 )\r\n( 0, 4 | 2 )\r\n( 2, 0 | 3 )\r\n( 2, 3 | 2 )\r\n( 3, 2 | 1 )\r\n( 3, 4 | 1 )\r\n( 4, 0 | 3 )\r\n( 4, 3 | 3 )\r\n";

    @Parameterized.Parameters()
    public static Collection<Object[]> data() {
        Map<Integer, Island> islands = new HashMap<>();
        islands.put(0, new Island(0, 0, 0, 3));
        islands.put(1, new Island(1, 0, 2, 4));
        islands.put(2, new Island(2, 0, 4, 2));
        islands.put(3, new Island(3, 2, 0, 3));
        islands.put(4, new Island(4, 2, 3, 2));
        islands.put(5, new Island(5, 3, 2, 1));
        islands.put(6, new Island(6, 3, 4, 1));
        islands.put(7, new Island(7, 4, 0, 3));
        islands.put(8, new Island(8, 4, 3, 3));

        Bridge bridge1 = new Bridge(0, islands.get(0), islands.get(1), 2);
        Bridge bridge2 = new Bridge(1, islands.get(0), islands.get(3), 1);
        Bridge bridge3 = new Bridge(2, islands.get(1), islands.get(2), 1);
        Bridge bridge4 = new Bridge(3, islands.get(1), islands.get(5), 1);
        Bridge bridge5 = new Bridge(4, islands.get(2), islands.get(6), 1);
        Bridge bridge6 = new Bridge(5, islands.get(3), islands.get(7), 2);
        Bridge bridge7 = new Bridge(6, islands.get(4), islands.get(8), 2);
        Bridge bridge8 = new Bridge(7, islands.get(7), islands.get(8), 1);

        Bridge emptyBridge1 = new Bridge(0, islands.get(0), islands.get(1), 0);
        Bridge emptyBridge2 = new Bridge(1, islands.get(0), islands.get(3), 0);
        Bridge emptyBridge3 = new Bridge(2, islands.get(1), islands.get(2), 0);
        Bridge emptyBridge4 = new Bridge(3, islands.get(1), islands.get(5), 0);
        Bridge emptyBridge5 = new Bridge(4, islands.get(2), islands.get(6), 0);
        Bridge emptyBridge6 = new Bridge(5, islands.get(3), islands.get(7), 0);
        Bridge emptyBridge7 = new Bridge(6, islands.get(4), islands.get(8), 0);
        Bridge emptyBridge8 = new Bridge(7, islands.get(7), islands.get(8), 0);


        return Arrays.asList(new Object[][]{
                {new BridgesPuzzle(islands, Arrays.asList(bridge1, bridge2, bridge3, bridge4, bridge5, bridge6, bridge7, bridge8), 5, 5), withBridges},
                {new BridgesPuzzle(islands, new ArrayList<>(), 5, 5), withoutBridges},
                {new BridgesPuzzle(islands, Arrays.asList(emptyBridge1, emptyBridge2, emptyBridge3, emptyBridge4, emptyBridge5, emptyBridge6, emptyBridge7, emptyBridge8), 5, 5), withEmptyBridges},
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
