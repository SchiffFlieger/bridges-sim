package de.karstenkoehler.bridges.test.validators;

import de.karstenkoehler.bridges.io.validator.BridgesDoNotCrossIslandValidator;
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
public class BridgesDoNotCrossIslandTest {

    private static final int FIELD_SIZE = 10;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        final Map<Integer, Island> islands = new HashMap<>();
        islands.put(0, new Island(0, 0, 0, 2));
        islands.put(1, new Island(1, 1, 3, 2));
        islands.put(2, new Island(2, 2, 0, 2));
        islands.put(3, new Island(3, 2, 2, 2));
        islands.put(4, new Island(4, 2, 4, 2));
        islands.put(5, new Island(5, 4, 0, 2));
        islands.put(6, new Island(6, 4, 3, 2));

        final Bridge validLeft = new Bridge(0, islands.get(5), islands.get(2));
        final Bridge validRight = new Bridge(1, islands.get(0), islands.get(2));
        final Bridge validUp = new Bridge(2, islands.get(6), islands.get(5));
        final Bridge validDown = new Bridge(3, islands.get(3), islands.get(4));

        final Bridge invalidLeft = new Bridge(4, islands.get(5), islands.get(0));
        final Bridge invalidRight = new Bridge(5, islands.get(0), islands.get(5));
        final Bridge invalidUp = new Bridge(6, islands.get(4), islands.get(2));
        final Bridge invalidDown = new Bridge(7, islands.get(2), islands.get(4));

        return Arrays.asList(new Object[][]{
                {null, new BridgesPuzzle(islands, Arrays.asList(validLeft, validRight, validUp, validDown), FIELD_SIZE, FIELD_SIZE)},

                {ValidateException.class, new BridgesPuzzle(islands, Collections.singletonList(invalidLeft), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(islands, Collections.singletonList(invalidRight), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(islands, Collections.singletonList(invalidUp), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(islands, Collections.singletonList(invalidDown), FIELD_SIZE, FIELD_SIZE)},
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
        validator = new BridgesDoNotCrossIslandValidator();
    }
}
