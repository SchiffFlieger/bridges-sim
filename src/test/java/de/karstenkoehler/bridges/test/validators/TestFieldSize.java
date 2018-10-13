package de.karstenkoehler.bridges.test.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.validators.FieldSizeValidator;
import de.karstenkoehler.bridges.io.validators.ValidateException;
import de.karstenkoehler.bridges.io.validators.Validator;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

@RunWith(Parameterized.class)
public class TestFieldSize {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {null, new ParseResult(new HashMap<>(), new ArrayList<>(), 4, 4)},
                {null, new ParseResult(new HashMap<>(), new ArrayList<>(), 25, 25)},
                {null, new ParseResult(new HashMap<>(), new ArrayList<>(), 4, 25)},
                {null, new ParseResult(new HashMap<>(), new ArrayList<>(), 25, 4)},
                {null, new ParseResult(new HashMap<>(), new ArrayList<>(), 12, 12)},

                {ValidateException.class, new ParseResult(new HashMap<>(), new ArrayList<>(), 1, 1)},
                {ValidateException.class, new ParseResult(new HashMap<>(), new ArrayList<>(), 1, 5)},
                {ValidateException.class, new ParseResult(new HashMap<>(), new ArrayList<>(), 5, 1)},
                {ValidateException.class, new ParseResult(new HashMap<>(), new ArrayList<>(), 5, 26)},
                {ValidateException.class, new ParseResult(new HashMap<>(), new ArrayList<>(), 26, 5)},
                {ValidateException.class, new ParseResult(new HashMap<>(), new ArrayList<>(), 26, 26)}
        });
    }

    @Parameterized.Parameter
    public Class<? extends Exception> expectedException;

    @Parameterized.Parameter(1)
    public ParseResult input;

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
