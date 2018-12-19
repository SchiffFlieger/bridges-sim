package de.karstenkoehler.bridges.test.validators;

import de.karstenkoehler.bridges.io.validator.IslandPlacementValidator;
import de.karstenkoehler.bridges.io.validator.ValidateException;
import de.karstenkoehler.bridges.io.validator.Validator;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.Island;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class IslandPlacementTest {

    private static final int FIELD_SIZE = 10;
    private static final List<Connection> CONNECTIONS = new ArrayList<>();

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        List<Object[]> list = new ArrayList<>();
        list.addAll(data1());
        list.addAll(data2());
        return list;
    }

    public static Collection<Object[]> data1() {
        final Island island1a = new Island(0, 2, 0, 2);
        final Island island1b = new Island(1, 2, 0, 2);
        final Island island2a = new Island(2, 6, 1, 2);
        final Island island2b = new Island(3, 6, 1, 2);
        final Island island3a = new Island(4, 8, 6, 2);
        final Island island3b = new Island(5, 8, 6, 2);

        final List<Island> valid1 = Arrays.asList(island1a, island2a, island3a);
        final List<Island> valid2 = Arrays.asList(island1b, island2b, island3b);

        final List<Island> invalid1 = Arrays.asList(island1a, island1b);
        final List<Island> invalid2 = Arrays.asList(island2a, island2b);
        final List<Island> invalid3 = Arrays.asList(island3a, island3b);

        return Arrays.asList(new Object[][]{
                {null, new BridgesPuzzle(valid1, CONNECTIONS, FIELD_SIZE, FIELD_SIZE)},
                {null, new BridgesPuzzle(valid2, CONNECTIONS, FIELD_SIZE, FIELD_SIZE)},

                {ValidateException.class, new BridgesPuzzle(invalid1, CONNECTIONS, FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(invalid2, CONNECTIONS, FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(invalid3, CONNECTIONS, FIELD_SIZE, FIELD_SIZE)},

        });
    }

    public static Collection<Object[]> data2() {
        final Island island1a = new Island(0, 2, 0, 2);
        final Island island1b = new Island(1, 2, 1, 2);
        final Island island2a = new Island(2, 6, 1, 2);
        final Island island2b = new Island(3, 5, 1, 2);
        final Island island3a = new Island(4, 6, 6, 2);
        final Island island3b = new Island(5, 7, 7, 2);

        final List<Island> valid1 = Arrays.asList(island1a, island2a, island3a);
        final List<Island> valid2 = Arrays.asList(island3a, island3b);

        final List<Island> invalid1 = Arrays.asList(island1a, island1b);
        final List<Island> invalid2 = Arrays.asList(island2a, island2b);

        return Arrays.asList(new Object[][]{
                {null, new BridgesPuzzle(valid1, CONNECTIONS, FIELD_SIZE, FIELD_SIZE)},
                {null, new BridgesPuzzle(valid2, CONNECTIONS, FIELD_SIZE, FIELD_SIZE)},

                {ValidateException.class, new BridgesPuzzle(invalid1, CONNECTIONS, FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(invalid2, CONNECTIONS, FIELD_SIZE, FIELD_SIZE)},
        });
    }


    @Parameterized.Parameter
    public Class<? extends Exception> expectedException;

    @Parameterized.Parameter(1)
    public BridgesPuzzle input;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testValidator() throws ValidateException {
        if (expectedException != null) {
            thrown.expect(expectedException);
        }

        validator.validate(input);
    }

    private static Validator validator;

    @BeforeClass
    public static void setup() {
        validator = new IslandPlacementValidator();
    }
}
