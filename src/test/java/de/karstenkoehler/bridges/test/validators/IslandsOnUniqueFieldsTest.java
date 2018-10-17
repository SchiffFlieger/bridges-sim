package de.karstenkoehler.bridges.test.validators;

import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.io.validator.IslandOnUniqueFieldsValidator;
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
public class IslandsOnUniqueFieldsTest {

    private static final int FIELD_SIZE = 10;
    private static final List<Bridge> bridges = new ArrayList<>();

    private static final Island ISLAND_1_A = new Island(0, 2, 0, 2);
    private static final Island ISLAND_1_B = new Island(1, 2, 0, 2);
    private static final Island ISLAND_2_A = new Island(2, 6, 1, 2);
    private static final Island ISLAND_2_B = new Island(3, 6, 1, 2);
    private static final Island ISLAND_3_A = new Island(4, 8, 6, 2);
    private static final Island ISLAND_3_B = new Island(5, 8, 6, 2);

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        final Map<Integer, Island> valid1 = new HashMap<>();
        valid1.put(ISLAND_1_A.getId(), ISLAND_1_A);
        valid1.put(ISLAND_2_A.getId(), ISLAND_2_A);
        valid1.put(ISLAND_3_A.getId(), ISLAND_3_A);

        final Map<Integer, Island> valid2 = new HashMap<>();
        valid2.put(ISLAND_1_B.getId(), ISLAND_1_B);
        valid2.put(ISLAND_2_B.getId(), ISLAND_2_B);
        valid2.put(ISLAND_3_B.getId(), ISLAND_3_B);

        final Map<Integer, Island> invalid1 = new HashMap<>();
        invalid1.put(ISLAND_1_A.getId(), ISLAND_1_A);
        invalid1.put(ISLAND_1_B.getId(), ISLAND_1_B);

        final Map<Integer, Island> invalid2 = new HashMap<>();
        invalid2.put(ISLAND_2_A.getId(), ISLAND_2_A);
        invalid2.put(ISLAND_2_B.getId(), ISLAND_2_B);

        final Map<Integer, Island> invalid3 = new HashMap<>();
        invalid3.put(ISLAND_3_A.getId(), ISLAND_3_A);
        invalid3.put(ISLAND_3_B.getId(), ISLAND_3_B);

        return Arrays.asList(new Object[][]{
                {null, new BridgesPuzzle(valid1, bridges, FIELD_SIZE, FIELD_SIZE)},
                {null, new BridgesPuzzle(valid2, bridges, FIELD_SIZE, FIELD_SIZE)},

                {ValidateException.class, new BridgesPuzzle(invalid1, bridges, FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(invalid2, bridges, FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(invalid3, bridges, FIELD_SIZE, FIELD_SIZE)},

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
        validator = new IslandOnUniqueFieldsValidator();
    }
}
