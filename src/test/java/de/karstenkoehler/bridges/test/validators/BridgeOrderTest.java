package de.karstenkoehler.bridges.test.validators;

import de.karstenkoehler.bridges.io.validator.BridgeOrderValidator;
import de.karstenkoehler.bridges.io.validator.ValidateException;
import de.karstenkoehler.bridges.io.validator.Validator;
import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Island;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

@RunWith(Parameterized.class)
public class BridgeOrderTest {

    private static final int FIELD_SIZE = 10;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        final Map<Integer, Island> islands = new HashMap<>();
        islands.put(0, new Island(0, 0, 0, 2));
        islands.put(1, new Island(1, 0, 2, 2));
        islands.put(2, new Island(2, 0, 4, 2));
        islands.put(3, new Island(3, 2, 0, 2));
        islands.put(4, new Island(4, 2, 3, 2));
        islands.put(5, new Island(5, 3, 2, 2));
        islands.put(6, new Island(6, 3, 4, 2));

        final List<Bridge> valid = Arrays.asList(
                new Bridge(islands.get(0), islands.get(1), 2),
                new Bridge(islands.get(0), islands.get(6), 2),
                new Bridge(islands.get(3), islands.get(4), 2)
        );

        final List<Bridge> invalidNode1 = Arrays.asList(
                new Bridge(islands.get(0), islands.get(1), 2),
                new Bridge(islands.get(3), islands.get(1), 2),
                new Bridge(islands.get(0), islands.get(6), 2)
        );

        final List<Bridge> invalidNode2 = Arrays.asList(
                new Bridge(islands.get(0), islands.get(6), 2),
                new Bridge(islands.get(0), islands.get(1), 2),
                new Bridge(islands.get(3), islands.get(4), 2)
        );

        return Arrays.asList(new Object[][]{
                {null, new BridgesPuzzle(islands, valid, FIELD_SIZE, FIELD_SIZE)},

                {ValidateException.class, new BridgesPuzzle(islands, invalidNode1, FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(islands, invalidNode2, FIELD_SIZE, FIELD_SIZE)},
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
        validator = new BridgeOrderValidator();
    }
}
