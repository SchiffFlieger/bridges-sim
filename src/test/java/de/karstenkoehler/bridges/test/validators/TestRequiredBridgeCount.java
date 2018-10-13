package de.karstenkoehler.bridges.test.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.validators.RequiredBridgesCountValidator;
import de.karstenkoehler.bridges.io.validators.ValidateException;
import de.karstenkoehler.bridges.io.validators.Validator;
import de.karstenkoehler.bridges.model.Node;
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

    private static Node valid1 = new Node(0, 1, 1, 1);
    private static Node valid2 = new Node(1, 1, 1, 2);
    private static Node valid3 = new Node(2, 1, 1, 5);
    private static Node valid4 = new Node(3, 1, 1, 8);
    private static Node invalid1 = new Node(4, 1, 1, -1);
    private static Node invalid2 = new Node(5, 1, 1, 0);
    private static Node invalid3 = new Node(6, 1, 1, 9);
    private static Node invalid4 = new Node(7, 1, 1, 99);

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {null, new ParseResult(Collections.singletonMap(valid1.getId(), valid1), null, 0, 0)},
                {null, new ParseResult(Collections.singletonMap(valid2.getId(), valid2), null, 0, 0)},
                {null, new ParseResult(Collections.singletonMap(valid3.getId(), valid3), null, 0, 0)},
                {null, new ParseResult(Collections.singletonMap(valid4.getId(), valid4), null, 0, 0)},

                {ValidateException.class, new ParseResult(Collections.singletonMap(invalid1.getId(), invalid1), null, 0, 0)},
                {ValidateException.class, new ParseResult(Collections.singletonMap(invalid2.getId(), invalid2), null, 0, 0)},
                {ValidateException.class, new ParseResult(Collections.singletonMap(invalid3.getId(), invalid3), null, 0, 0)},
                {ValidateException.class, new ParseResult(Collections.singletonMap(invalid4.getId(), invalid4), null, 0, 0)},
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
        validator = new RequiredBridgesCountValidator();
    }
}
