package de.karstenkoehler.bridges.test.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.validators.IslandsOnFieldValidator;
import de.karstenkoehler.bridges.io.validators.ValidateException;
import de.karstenkoehler.bridges.io.validators.Validator;
import de.karstenkoehler.bridges.model.Edge;
import de.karstenkoehler.bridges.model.Node;
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
    private static final List<Edge> bridges = new ArrayList<>();

    private static final Node valid1 = new Node(0, 0, 9, 2);
    private static final Node valid2 = new Node(1, 9, 0, 2);
    private static final Node valid3 = new Node(2, 0, 0, 2);
    private static final Node valid4 = new Node(3, 9, 9, 2);
    private static final Node valid5 = new Node(4, 5, 5, 2);

    private static final Node invalid1 = new Node(5, -1, 1, 2);
    private static final Node invalid2 = new Node(6, 1, -1, 2);
    private static final Node invalid3 = new Node(7, 10, 1, 2);
    private static final Node invalid4 = new Node(8, 1, 10, 2);
    private static final Node invalid5 = new Node(9, 10, 10, 2);

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {null, new ParseResult(Collections.singletonMap(valid1.getId(), valid1), bridges, FIELD_SIZE, FIELD_SIZE)},
                {null, new ParseResult(Collections.singletonMap(valid2.getId(), valid2), bridges, FIELD_SIZE, FIELD_SIZE)},
                {null, new ParseResult(Collections.singletonMap(valid3.getId(), valid3), bridges, FIELD_SIZE, FIELD_SIZE)},
                {null, new ParseResult(Collections.singletonMap(valid4.getId(), valid4), bridges, FIELD_SIZE, FIELD_SIZE)},
                {null, new ParseResult(Collections.singletonMap(valid5.getId(), valid5), bridges, FIELD_SIZE, FIELD_SIZE)},

                {ValidateException.class, new ParseResult(Collections.singletonMap(invalid1.getId(), invalid1), bridges, FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new ParseResult(Collections.singletonMap(invalid2.getId(), invalid2), bridges, FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new ParseResult(Collections.singletonMap(invalid3.getId(), invalid3), bridges, FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new ParseResult(Collections.singletonMap(invalid4.getId(), invalid4), bridges, FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new ParseResult(Collections.singletonMap(invalid5.getId(), invalid5), bridges, FIELD_SIZE, FIELD_SIZE)},
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
        validator = new IslandsOnFieldValidator();
    }
}
