package de.karstenkoehler.bridges.test.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.ValidateException;
import de.karstenkoehler.bridges.io.validators.MaxOneBridgePerIslandPairValidator;
import de.karstenkoehler.bridges.io.validators.Validator;
import de.karstenkoehler.bridges.model.Edge;
import de.karstenkoehler.bridges.model.Node;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class MaxOneBridgePerIslandPairTest {

    private static final int FIELD_SIZE = 10;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        final Map<Integer, Node> islands = new HashMap<>();
        islands.put(0, new Node(0, 0, 0, 2));
        islands.put(1, new Node(1, 2, 1, 2));
        islands.put(2, new Node(2, 3, 1, 2));
        islands.put(3, new Node(3, 3, 4, 2));
        islands.put(4, new Node(4, 5, 1, 2));
        islands.put(5, new Node(5, 6, 0, 2));
        islands.put(6, new Node(6, 6, 2, 2));

        final Edge bridge1a = new Edge(0, 0, 5);
        final Edge bridge1b = new Edge(1, 0, 5);
        final Edge bridge2a = new Edge(2, 2, 3);
        final Edge bridge2b = new Edge(3, 2, 3);
        final Edge bridge3a = new Edge(4, 1, 6);
        final Edge bridge3b = new Edge(5, 6, 1);

        return Arrays.asList(new Object[][]{
                {null, new ParseResult(islands, Arrays.asList(bridge1a, bridge2a, bridge3a), FIELD_SIZE, FIELD_SIZE)},

                {ValidateException.class, new ParseResult(islands, Arrays.asList(bridge1a, bridge1b), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new ParseResult(islands, Arrays.asList(bridge2a, bridge2b), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new ParseResult(islands, Arrays.asList(bridge3a, bridge3b), FIELD_SIZE, FIELD_SIZE)},
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
        validator = new MaxOneBridgePerIslandPairValidator();
    }
}
