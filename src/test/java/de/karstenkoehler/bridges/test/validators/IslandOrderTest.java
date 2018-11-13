package de.karstenkoehler.bridges.test.validators;

import de.karstenkoehler.bridges.io.validator.IslandOrderValidator;
import de.karstenkoehler.bridges.io.validator.ValidateException;
import de.karstenkoehler.bridges.io.validator.Validator;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
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
public class IslandOrderTest {

    private static final int FIELD_SIZE = 10;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        final List<Island> valid = Arrays.asList(
                new Island(0, 0, 0, 2),
                new Island(1, 0, 2, 2),
                new Island(2, 0, 4, 2),
                new Island(3, 2, 0, 2),
                new Island(4, 2, 3, 2),
                new Island(5, 3, 2, 2),
                new Island(6, 3, 4, 2)
        );

        final List<Island> invalidX = Arrays.asList(
                new Island(0, 0, 0, 2),
                new Island(1, 1, 2, 2),
                new Island(2, 0, 4, 2),
                new Island(3, 2, 0, 2),
                new Island(4, 2, 3, 2),
                new Island(5, 3, 2, 2),
                new Island(6, 3, 4, 2)
        );

        final List<Island> invalidY = Arrays.asList(
                new Island(0, 0, 0, 2),
                new Island(1, 0, 4, 2),
                new Island(2, 0, 2, 2),
                new Island(3, 2, 0, 2),
                new Island(4, 2, 3, 2),
                new Island(5, 3, 2, 2),
                new Island(6, 3, 4, 2)
        );

        return Arrays.asList(new Object[][]{
                {null, new BridgesPuzzle(valid, new ArrayList<>(), FIELD_SIZE, FIELD_SIZE)},

                {ValidateException.class, new BridgesPuzzle(invalidX, new ArrayList<>(), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(invalidY, new ArrayList<>(), FIELD_SIZE, FIELD_SIZE)},
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
        validator = new IslandOrderValidator();
    }
}
