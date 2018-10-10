package de.karstenkoehler.bridges.test.parser;

import de.karstenkoehler.bridges.io.parser.ParseException;
import de.karstenkoehler.bridges.io.parser.token.Token;
import de.karstenkoehler.bridges.io.parser.token.Tokenizer;
import de.karstenkoehler.bridges.io.parser.token.TokenizerImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class TokenizerTest {

    private static final String numbers = "0 1 2 65 5486 012";
    private static final Token[] tkNumbers = new Token[]{
            new Token("0", Token.Type.NUMBER),
            new Token("1", Token.Type.NUMBER),
            new Token("2", Token.Type.NUMBER),
            new Token("65", Token.Type.NUMBER),
            new Token("5486", Token.Type.NUMBER),
            new Token("012", Token.Type.NUMBER),
    };

    private static final String bools = "true false false true";
    private static final Token[] tkBools = new Token[]{
            new Token("true", Token.Type.BOOL),
            new Token("false", Token.Type.BOOL),
            new Token("false", Token.Type.BOOL),
            new Token("true", Token.Type.BOOL),
    };

    private static final String sections = "FIELD ISLANDS BRIDGES";
    private static final Token[] tkSections = new Token[]{
            new Token("FIELD", Token.Type.FIELD_SECTION),
            new Token("ISLANDS", Token.Type.ISLAND_SECTION),
            new Token("BRIDGES", Token.Type.BRIDGES_SECTION),
    };

    private static final String symbols = "()|x,";
    private static final Token[] tkSymbols = new Token[]{
            new Token("(", Token.Type.OPEN_PARENTHESIS),
            new Token(")", Token.Type.CLOSE_PARENTHESIS),
            new Token("|", Token.Type.PIPE),
            new Token("x", Token.Type.X),
            new Token(",", Token.Type.COMMA),
    };

    @Parameterized.Parameters()
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {numbers, tkNumbers, null},
                {bools, tkBools, null},
                {sections, tkSections, null},
                {symbols, tkSymbols, null},

                {"!", null, ParseException.class},
                {"Field", null, ParseException.class},
                {"123abc", null, ParseException.class},
                {"(a, b)", null, ParseException.class},
                {"True", null, ParseException.class},
                {"faLSE", null, ParseException.class},
        });
    }

    @Parameterized.Parameter()
    public String input;

    @Parameterized.Parameter(1)
    public Token[] expectedOutput;

    @Parameterized.Parameter(2)
    public Class<? extends Exception> expectedException;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testTokenizer() throws ParseException {
        if (expectedException != null) {
            thrown.expect(expectedException);
        }

        Tokenizer tokenizer = new TokenizerImpl(input);
        Token[] actual = collect(tokenizer);

        assertTrue(Arrays.deepEquals(expectedOutput, actual));
    }

    private Token[] collect(Tokenizer tokenizer) throws ParseException {
        List<Token> tokens = new ArrayList<>();
        for (Token tk = tokenizer.next(); tk.getType() != Token.Type.EOF; tk = tokenizer.next()) {
            tokens.add(tk);
        }

        Token[] arr = new Token[tokens.size()];
        tokens.toArray(arr);

        return arr;
    }
}
