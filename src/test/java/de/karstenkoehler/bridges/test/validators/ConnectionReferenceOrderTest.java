package de.karstenkoehler.bridges.test.validators;

import de.karstenkoehler.bridges.io.validator.BridgeReferenceOrderValidator;
import de.karstenkoehler.bridges.io.validator.ValidateException;
import de.karstenkoehler.bridges.io.validator.Validator;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.Island;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class ConnectionReferenceOrderTest {

    private static final int FIELD_SIZE = 10;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        final List<Island> islands = Arrays.asList(
                new Island(0, 0, 0, 2),
                new Island(1, 0, 2, 2),
                new Island(2, 0, 4, 2),
                new Island(3, 2, 0, 2),
                new Island(4, 2, 3, 2),
                new Island(5, 3, 2, 2),
                new Island(6, 3, 4, 2)
        );

        final Connection valid1 = new Connection(islands.get(0), islands.get(1), 2);
        final Connection valid2 = new Connection(islands.get(0), islands.get(6), 2);
        final Connection valid3 = new Connection(islands.get(3), islands.get(4), 2);

        final Connection invalid1 = new Connection(islands.get(6), islands.get(0), 2);
        final Connection invalid2 = new Connection(islands.get(2), islands.get(1), 2);
        final Connection invalid3 = new Connection(islands.get(5), islands.get(2), 2);

        return Arrays.asList(new Object[][]{
                {null, new BridgesPuzzle(islands, Arrays.asList(valid1, valid2, valid3), FIELD_SIZE, FIELD_SIZE)},

                {ValidateException.class, new BridgesPuzzle(islands, Collections.singletonList(invalid1), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(islands, Collections.singletonList(invalid2), FIELD_SIZE, FIELD_SIZE)},
                {ValidateException.class, new BridgesPuzzle(islands, Collections.singletonList(invalid3), FIELD_SIZE, FIELD_SIZE)},

        });
    }

    @Parameterized.Parameter
    public Class<? extends Exception> expectedException;

    @Parameterized.Parameter(1)
    public BridgesPuzzle input;

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
        validator = new BridgeReferenceOrderValidator();
    }
}
