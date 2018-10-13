package de.karstenkoehler.bridges.test.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.validators.NoIslandsOnAdjacentFieldsValidator;
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
public class NoIslandsOnAdjacentFieldsTest {

    private static final int FIELD_SIZE = 10;
    private static final List<Edge> bridges = new ArrayList<>();

    private static final Node node1 = new Node(0, 2, 0, 2);
    private static final Node node1adjacent = new Node(1, 2, 1, 2);
    private static final Node node2 = new Node(2, 6, 1, 2);
    private static final Node node2adjacent = new Node(3, 5, 1, 2);
    private static final Node node3 = new Node(4, 6, 6, 2);
    private static final Node node3diagonal = new Node(5, 7, 7, 2);

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        final Map<Integer, Node> valid1 = new HashMap<>();
        valid1.put(node1.getId(), node1);
        valid1.put(node2.getId(), node2);
        valid1.put(node3.getId(), node3);

        final Map<Integer, Node> valid2 = new HashMap<>();
        valid2.put(node3.getId(), node3);
        valid2.put(node3diagonal.getId(), node3diagonal);

        final Map<Integer, Node> invalid1 = new HashMap<>();
        invalid1.put(node1.getId(), node1);
        invalid1.put(node1adjacent.getId(), node1adjacent);

        final Map<Integer, Node> invalid2 = new HashMap<>();
        invalid2.put(node2.getId(), node2);
        invalid2.put(node2adjacent.getId(), node2adjacent);

        return Arrays.asList(new Object[][]{
                {null, new ParseResult(valid1, bridges, FIELD_SIZE, FIELD_SIZE)},
                {null, new ParseResult(valid2, bridges, FIELD_SIZE, FIELD_SIZE)},

                {ValidateException.class, new ParseResult(invalid1, bridges, FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new ParseResult(invalid2, bridges, FIELD_SIZE, FIELD_SIZE)},

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
        validator = new NoIslandsOnAdjacentFieldsValidator();
    }
}
