package de.karstenkoehler.bridges.test.model;

import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.Island;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class AddBridgeTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Island island1 = new Island(0, 0, 0, 3);
        Island island2 = new Island(1, 0, 2, 4);

        return Arrays.asList(new Object[][]{
                {new Connection(island1, island2, 0), 1, 1},
                {new Connection(island1, island2, 1), 1, 2},
                {new Connection(island1, island2, 2), -1, 1},
                {new Connection(island1, island2, 1), -1, 0},
        });
    }

    @Parameterized.Parameter
    public Connection input;

    @Parameterized.Parameter(1)
    public int toAdd;

    @Parameterized.Parameter(2)
    public int newCount;

    @Test
    public void testFillBridges() {
        while (toAdd != 0) {
            if (toAdd > 0) {
                input.addBridge();
                toAdd--;
            } else {
                input.removeBridge();
                toAdd++;
            }
        }

        assertEquals(newCount, input.getBridgeCount());
    }
}
