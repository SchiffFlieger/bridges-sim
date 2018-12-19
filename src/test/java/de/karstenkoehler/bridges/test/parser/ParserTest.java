package de.karstenkoehler.bridges.test.parser;

import de.karstenkoehler.bridges.io.parser.ParseException;
import de.karstenkoehler.bridges.io.parser.Parser;
import de.karstenkoehler.bridges.io.parser.TokenConsumingParser;
import de.karstenkoehler.bridges.io.parser.token.Token;
import de.karstenkoehler.bridges.io.parser.token.Tokenizer;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.Island;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ParserTest {

    private static final List<Token> bsp_5x5 = Arrays.asList(new Token("FIELD", Token.Type.FIELD_SECTION), new Token("5", Token.Type.NUMBER), new Token("x", Token.Type.X), new Token("5", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("2", Token.Type.NUMBER), new Token("ISLANDS", Token.Type.ISLAND_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("0", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("2", Token.Type.NUMBER), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("3", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("0", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("2", Token.Type.NUMBER), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_sol = Arrays.asList(new Token("FIELD", Token.Type.FIELD_SECTION), new Token("5", Token.Type.NUMBER), new Token("x", Token.Type.X), new Token("5", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("2", Token.Type.NUMBER), new Token("ISLANDS", Token.Type.ISLAND_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("0", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("2", Token.Type.NUMBER), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("3", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("0", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("2", Token.Type.NUMBER), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("BRIDGES", Token.Type.BRIDGES_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("1", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("true", Token.Type.BOOL), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("", Token.Type.EOF));

    // field section error
    private static final List<Token> bsp_5x5_missingFieldSection = Arrays.asList(new Token("5", Token.Type.NUMBER), new Token("x", Token.Type.X), new Token("5", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("1", Token.Type.NUMBER), new Token("ISLANDS", Token.Type.ISLAND_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("0", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("3", Token.Type.NUMBER), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("BRIDGES", Token.Type.BRIDGES_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("1", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("true", Token.Type.BOOL), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingOneDimension1 = Arrays.asList(new Token("FIELD", Token.Type.FIELD_SECTION), new Token("x", Token.Type.X), new Token("5", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("1", Token.Type.NUMBER), new Token("ISLANDS", Token.Type.ISLAND_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("0", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("3", Token.Type.NUMBER), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("BRIDGES", Token.Type.BRIDGES_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("1", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("true", Token.Type.BOOL), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingOneDimension2 = Arrays.asList(new Token("FIELD", Token.Type.FIELD_SECTION), new Token("5", Token.Type.NUMBER), new Token("x", Token.Type.X), new Token("|", Token.Type.PIPE), new Token("1", Token.Type.NUMBER), new Token("ISLANDS", Token.Type.ISLAND_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("0", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("3", Token.Type.NUMBER), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("BRIDGES", Token.Type.BRIDGES_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("1", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("true", Token.Type.BOOL), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingIslandCount = Arrays.asList(new Token("FIELD", Token.Type.FIELD_SECTION), new Token("5", Token.Type.NUMBER), new Token("x", Token.Type.X), new Token("5", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("ISLANDS", Token.Type.ISLAND_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("0", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("3", Token.Type.NUMBER), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("BRIDGES", Token.Type.BRIDGES_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("1", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("true", Token.Type.BOOL), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingFieldDelimiterChars = Arrays.asList(new Token("FIELD", Token.Type.FIELD_SECTION), new Token("5", Token.Type.NUMBER), new Token("5", Token.Type.NUMBER), new Token("1", Token.Type.NUMBER), new Token("ISLANDS", Token.Type.ISLAND_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("0", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("3", Token.Type.NUMBER), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("BRIDGES", Token.Type.BRIDGES_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("1", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("true", Token.Type.BOOL), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingAllDimensions = Arrays.asList(new Token("FIELD", Token.Type.FIELD_SECTION), new Token("ISLANDS", Token.Type.ISLAND_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("0", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("3", Token.Type.NUMBER), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("BRIDGES", Token.Type.BRIDGES_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("1", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("true", Token.Type.BOOL), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("", Token.Type.EOF));

    // island section error
    private static final List<Token> bsp_5x5_missingIslandSection = Arrays.asList(new Token("FIELD", Token.Type.FIELD_SECTION), new Token("5", Token.Type.NUMBER), new Token("x", Token.Type.X), new Token("5", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("1", Token.Type.NUMBER), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("0", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("3", Token.Type.NUMBER), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("BRIDGES", Token.Type.BRIDGES_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("1", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("true", Token.Type.BOOL), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingIslandParenthesis = Arrays.asList(new Token("FIELD", Token.Type.FIELD_SECTION), new Token("5", Token.Type.NUMBER), new Token("x", Token.Type.X), new Token("5", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("1", Token.Type.NUMBER), new Token("ISLANDS", Token.Type.ISLAND_SECTION), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("0", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("3", Token.Type.NUMBER), new Token("BRIDGES", Token.Type.BRIDGES_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("1", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("true", Token.Type.BOOL), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingXcoord = Arrays.asList(new Token("FIELD", Token.Type.FIELD_SECTION), new Token("5", Token.Type.NUMBER), new Token("x", Token.Type.X), new Token("5", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("1", Token.Type.NUMBER), new Token("ISLANDS", Token.Type.ISLAND_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token(",", Token.Type.COMMA), new Token("0", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("3", Token.Type.NUMBER), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("BRIDGES", Token.Type.BRIDGES_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("1", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("true", Token.Type.BOOL), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingYcoord = Arrays.asList(new Token("FIELD", Token.Type.FIELD_SECTION), new Token("5", Token.Type.NUMBER), new Token("x", Token.Type.X), new Token("5", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("1", Token.Type.NUMBER), new Token("ISLANDS", Token.Type.ISLAND_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("|", Token.Type.PIPE), new Token("3", Token.Type.NUMBER), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("BRIDGES", Token.Type.BRIDGES_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("1", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("true", Token.Type.BOOL), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingBridgeCount = Arrays.asList(new Token("FIELD", Token.Type.FIELD_SECTION), new Token("5", Token.Type.NUMBER), new Token("x", Token.Type.X), new Token("5", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("1", Token.Type.NUMBER), new Token("ISLANDS", Token.Type.ISLAND_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("0", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("BRIDGES", Token.Type.BRIDGES_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("1", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("true", Token.Type.BOOL), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingDelimiterChars = Arrays.asList(new Token("FIELD", Token.Type.FIELD_SECTION), new Token("5", Token.Type.NUMBER), new Token("x", Token.Type.X), new Token("5", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("1", Token.Type.NUMBER), new Token("ISLANDS", Token.Type.ISLAND_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token("0", Token.Type.NUMBER), new Token("3", Token.Type.NUMBER), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("2", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("4", Token.Type.NUMBER), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("BRIDGES", Token.Type.BRIDGES_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("1", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("true", Token.Type.BOOL), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("", Token.Type.EOF));

    // bridge section error
    private static final List<Token> bsp_5x5_missingBridgeSection = Arrays.asList(new Token("FIELD", Token.Type.FIELD_SECTION), new Token("5", Token.Type.NUMBER), new Token("x", Token.Type.X), new Token("5", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("1", Token.Type.NUMBER), new Token("ISLANDS", Token.Type.ISLAND_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("0", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("3", Token.Type.NUMBER), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("1", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("true", Token.Type.BOOL), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingBridgeParenthesis = Arrays.asList(new Token("FIELD", Token.Type.FIELD_SECTION), new Token("5", Token.Type.NUMBER), new Token("x", Token.Type.X), new Token("5", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("1", Token.Type.NUMBER), new Token("ISLANDS", Token.Type.ISLAND_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("0", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("3", Token.Type.NUMBER), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("BRIDGES", Token.Type.BRIDGES_SECTION), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("1", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("true", Token.Type.BOOL), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingIsland1 = Arrays.asList(new Token("FIELD", Token.Type.FIELD_SECTION), new Token("5", Token.Type.NUMBER), new Token("x", Token.Type.X), new Token("5", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("1", Token.Type.NUMBER), new Token("ISLANDS", Token.Type.ISLAND_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("0", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("3", Token.Type.NUMBER), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("BRIDGES", Token.Type.BRIDGES_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token(",", Token.Type.COMMA), new Token("1", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("true", Token.Type.BOOL), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingIsland2 = Arrays.asList(new Token("FIELD", Token.Type.FIELD_SECTION), new Token("5", Token.Type.NUMBER), new Token("x", Token.Type.X), new Token("5", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("1", Token.Type.NUMBER), new Token("ISLANDS", Token.Type.ISLAND_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("0", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("3", Token.Type.NUMBER), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("BRIDGES", Token.Type.BRIDGES_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("|", Token.Type.PIPE), new Token("true", Token.Type.BOOL), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingDoubleBridge = Arrays.asList(new Token("FIELD", Token.Type.FIELD_SECTION), new Token("5", Token.Type.NUMBER), new Token("x", Token.Type.X), new Token("5", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("1", Token.Type.NUMBER), new Token("ISLANDS", Token.Type.ISLAND_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("0", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("3", Token.Type.NUMBER), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("BRIDGES", Token.Type.BRIDGES_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("1", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingBridgeDelimiterChars = Arrays.asList(new Token("FIELD", Token.Type.FIELD_SECTION), new Token("5", Token.Type.NUMBER), new Token("x", Token.Type.X), new Token("5", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("1", Token.Type.NUMBER), new Token("ISLANDS", Token.Type.ISLAND_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token(",", Token.Type.COMMA), new Token("0", Token.Type.NUMBER), new Token("|", Token.Type.PIPE), new Token("3", Token.Type.NUMBER), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("BRIDGES", Token.Type.BRIDGES_SECTION), new Token("(", Token.Type.OPEN_PARENTHESIS), new Token("0", Token.Type.NUMBER), new Token("1", Token.Type.NUMBER), new Token("true", Token.Type.BOOL), new Token(")", Token.Type.CLOSE_PARENTHESIS), new Token("", Token.Type.EOF));


    @Parameterized.Parameters(name = "{index} - {0}")
    public static Collection<Object[]> data() {
        Island island0 = new Island(0, 0, 0, 2);
        Island island1 = new Island(1, 3, 0, 2);

        List<Island> islands = Arrays.asList(island0, island1);
        List<Connection> connections = Collections.singletonList(new Connection(island0, island1, 2));

        BridgesPuzzle bsp_5x5_parsed = new BridgesPuzzle(islands, new ArrayList<>(), 5, 5);
        BridgesPuzzle bsp_5x5_parsed_sol = new BridgesPuzzle(islands, connections, 5, 5);

        return Arrays.asList(new Object[][]{
                {"bsp_5x5", bsp_5x5, bsp_5x5_parsed, null},
                {"bsp_5x5_sol", bsp_5x5_sol, bsp_5x5_parsed_sol, null},

                // field section error
                {"bsp_5x5_missingFieldSection", bsp_5x5_missingFieldSection, null, ParseException.class},
                {"bsp_5x5_missingOneDimension1", bsp_5x5_missingOneDimension1, null, ParseException.class},
                {"bsp_5x5_missingOneDimension2", bsp_5x5_missingOneDimension2, null, ParseException.class},
                {"bsp_5x5_missingIslandCount", bsp_5x5_missingIslandCount, null, ParseException.class},
                {"bsp_5x5_missingFieldDelimiterChars", bsp_5x5_missingFieldDelimiterChars, null, ParseException.class},
                {"bsp_5x5_missingAllDimensions", bsp_5x5_missingAllDimensions, null, ParseException.class},

                // island section error
                {"bsp_5x5_missingIslandSection", bsp_5x5_missingIslandSection, null, ParseException.class},
                {"bsp_5x5_missingIslandParenthesis", bsp_5x5_missingIslandParenthesis, null, ParseException.class},
                {"bsp_5x5_missingXcoord", bsp_5x5_missingXcoord, null, ParseException.class},
                {"bsp_5x5_missingYcoord", bsp_5x5_missingYcoord, null, ParseException.class},
                {"bsp_5x5_missingBridgeCount", bsp_5x5_missingBridgeCount, null, ParseException.class},
                {"bsp_5x5_missingDelimiterChars", bsp_5x5_missingDelimiterChars, null, ParseException.class},

                // bridge section error
                {"bsp_5x5_missingBridgeSection", bsp_5x5_missingBridgeSection, null, ParseException.class},
                {"bsp_5x5_missingBridgeParenthesis", bsp_5x5_missingBridgeParenthesis, null, ParseException.class},
                {"bsp_5x5_missingIsland1", bsp_5x5_missingIsland1, null, ParseException.class},
                {"bsp_5x5_missingIsland2", bsp_5x5_missingIsland2, null, ParseException.class},
                {"bsp_5x5_missingDoubleBridge", bsp_5x5_missingDoubleBridge, null, ParseException.class},
                {"bsp_5x5_missingBridgeDelimiterChars", bsp_5x5_missingBridgeDelimiterChars, null, ParseException.class},
        });
    }

    @Parameterized.Parameter
    public String name;

    @Parameterized.Parameter(1)
    public List<Token> input;

    @Parameterized.Parameter(2)
    public BridgesPuzzle expectedOutput;

    @Parameterized.Parameter(3)
    public Class<? extends Exception> expectedException;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testParser() throws ParseException {
        if (expectedException != null) {
            thrown.expect(expectedException);
        }

        Parser parser = new TokenConsumingParser(new MockTokenizer(input));
        BridgesPuzzle actual = parser.parse();

        assertEquals(expectedOutput.getWidth(), actual.getWidth());
        assertEquals(expectedOutput.getHeight(), actual.getHeight());
        assertEquals(expectedOutput.getConnections(), actual.getConnections());
        assertEquals(expectedOutput.getIslands(), actual.getIslands());
    }

    private class MockTokenizer implements Tokenizer {
        private final List<Token> tokens;
        private int current;

        MockTokenizer(List<Token> tokens) {
            this.tokens = tokens;
            this.current = 0;
        }

        @Override
        public Token next() {
            if (current > tokens.size()) {
                throw new AssertionError("fetched too many elements from MockTokenizer");
            }

            if (current == tokens.size()) {
                return null;
            }

            return tokens.get(current++);
        }
    }
}
