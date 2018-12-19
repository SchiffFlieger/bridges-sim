package de.karstenkoehler.bridges.test.solver;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.Island;
import de.karstenkoehler.bridges.model.solver.Solver;
import de.karstenkoehler.bridges.model.solver.SolverImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class SolverIsolationTest {
    private static Solver solver = new SolverImpl();

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Island isolation1island1 = new Island(0, 2, 0, 1);
        Island isolation1island2 = new Island(1, 2, 4, 1);

        Island isolation2island1 = new Island(0, 2, 0, 2);
        Island isolation2island2 = new Island(1, 2, 4, 2);

        Connection bspIsolation2nextSave = new Connection(isolation2island1, isolation2island2, 1);
        List<Connection> bspIsolation2Connections = new ArrayList<>(Collections.singletonList(bspIsolation2nextSave));

        return Arrays.asList(new Object[][]{
                {new BridgesPuzzle(Arrays.asList(isolation1island1, isolation1island2), new ArrayList<>(), 5, 5), isolation1island1, isolation1island2, 1},
                {new BridgesPuzzle(Arrays.asList(isolation2island1, isolation2island2), bspIsolation2Connections, 5, 5), isolation2island1, isolation2island2, 2},
        });
    }

    @Parameterized.Parameter
    public BridgesPuzzle input;

    @Parameterized.Parameter(1)
    public Island start;

    @Parameterized.Parameter(2)
    public Island end;

    @Parameterized.Parameter(3)
    public int bridgeCount;

    @Test
    public void testFillBridges() {
        this.input.fillMissingBridges();
        Connection next = solver.nextSafeBridge(this.input);
        next.setBridgeCount(next.getBridgeCount() + 1);

        assertEquals(next.getStartIsland(), start);
        assertEquals(next.getEndIsland(), end);
        assertEquals(next.getBridgeCount(), bridgeCount);
    }
}
