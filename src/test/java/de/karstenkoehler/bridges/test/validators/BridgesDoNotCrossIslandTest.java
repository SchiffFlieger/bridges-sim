package de.karstenkoehler.bridges.test.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.validators.BridgesDoNotCrossIslandValidator;
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
public class BridgesDoNotCrossIslandTest {

    private static final int FIELD_SIZE = 10;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        final Map<Integer, Node> islands = new HashMap<>();
        islands.put(0, new Node(0, 0, 0, 2));
        islands.put(1, new Node(1, 1, 3, 2));
        islands.put(2, new Node(2, 2, 0, 2));
        islands.put(3, new Node(3, 2, 2, 2));
        islands.put(4, new Node(4, 2, 4, 2));
        islands.put(5, new Node(5, 4, 0, 2));
        islands.put(6, new Node(6, 4, 3, 2));

        final Edge validLeft = new Edge(0, 5, 2);
        final Edge validRight = new Edge(1, 0, 2);
        final Edge validUp = new Edge(2, 6, 5);
        final Edge validDown = new Edge(3, 3, 4);

        final Edge invalidLeft = new Edge(4, 5, 0);
        final Edge invalidRight = new Edge(5, 0, 5);
        final Edge invalidUp = new Edge(6, 4, 2);
        final Edge invalidDown = new Edge(7, 2, 4);

        return Arrays.asList(new Object[][]{
                {null, new ParseResult(islands, Arrays.asList(validLeft, validRight, validUp, validDown), FIELD_SIZE, FIELD_SIZE)},

                {ValidateException.class, new ParseResult(islands, Collections.singletonList(invalidLeft), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new ParseResult(islands, Collections.singletonList(invalidRight), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new ParseResult(islands, Collections.singletonList(invalidUp), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new ParseResult(islands, Collections.singletonList(invalidDown), FIELD_SIZE, FIELD_SIZE)},
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
        validator = new BridgesDoNotCrossIslandValidator();
    }
}
