package de.karstenkoehler.bridges.test.validators;

import de.karstenkoehler.bridges.io.validator.BridgesNotDiagonalValidator;
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
public class BridgesNotDiagonalTest {

    private static final int FIELD_SIZE = 10;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        final Map<Integer, Island> islands = new HashMap<>();
        islands.put(0, new Island(0, 0, 0, 2));
        islands.put(1, new Island(1, 2, 1, 2));
        islands.put(2, new Island(2, 3, 1, 2));
        islands.put(3, new Island(3, 3, 4, 2));
        islands.put(4, new Island(4, 5, 1, 2));
        islands.put(5, new Island(5, 6, 0, 2));
        islands.put(6, new Island(6, 6, 2, 2));

        final Bridge valid1 = new Bridge(islands.get(0), islands.get(5));
        final Bridge valid2 = new Bridge(islands.get(2), islands.get(3));
        final Bridge valid3 = new Bridge(islands.get(0), islands.get(5));

        final Bridge invalid1 = new Bridge(islands.get(0), islands.get(1));
        final Bridge invalid2 = new Bridge(islands.get(0), islands.get(2));
        final Bridge invalid3 = new Bridge(islands.get(1), islands.get(3));
        final Bridge invalid4 = new Bridge(islands.get(3), islands.get(4));
        final Bridge invalid5 = new Bridge(islands.get(2), islands.get(5));
        final Bridge invalid6 = new Bridge(islands.get(2), islands.get(6));

        return Arrays.asList(new Object[][]{
                {null, new BridgesPuzzle(islands, Arrays.asList(valid1, valid2, valid3), FIELD_SIZE, FIELD_SIZE)},

                {ValidateException.class, new BridgesPuzzle(islands, Collections.singletonList(invalid1), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(islands, Collections.singletonList(invalid2), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(islands, Collections.singletonList(invalid3), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(islands, Collections.singletonList(invalid4), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(islands, Collections.singletonList(invalid5), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(islands, Collections.singletonList(invalid6), FIELD_SIZE, FIELD_SIZE)},
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
        validator = new BridgesNotDiagonalValidator();
    }
}
