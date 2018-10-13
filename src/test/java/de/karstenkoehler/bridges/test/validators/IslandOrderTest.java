package de.karstenkoehler.bridges.test.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.validators.IslandOrderValidator;
import de.karstenkoehler.bridges.io.validators.ValidateException;
import de.karstenkoehler.bridges.io.validators.Validator;
import de.karstenkoehler.bridges.model.Node;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

@RunWith(Parameterized.class)
public class IslandOrderTest {

    private static final int FIELD_SIZE = 10;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        final Map<Integer, Node> valid = new HashMap<>();
        valid.put(0, new Node(0, 0, 0, 2));
        valid.put(1, new Node(1, 0, 2, 2));
        valid.put(2, new Node(2, 0, 4, 2));
        valid.put(3, new Node(3, 2, 0, 2));
        valid.put(4, new Node(4, 2, 3, 2));
        valid.put(5, new Node(5, 3, 2, 2));
        valid.put(6, new Node(6, 3, 4, 2));

        final Map<Integer, Node> invalidX = new HashMap<>();
        invalidX.put(0, new Node(0, 0, 0, 2));
        invalidX.put(1, new Node(1, 1, 2, 2));
        invalidX.put(2, new Node(2, 0, 4, 2));
        invalidX.put(3, new Node(3, 2, 0, 2));
        invalidX.put(4, new Node(4, 2, 3, 2));
        invalidX.put(5, new Node(5, 3, 2, 2));
        invalidX.put(6, new Node(6, 3, 4, 2));

        final Map<Integer, Node> invalidY = new HashMap<>();
        invalidY.put(0, new Node(0, 0, 0, 2));
        invalidY.put(1, new Node(1, 0, 4, 2));
        invalidY.put(2, new Node(2, 0, 2, 2));
        invalidY.put(3, new Node(3, 2, 0, 2));
        invalidY.put(4, new Node(4, 2, 3, 2));
        invalidY.put(5, new Node(5, 3, 2, 2));
        invalidY.put(6, new Node(6, 3, 4, 2));

        return Arrays.asList(new Object[][]{
                {null, new ParseResult(valid, new ArrayList<>(), FIELD_SIZE, FIELD_SIZE)},

                {ValidateException.class, new ParseResult(invalidX, new ArrayList<>(), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new ParseResult(invalidY, new ArrayList<>(), FIELD_SIZE, FIELD_SIZE)},
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
        validator = new IslandOrderValidator();
    }
}
