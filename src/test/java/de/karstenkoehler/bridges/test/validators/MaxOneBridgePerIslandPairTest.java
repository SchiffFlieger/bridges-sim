package de.karstenkoehler.bridges.test.validators;

import de.karstenkoehler.bridges.io.validator.MaxOneBridgePerIslandPairValidator;
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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class MaxOneBridgePerIslandPairTest {

    private static final int FIELD_SIZE = 10;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        final List<Island> islands = Arrays.asList(
                new Island(0, 0, 0, 2),
                new Island(1, 2, 1, 2),
                new Island(2, 3, 1, 2),
                new Island(3, 3, 4, 2),
                new Island(4, 5, 1, 2),
                new Island(5, 6, 0, 2),
                new Island(6, 6, 2, 2)
        );

        final Bridge bridge1a = new Bridge(islands.get(0), islands.get(5));
        final Bridge bridge1b = new Bridge(islands.get(0), islands.get(5));
        final Bridge bridge2a = new Bridge(islands.get(2), islands.get(3));
        final Bridge bridge2b = new Bridge(islands.get(2), islands.get(3));
        final Bridge bridge3a = new Bridge(islands.get(1), islands.get(6));
        final Bridge bridge3b = new Bridge(islands.get(6), islands.get(1));

        return Arrays.asList(new Object[][]{
                {null, new BridgesPuzzle(islands, Arrays.asList(bridge1a, bridge2a, bridge3a), FIELD_SIZE, FIELD_SIZE)},

                {ValidateException.class, new BridgesPuzzle(islands, Arrays.asList(bridge1a, bridge1b), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(islands, Arrays.asList(bridge2a, bridge2b), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(islands, Arrays.asList(bridge3a, bridge3b), FIELD_SIZE, FIELD_SIZE)},
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
        validator = new MaxOneBridgePerIslandPairValidator();
    }
}
