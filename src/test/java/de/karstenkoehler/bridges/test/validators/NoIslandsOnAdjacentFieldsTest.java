package de.karstenkoehler.bridges.test.validators;

import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.io.validator.NoIslandsOnAdjacentFieldsValidator;
import de.karstenkoehler.bridges.io.validator.ValidateException;
import de.karstenkoehler.bridges.io.validator.Validator;
import de.karstenkoehler.bridges.model.Island;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

@RunWith(Parameterized.class)
public class NoIslandsOnAdjacentFieldsTest {

    private static final int FIELD_SIZE = 10;
    private static final List<Bridge> bridges = new ArrayList<>();

    private static final Island ISLAND_1 = new Island(0, 2, 0, 2);
    private static final Island ISLAND_1_ADJACENT = new Island(1, 2, 1, 2);
    private static final Island ISLAND_2 = new Island(2, 6, 1, 2);
    private static final Island ISLAND_2_ADJACENT = new Island(3, 5, 1, 2);
    private static final Island ISLAND_3 = new Island(4, 6, 6, 2);
    private static final Island ISLAND_3_DIAGONAL = new Island(5, 7, 7, 2);

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        final Map<Integer, Island> valid1 = new HashMap<>();
        valid1.put(ISLAND_1.getId(), ISLAND_1);
        valid1.put(ISLAND_2.getId(), ISLAND_2);
        valid1.put(ISLAND_3.getId(), ISLAND_3);

        final Map<Integer, Island> valid2 = new HashMap<>();
        valid2.put(ISLAND_3.getId(), ISLAND_3);
        valid2.put(ISLAND_3_DIAGONAL.getId(), ISLAND_3_DIAGONAL);

        final Map<Integer, Island> invalid1 = new HashMap<>();
        invalid1.put(ISLAND_1.getId(), ISLAND_1);
        invalid1.put(ISLAND_1_ADJACENT.getId(), ISLAND_1_ADJACENT);

        final Map<Integer, Island> invalid2 = new HashMap<>();
        invalid2.put(ISLAND_2.getId(), ISLAND_2);
        invalid2.put(ISLAND_2_ADJACENT.getId(), ISLAND_2_ADJACENT);

        return Arrays.asList(new Object[][]{
                {null, new BridgesPuzzle(valid1, bridges, FIELD_SIZE, FIELD_SIZE)},
                {null, new BridgesPuzzle(valid2, bridges, FIELD_SIZE, FIELD_SIZE)},

                {ValidateException.class, new BridgesPuzzle(invalid1, bridges, FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(invalid2, bridges, FIELD_SIZE, FIELD_SIZE)},

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
        validator = new NoIslandsOnAdjacentFieldsValidator();
    }
}
