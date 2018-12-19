package de.karstenkoehler.bridges.test.solver;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.Island;
import de.karstenkoehler.bridges.model.solver.Solver;
import de.karstenkoehler.bridges.model.solver.SolverImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class SolverEasyTest {
    private static Solver solver = new SolverImpl();

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
                {new BridgesPuzzle(bsp_5x5, new ArrayList<>(), 5, 5), 11},
                {new BridgesPuzzle(bsp_6x6, new ArrayList<>(), 6, 6), 22},
        });
    }

    @Parameterized.Parameter
    public BridgesPuzzle input;

    @Parameterized.Parameter(1)
    public int expectedBridges;

    @Test
    public void testFillBridges() {
        this.input.fillMissingBridges();
        int bridgesBuilt = 0;

        Connection next;
        do {
            next = solver.nextSafeBridge(this.input);

            if (next != null) {
                next.setBridgeCount(next.getBridgeCount() + 1);
                bridgesBuilt++;
            }
        } while (next != null);

        this.input.markInvalidBridges();
        assertEquals(expectedBridges, bridgesBuilt);
        assertTrue(input.getConnections().stream().allMatch(Connection::isValid));
    }
}
