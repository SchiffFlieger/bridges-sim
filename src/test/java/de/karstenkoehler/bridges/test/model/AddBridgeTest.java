package de.karstenkoehler.bridges.test.model;

import de.karstenkoehler.bridges.InvalidBridgeCountException;
import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.Island;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
                {new Bridge(island1, island2, 0), 1, 1, null},
                {new Bridge(island1, island2, 1), 1, 2, null},
                {new Bridge(island1, island2, 2), -1, 1, null},
                {new Bridge(island1, island2, 1), -1, 0, null},

                {new Bridge(island1, island2, 1), 0, 0, IllegalArgumentException.class},
                {new Bridge(island1, island2, 1), 2, 0, IllegalArgumentException.class},
                {new Bridge(island1, island2, 1), -2, 0, IllegalArgumentException.class},

                {new Bridge(island1, island2, 0), -1, 0, InvalidBridgeCountException.class},
                {new Bridge(island1, island2, 2), 1, 0, InvalidBridgeCountException.class},
        });
    }

    @Parameterized.Parameter
    public Bridge input;

    @Parameterized.Parameter(1)
    public int toAdd;

    @Parameterized.Parameter(2)
    public int newCount;

    @Parameterized.Parameter(3)
    public Class<? extends Exception> expectedException;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testFillBridges() throws InvalidBridgeCountException {
        if (expectedException != null) {
            thrown.expect(expectedException);
        }

        input.addBridges(toAdd);

        assertEquals(newCount, input.getBridgeCount());
    }
}
