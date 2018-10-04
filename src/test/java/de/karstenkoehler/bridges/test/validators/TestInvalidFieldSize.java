package de.karstenkoehler.bridges.test.validators;

import de.karstenkoehler.bridges.io.DefaultValidator;
import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.ResultValidator;
import de.karstenkoehler.bridges.io.ValidateException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

@RunWith(Parameterized.class)
public class TestInvalidFieldSize {

    @Parameterized.Parameters
    public static Collection<ParseResult> data() {
        return Arrays.asList(
                new ParseResult(new HashMap<>(), new ArrayList<>(), 1, 1),
                new ParseResult(new HashMap<>(), new ArrayList<>(), 1, 5),
                new ParseResult(new HashMap<>(), new ArrayList<>(), 5, 1),
                new ParseResult(new HashMap<>(), new ArrayList<>(), 5, 26),
                new ParseResult(new HashMap<>(), new ArrayList<>(), 26, 5),
                new ParseResult(new HashMap<>(), new ArrayList<>(), 26, 26)
        );
    }

    @Parameterized.Parameter
    public ParseResult input;

    @Test(expected = ValidateException.class)
    public void FieldWidthTooLow() throws ValidateException {
        validator.validate(input);
    }

    private static ResultValidator validator;

    @BeforeClass
    public static void setup() {
        validator = new DefaultValidator();
    }
}
