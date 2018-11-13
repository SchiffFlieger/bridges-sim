package de.karstenkoehler.bridges.test.validators;

import de.karstenkoehler.bridges.io.validator.FieldSizeValidator;
import de.karstenkoehler.bridges.io.validator.ValidateException;
import de.karstenkoehler.bridges.io.validator.Validator;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestFieldSize {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {null, new BridgesPuzzle(new ArrayList<>(), new ArrayList<>(), 4, 4)},
                {null, new BridgesPuzzle(new ArrayList<>(), new ArrayList<>(), 25, 25)},
                {null, new BridgesPuzzle(new ArrayList<>(), new ArrayList<>(), 4, 25)},
                {null, new BridgesPuzzle(new ArrayList<>(), new ArrayList<>(), 25, 4)},
                {null, new BridgesPuzzle(new ArrayList<>(), new ArrayList<>(), 12, 12)},

                {ValidateException.class, new BridgesPuzzle(new ArrayList<>(), new ArrayList<>(), 1, 1)},
                {ValidateException.class, new BridgesPuzzle(new ArrayList<>(), new ArrayList<>(), 1, 5)},
                {ValidateException.class, new BridgesPuzzle(new ArrayList<>(), new ArrayList<>(), 5, 1)},
                {ValidateException.class, new BridgesPuzzle(new ArrayList<>(), new ArrayList<>(), 5, 26)},
                {ValidateException.class, new BridgesPuzzle(new ArrayList<>(), new ArrayList<>(), 26, 5)},
                {ValidateException.class, new BridgesPuzzle(new ArrayList<>(), new ArrayList<>(), 26, 26)}
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
        validator = new FieldSizeValidator();
    }
}
