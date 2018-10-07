package de.karstenkoehler.bridges.test.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.ValidateException;
import de.karstenkoehler.bridges.io.validators.BridgeOrderValidator;
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
public class BridgeOrderTest {

    private static final int FIELD_SIZE = 10;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        final Map<Integer, Node> islands = new HashMap<>();
        islands.put(0, new Node(0, 0, 0, 2));
        islands.put(1, new Node(1, 0, 2, 2));
        islands.put(2, new Node(2, 0, 4, 2));
        islands.put(3, new Node(3, 2, 0, 2));
        islands.put(4, new Node(4, 2, 3, 2));
        islands.put(5, new Node(5, 3, 2, 2));
        islands.put(6, new Node(6, 3, 4, 2));

        final List<Edge> valid = Arrays.asList(
                new Edge(0, 0, 1, 2),
                new Edge(1, 0, 6, 2),
                new Edge(2, 3, 4, 2)
        );

        final List<Edge> invalidNode1 = Arrays.asList(
                new Edge(0, 0, 1, 2),
                new Edge(1, 3, 1, 2),
                new Edge(2, 0, 6, 2)
        );

        final List<Edge> invalidNode2 = Arrays.asList(
                new Edge(0, 0, 6, 2),
                new Edge(1, 0, 1, 2),
                new Edge(2, 3, 4, 2)
        );

        return Arrays.asList(new Object[][]{
                {null, new ParseResult(islands, valid, FIELD_SIZE, FIELD_SIZE)},

                {ValidateException.class, new ParseResult(islands, invalidNode1, FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new ParseResult(islands, invalidNode2, FIELD_SIZE, FIELD_SIZE)},
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
        validator = new BridgeOrderValidator();
    }
}
