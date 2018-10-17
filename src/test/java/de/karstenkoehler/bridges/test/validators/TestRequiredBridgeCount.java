package de.karstenkoehler.bridges.test.validators;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.io.validator.RequiredBridgesCountValidator;
import de.karstenkoehler.bridges.io.validator.ValidateException;
import de.karstenkoehler.bridges.io.validator.Validator;
import de.karstenkoehler.bridges.model.Island;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@RunWith(Parameterized.class)
public class TestRequiredBridgeCount {

    private static Island valid1 = new Island(0, 1, 1, 1);
    private static Island valid2 = new Island(1, 1, 1, 2);
    private static Island valid3 = new Island(2, 1, 1, 5);
    private static Island valid4 = new Island(3, 1, 1, 8);
    private static Island invalid1 = new Island(4, 1, 1, -1);
    private static Island invalid2 = new Island(5, 1, 1, 0);
    private static Island invalid3 = new Island(6, 1, 1, 9);
    private static Island invalid4 = new Island(7, 1, 1, 99);

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {null, new BridgesPuzzle(Collections.singletonMap(valid1.getId(), valid1), null, 0, 0)},
                {null, new BridgesPuzzle(Collections.singletonMap(valid2.getId(), valid2), null, 0, 0)},
                {null, new BridgesPuzzle(Collections.singletonMap(valid3.getId(), valid3), null, 0, 0)},
                {null, new BridgesPuzzle(Collections.singletonMap(valid4.getId(), valid4), null, 0, 0)},

                {ValidateException.class, new BridgesPuzzle(Collections.singletonMap(invalid1.getId(), invalid1), null, 0, 0)},
                {ValidateException.class, new BridgesPuzzle(Collections.singletonMap(invalid2.getId(), invalid2), null, 0, 0)},
                {ValidateException.class, new BridgesPuzzle(Collections.singletonMap(invalid3.getId(), invalid3), null, 0, 0)},
                {ValidateException.class, new BridgesPuzzle(Collections.singletonMap(invalid4.getId(), invalid4), null, 0, 0)},
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
        validator = new RequiredBridgesCountValidator();
    }
}
