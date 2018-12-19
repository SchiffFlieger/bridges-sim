package de.karstenkoehler.bridges.test.validators;

import de.karstenkoehler.bridges.io.validator.IslandsOnFieldValidator;
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

import java.util.*;

@RunWith(Parameterized.class)
public class TestIslandsOnField {

    private static final int FIELD_SIZE = 10;
    private static final List<Connection> CONNECTIONS = new ArrayList<>();

    private static final Island valid1 = new Island(0, 0, 9, 2);
    private static final Island valid2 = new Island(1, 9, 0, 2);
    private static final Island valid3 = new Island(2, 0, 0, 2);
    private static final Island valid4 = new Island(3, 9, 9, 2);
    private static final Island valid5 = new Island(4, 5, 5, 2);

    private static final Island invalid1 = new Island(5, -1, 1, 2);
    private static final Island invalid2 = new Island(6, 1, -1, 2);
    private static final Island invalid3 = new Island(7, 10, 1, 2);
    private static final Island invalid4 = new Island(8, 1, 10, 2);
    private static final Island invalid5 = new Island(9, 10, 10, 2);

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {null, new BridgesPuzzle(Collections.singletonList(valid1), CONNECTIONS, FIELD_SIZE, FIELD_SIZE)},
                {null, new BridgesPuzzle(Collections.singletonList(valid2), CONNECTIONS, FIELD_SIZE, FIELD_SIZE)},
                {null, new BridgesPuzzle(Collections.singletonList(valid3), CONNECTIONS, FIELD_SIZE, FIELD_SIZE)},
                {null, new BridgesPuzzle(Collections.singletonList(valid4), CONNECTIONS, FIELD_SIZE, FIELD_SIZE)},
                {null, new BridgesPuzzle(Collections.singletonList(valid5), CONNECTIONS, FIELD_SIZE, FIELD_SIZE)},

                {ValidateException.class, new BridgesPuzzle(Collections.singletonList(invalid1), CONNECTIONS, FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(Collections.singletonList(invalid2), CONNECTIONS, FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(Collections.singletonList(invalid3), CONNECTIONS, FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(Collections.singletonList(invalid4), CONNECTIONS, FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(Collections.singletonList(invalid5), CONNECTIONS, FIELD_SIZE, FIELD_SIZE)},
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
        validator = new IslandsOnFieldValidator();
    }
}
