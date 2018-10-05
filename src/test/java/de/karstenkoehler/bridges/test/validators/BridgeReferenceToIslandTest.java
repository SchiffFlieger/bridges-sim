package de.karstenkoehler.bridges.test.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.ValidateException;
import de.karstenkoehler.bridges.io.validators.BridgeReferenceToIslandValidator;
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
public class BridgeReferenceToIslandTest {

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

        final Edge valid1 = new Edge(0, 0, 5);
        final Edge valid2 = new Edge(1, 2, 3);
        final Edge valid3 = new Edge(2, 0, 5);

        final Edge invalid1 = new Edge(3, -1, 1);
        final Edge invalid2 = new Edge(4, 1, -1);
        final Edge invalid3 = new Edge(5, 7, 0);
        final Edge invalid4 = new Edge(6, 0, 7);
        final Edge invalid5 = new Edge(7, -1, 7);
        final Edge invalid6 = new Edge(8, 7, -1);

        return Arrays.asList(new Object[][]{
                {null, new ParseResult(islands, Arrays.asList(valid1, valid2, valid3), FIELD_SIZE, FIELD_SIZE)},

                {ValidateException.class, new ParseResult(islands, Collections.singletonList(invalid1), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new ParseResult(islands, Collections.singletonList(invalid2), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new ParseResult(islands, Collections.singletonList(invalid3), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new ParseResult(islands, Collections.singletonList(invalid4), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new ParseResult(islands, Collections.singletonList(invalid5), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new ParseResult(islands, Collections.singletonList(invalid6), FIELD_SIZE, FIELD_SIZE)},
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
        validator = new BridgeReferenceToIslandValidator();
    }
}
