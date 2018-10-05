package de.karstenkoehler.bridges.test.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.ValidateException;
import de.karstenkoehler.bridges.io.validators.IslandOnUniqueFieldsValidator;
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
public class IslandsOnUniqueFieldsTest {

    private static final int FIELD_SIZE = 10;
    private static final List<Edge> bridges = new ArrayList<>();

    private static final Node node1a = new Node(0, 2, 0, 2);
    private static final Node node1b = new Node(1, 2, 0, 2);
    private static final Node node2a = new Node(2, 6, 1, 2);
    private static final Node node2b = new Node(3, 6, 1, 2);
    private static final Node node3a = new Node(4, 8, 6, 2);
    private static final Node node3b = new Node(5, 8, 6, 2);

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        final Map<Integer, Node> valid1 = new HashMap<>();
        valid1.put(node1a.getId(), node1a);
        valid1.put(node2a.getId(), node2a);
        valid1.put(node3a.getId(), node3a);

        final Map<Integer, Node> valid2 = new HashMap<>();
        valid2.put(node1b.getId(), node1b);
        valid2.put(node2b.getId(), node2b);
        valid2.put(node3b.getId(), node3b);

        final Map<Integer, Node> invalid1 = new HashMap<>();
        invalid1.put(node1a.getId(), node1a);
        invalid1.put(node1b.getId(), node1b);

        final Map<Integer, Node> invalid2 = new HashMap<>();
        invalid2.put(node2a.getId(), node2a);
        invalid2.put(node2b.getId(), node2b);

        final Map<Integer, Node> invalid3 = new HashMap<>();
        invalid3.put(node3a.getId(), node3a);
        invalid3.put(node3b.getId(), node3b);

        return Arrays.asList(new Object[][]{
                {null, new ParseResult(valid1, bridges, FIELD_SIZE, FIELD_SIZE)},
                {null, new ParseResult(valid2, bridges, FIELD_SIZE, FIELD_SIZE)},

                {ValidateException.class, new ParseResult(invalid1, bridges, FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new ParseResult(invalid2, bridges, FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new ParseResult(invalid3, bridges, FIELD_SIZE, FIELD_SIZE)},

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
        validator = new IslandOnUniqueFieldsValidator();
    }
}
