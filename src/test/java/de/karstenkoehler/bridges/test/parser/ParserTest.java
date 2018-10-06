package de.karstenkoehler.bridges.test.parser;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.parser.ParseException;
import de.karstenkoehler.bridges.io.parser.Parser;
import de.karstenkoehler.bridges.io.parser.TokenConsumingParser;
import de.karstenkoehler.bridges.io.parser.token.Token;
import de.karstenkoehler.bridges.io.parser.token.Tokenizer;
import de.karstenkoehler.bridges.model.Edge;
import de.karstenkoehler.bridges.model.Node;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ParserTest {

    private static final List<Token> bsp_5x5 = Arrays.asList(new Token("FIELD", Token.Type.FieldSection), new Token("5", Token.Type.Number), new Token("x", Token.Type.X), new Token("5", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("1", Token.Type.Number), new Token("ISLANDS", Token.Type.IslandSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("0", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("3", Token.Type.Number), new Token(")", Token.Type.CloseParenthesis), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_sol = Arrays.asList(new Token("FIELD", Token.Type.FieldSection), new Token("5", Token.Type.Number), new Token("x", Token.Type.X), new Token("5", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("1", Token.Type.Number), new Token("ISLANDS", Token.Type.IslandSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("0", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("3", Token.Type.Number), new Token(")", Token.Type.CloseParenthesis), new Token("BRIDGES", Token.Type.BridgesSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("1", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("true", Token.Type.Bool), new Token(")", Token.Type.CloseParenthesis), new Token("", Token.Type.EOF));

    // field section error
    private static final List<Token> bsp_5x5_missingFieldSection = Arrays.asList(new Token("5", Token.Type.Number), new Token("x", Token.Type.X), new Token("5", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("1", Token.Type.Number), new Token("ISLANDS", Token.Type.IslandSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("0", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("3", Token.Type.Number), new Token(")", Token.Type.CloseParenthesis), new Token("BRIDGES", Token.Type.BridgesSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("1", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("true", Token.Type.Bool), new Token(")", Token.Type.CloseParenthesis), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingOneDimension1 = Arrays.asList(new Token("FIELD", Token.Type.FieldSection), new Token("x", Token.Type.X), new Token("5", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("1", Token.Type.Number), new Token("ISLANDS", Token.Type.IslandSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("0", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("3", Token.Type.Number), new Token(")", Token.Type.CloseParenthesis), new Token("BRIDGES", Token.Type.BridgesSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("1", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("true", Token.Type.Bool), new Token(")", Token.Type.CloseParenthesis), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingOneDimension2 = Arrays.asList(new Token("FIELD", Token.Type.FieldSection), new Token("5", Token.Type.Number), new Token("x", Token.Type.X), new Token("|", Token.Type.Pipe), new Token("1", Token.Type.Number), new Token("ISLANDS", Token.Type.IslandSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("0", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("3", Token.Type.Number), new Token(")", Token.Type.CloseParenthesis), new Token("BRIDGES", Token.Type.BridgesSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("1", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("true", Token.Type.Bool), new Token(")", Token.Type.CloseParenthesis), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingIslandCount = Arrays.asList(new Token("FIELD", Token.Type.FieldSection), new Token("5", Token.Type.Number), new Token("x", Token.Type.X), new Token("5", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("ISLANDS", Token.Type.IslandSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("0", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("3", Token.Type.Number), new Token(")", Token.Type.CloseParenthesis), new Token("BRIDGES", Token.Type.BridgesSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("1", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("true", Token.Type.Bool), new Token(")", Token.Type.CloseParenthesis), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingFieldDelimiterChars = Arrays.asList(new Token("FIELD", Token.Type.FieldSection), new Token("5", Token.Type.Number), new Token("5", Token.Type.Number), new Token("1", Token.Type.Number), new Token("ISLANDS", Token.Type.IslandSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("0", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("3", Token.Type.Number), new Token(")", Token.Type.CloseParenthesis), new Token("BRIDGES", Token.Type.BridgesSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("1", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("true", Token.Type.Bool), new Token(")", Token.Type.CloseParenthesis), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingAllDimensions = Arrays.asList(new Token("FIELD", Token.Type.FieldSection), new Token("ISLANDS", Token.Type.IslandSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("0", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("3", Token.Type.Number), new Token(")", Token.Type.CloseParenthesis), new Token("BRIDGES", Token.Type.BridgesSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("1", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("true", Token.Type.Bool), new Token(")", Token.Type.CloseParenthesis), new Token("", Token.Type.EOF));

    // island section error
    private static final List<Token> bsp_5x5_missingIslandSection = Arrays.asList(new Token("FIELD", Token.Type.FieldSection), new Token("5", Token.Type.Number), new Token("x", Token.Type.X), new Token("5", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("1", Token.Type.Number), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("0", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("3", Token.Type.Number), new Token(")", Token.Type.CloseParenthesis), new Token("BRIDGES", Token.Type.BridgesSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("1", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("true", Token.Type.Bool), new Token(")", Token.Type.CloseParenthesis), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingIslandParenthesis = Arrays.asList(new Token("FIELD", Token.Type.FieldSection), new Token("5", Token.Type.Number), new Token("x", Token.Type.X), new Token("5", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("1", Token.Type.Number), new Token("ISLANDS", Token.Type.IslandSection), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("0", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("3", Token.Type.Number), new Token("BRIDGES", Token.Type.BridgesSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("1", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("true", Token.Type.Bool), new Token(")", Token.Type.CloseParenthesis), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingXcoord = Arrays.asList(new Token("FIELD", Token.Type.FieldSection), new Token("5", Token.Type.Number), new Token("x", Token.Type.X), new Token("5", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("1", Token.Type.Number), new Token("ISLANDS", Token.Type.IslandSection), new Token("(", Token.Type.OpenParenthesis), new Token(",", Token.Type.Comma), new Token("0", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("3", Token.Type.Number), new Token(")", Token.Type.CloseParenthesis), new Token("BRIDGES", Token.Type.BridgesSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("1", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("true", Token.Type.Bool), new Token(")", Token.Type.CloseParenthesis), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingYcoord = Arrays.asList(new Token("FIELD", Token.Type.FieldSection), new Token("5", Token.Type.Number), new Token("x", Token.Type.X), new Token("5", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("1", Token.Type.Number), new Token("ISLANDS", Token.Type.IslandSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("|", Token.Type.Pipe), new Token("3", Token.Type.Number), new Token(")", Token.Type.CloseParenthesis), new Token("BRIDGES", Token.Type.BridgesSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("1", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("true", Token.Type.Bool), new Token(")", Token.Type.CloseParenthesis), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingBridgeCount = Arrays.asList(new Token("FIELD", Token.Type.FieldSection), new Token("5", Token.Type.Number), new Token("x", Token.Type.X), new Token("5", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("1", Token.Type.Number), new Token("ISLANDS", Token.Type.IslandSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("0", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token(")", Token.Type.CloseParenthesis), new Token("BRIDGES", Token.Type.BridgesSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("1", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("true", Token.Type.Bool), new Token(")", Token.Type.CloseParenthesis), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingDelimiterChars = Arrays.asList(new Token("FIELD", Token.Type.FieldSection), new Token("5", Token.Type.Number), new Token("x", Token.Type.X), new Token("5", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("1", Token.Type.Number), new Token("ISLANDS", Token.Type.IslandSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token("0", Token.Type.Number), new Token("3", Token.Type.Number), new Token(")", Token.Type.CloseParenthesis), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("2", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("4", Token.Type.Number), new Token(")", Token.Type.CloseParenthesis), new Token("BRIDGES", Token.Type.BridgesSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("1", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("true", Token.Type.Bool), new Token(")", Token.Type.CloseParenthesis), new Token("", Token.Type.EOF));

    // bridge section error
    private static final List<Token> bsp_5x5_missingBridgeSection = Arrays.asList(new Token("FIELD", Token.Type.FieldSection), new Token("5", Token.Type.Number), new Token("x", Token.Type.X), new Token("5", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("1", Token.Type.Number), new Token("ISLANDS", Token.Type.IslandSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("0", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("3", Token.Type.Number), new Token(")", Token.Type.CloseParenthesis), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("1", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("true", Token.Type.Bool), new Token(")", Token.Type.CloseParenthesis), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingBridgeParenthesis = Arrays.asList(new Token("FIELD", Token.Type.FieldSection), new Token("5", Token.Type.Number), new Token("x", Token.Type.X), new Token("5", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("1", Token.Type.Number), new Token("ISLANDS", Token.Type.IslandSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("0", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("3", Token.Type.Number), new Token(")", Token.Type.CloseParenthesis), new Token("BRIDGES", Token.Type.BridgesSection), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("1", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("true", Token.Type.Bool), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingIsland1 = Arrays.asList(new Token("FIELD", Token.Type.FieldSection), new Token("5", Token.Type.Number), new Token("x", Token.Type.X), new Token("5", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("1", Token.Type.Number), new Token("ISLANDS", Token.Type.IslandSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("0", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("3", Token.Type.Number), new Token(")", Token.Type.CloseParenthesis), new Token("BRIDGES", Token.Type.BridgesSection), new Token("(", Token.Type.OpenParenthesis), new Token(",", Token.Type.Comma), new Token("1", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("true", Token.Type.Bool), new Token(")", Token.Type.CloseParenthesis), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingIsland2 = Arrays.asList(new Token("FIELD", Token.Type.FieldSection), new Token("5", Token.Type.Number), new Token("x", Token.Type.X), new Token("5", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("1", Token.Type.Number), new Token("ISLANDS", Token.Type.IslandSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("0", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("3", Token.Type.Number), new Token(")", Token.Type.CloseParenthesis), new Token("BRIDGES", Token.Type.BridgesSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("|", Token.Type.Pipe), new Token("true", Token.Type.Bool), new Token(")", Token.Type.CloseParenthesis), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingDoubleBridge = Arrays.asList(new Token("FIELD", Token.Type.FieldSection), new Token("5", Token.Type.Number), new Token("x", Token.Type.X), new Token("5", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("1", Token.Type.Number), new Token("ISLANDS", Token.Type.IslandSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("0", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("3", Token.Type.Number), new Token(")", Token.Type.CloseParenthesis), new Token("BRIDGES", Token.Type.BridgesSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("1", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token(")", Token.Type.CloseParenthesis), new Token("", Token.Type.EOF));
    private static final List<Token> bsp_5x5_missingBridgeDelimiterChars = Arrays.asList(new Token("FIELD", Token.Type.FieldSection), new Token("5", Token.Type.Number), new Token("x", Token.Type.X), new Token("5", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("1", Token.Type.Number), new Token("ISLANDS", Token.Type.IslandSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token(",", Token.Type.Comma), new Token("0", Token.Type.Number), new Token("|", Token.Type.Pipe), new Token("3", Token.Type.Number), new Token(")", Token.Type.CloseParenthesis), new Token("BRIDGES", Token.Type.BridgesSection), new Token("(", Token.Type.OpenParenthesis), new Token("0", Token.Type.Number), new Token("1", Token.Type.Number), new Token("true", Token.Type.Bool), new Token(")", Token.Type.CloseParenthesis), new Token("", Token.Type.EOF));


    @Parameterized.Parameters(name = "{index} - {0}")
    public static Collection<Object[]> data() {
        Map<Integer, Node> islands = Collections.singletonMap(0, new Node(0, 0, 0, 3));
        List<Edge> bridges = Collections.singletonList(new Edge(0, 0, 1, 2));

        ParseResult bsp_5x5_parsed = new ParseResult(islands, new ArrayList<>(), 5, 5);
        ParseResult bsp_5x5_parsed_sol = new ParseResult(islands, bridges, 5, 5);

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
    public ParseResult expectedOutput;

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
        ParseResult actual = parser.parse();

        assertEquals(expectedOutput.getWidth(), actual.getWidth());
        assertEquals(expectedOutput.getHeight(), actual.getHeight());
        assertEquals(expectedOutput.getBridges(), actual.getBridges());
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
            if (current >= tokens.size()) {
                throw new AssertionError("fetched too many elements from MockTokenizer");
            }

            return tokens.get(current++);
        }
    }
}
