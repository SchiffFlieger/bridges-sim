package de.karstenkoehler.bridges.test.parser;

import de.karstenkoehler.bridges.io.parser.ParseException;
import de.karstenkoehler.bridges.io.parser.token.Token;
import de.karstenkoehler.bridges.io.parser.token.TokenizerImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TokenizerLookAheadTest {

    private static final String in = "ISLANDS\n( 0, 1 | 2 )";
    private static final Token[] expected = new Token[]{
            new Token("ISLANDS", Token.Type.IslandSection),
            new Token("(", Token.Type.OpenParenthesis),
            new Token("0", Token.Type.Number),
            new Token(",", Token.Type.Comma),
            new Token("1", Token.Type.Number),
            new Token("|", Token.Type.Pipe),
            new Token("2", Token.Type.Number),
            new Token(")", Token.Type.CloseParenthesis),
            new Token("", Token.Type.EOF),
    };

    @Test
    public void testPeekFirst() throws ParseException {
        TokenizerImpl tokenizer = new TokenizerImpl(in);

        Token peek = tokenizer.peekNext();
        Token next = tokenizer.next();

        assertEquals(expected[0], peek);
        assertEquals(expected[0], next);
    }

    @Test
    public void testDoublePeek() throws ParseException {
        TokenizerImpl tokenizer = new TokenizerImpl(in);

        Token peek1 = tokenizer.peekNext();
        Token peek2 = tokenizer.peekNext();

        assertEquals(peek1, peek2);
    }

    @Test
    public void testPeekNextPeek() throws ParseException {
        TokenizerImpl tokenizer = new TokenizerImpl(in);

        Token peek1 = tokenizer.peekNext();
        Token next1 = tokenizer.next();
        Token peek2 = tokenizer.peekNext();
        Token next2 = tokenizer.next();

        assertNotEquals(peek1, peek2);
        assertNotEquals(next1, next2);
    }


    @Test
    public void testPeekEof() throws ParseException {
        TokenizerImpl tokenizer = new TokenizerImpl(in);

        // discard all tokens
        for (Token tk = tokenizer.next(); tk.getType() != Token.Type.EOF; tk = tokenizer.next()) ;

        Token peek = tokenizer.peekNext();
        Token next = tokenizer.next();

        assertEquals(expected[8], peek);
        assertEquals(expected[8], next);
    }
}
