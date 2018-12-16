package de.karstenkoehler.bridges.io.parser.token;

import de.karstenkoehler.bridges.io.parser.ParseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A basic implementation of the {@link Tokenizer} interface. The input string is passed as a
 * constructor parameter. For every input string to tokenize, a new instance of this class has to be
 * instantiated.
 */
public class TokenizerImpl implements Tokenizer {

    private String[] chars;
    private int pos;

    private final Matcher numMatcher;
    private final Matcher boolMatcher;
    private final Matcher whitespace;

    /**
     * @param input the string to generate tokens from
     */
    public TokenizerImpl(final String input) {
        this.chars = input.split("");
        this.pos = 0;

        this.numMatcher = Pattern.compile("[0-9]").matcher("");
        this.boolMatcher = Pattern.compile("[tf]").matcher("");
        this.whitespace = Pattern.compile("\\s").matcher("");
    }

    /**
     * @see Tokenizer#next()
     */
    @Override
    public Token next() throws ParseException {
        while (pos < chars.length) {
            String current = chars[pos];

            if (whitespace.reset(current).matches()) {
                consume(current);
            } else if (current.equals("#")) {
                skipComment();
            } else if (current.equals("(")) {
                consume(current);
                return new Token("(", Token.Type.OPEN_PARENTHESIS);
            } else if (current.equals(")")) {
                consume(current);
                return new Token(")", Token.Type.CLOSE_PARENTHESIS);
            } else if (current.equals(",")) {
                consume(current);
                return new Token(",", Token.Type.COMMA);
            } else if (current.equals("|")) {
                consume(current);
                return new Token("|", Token.Type.PIPE);
            } else if (current.equals("x")) {
                consume(current);
                return new Token("x", Token.Type.X);
            } else if (current.equals("F")) {
                return fieldToken();
            } else if (current.equals("I")) {
                return islandsToken();
            } else if (current.equals("B")) {
                return bridgesToken();
            } else if (numMatcher.reset(current).matches()) {
                return numberToken();
            } else if (boolMatcher.reset(current).matches()) {
                return boolToken();
            } else {
                throw new ParseException("Found unknown character '" + current + "'.");
            }
        }

        return new Token("", Token.Type.EOF);
    }

    /**
     * Tries to read a field token. A field token is exactly the string 'FIELD' (case sensitive).
     *
     * @return the created field token
     * @throws ParseException if there are unexpected characters
     */
    private Token fieldToken() throws ParseException {
        consumeString("FIELD");
        return new Token("FIELD", Token.Type.FIELD_SECTION);
    }

    /**
     * Tries to read an islands token. An islands token is exactly the string 'ISLANDS' (case sensitive).
     * @return the created islands token
     * @throws ParseException if there are unexpected characters
     */
    private Token islandsToken() throws ParseException {
        consumeString("ISLANDS");
        return new Token("ISLANDS", Token.Type.ISLAND_SECTION);
    }

    /**
     * Tries to read a bridges token. A bridges token is exactly the string 'BRIDGES' (case sensitive).
     * @return the created bridges token
     * @throws ParseException if there are unexpected characters
     */
    private Token bridgesToken() throws ParseException {
        consumeString("BRIDGES");
        return new Token("BRIDGES", Token.Type.BRIDGES_SECTION);
    }

    /**
     * Tries to read a number token. A number token consists only of digits.
     * @return the created number token
     * @throws ParseException if there are unexpected characters
     */
    private Token numberToken() throws ParseException {
        StringBuilder builder = new StringBuilder();
        do {
            builder.append(chars[pos]);
            consume(chars[pos]);
        } while (pos < chars.length && chars[pos].matches("[0-9]"));

        return new Token(builder.toString(), Token.Type.NUMBER);
    }

    /**
     * Tries to read a boolean token. A boolean token is one of the strings 'true' or 'false' (case sensitive).
     * @return the created boolean token
     * @throws ParseException if there are unexpected characters
     */
    private Token boolToken() throws ParseException {
        if (chars[pos].equals("t")) {
            consumeString("true");
            return new Token("true", Token.Type.BOOL);
        } else {
            consumeString("false");
            return new Token("false", Token.Type.BOOL);
        }
    }

    /**
     * Consumes every character until it reaches a new line that does not start with the # character.
     * @throws ParseException should not be thrown in this method
     */
    private void skipComment() throws ParseException {
        consume("#");
        while (pos < chars.length && !chars[pos].equals("\n") && !chars[pos].equals("\r")) {
            consume(chars[pos]);
        }
    }

    /**
     * Consumes multiple characters at once. Therefore it calls the {@link TokenizerImpl#consume(String)} method
     * multiple times.
     * @param str the string to consume
     * @throws ParseException if the string mismatched the next actual characters
     */
    private void consumeString(String str) throws ParseException {
        for (String c : str.split("")) {
            consume(c);
        }
    }

    /**
     * If the character at the current position matches the given character this method moves the current
     * position one ahead.
     * @param str the character to consume
     * @throws ParseException if the actual character does not match the expected character
     */
    private void consume(String str) throws ParseException {
        if (chars[pos].equals(str)) {
            pos++;
            return;
        }
        throw new ParseException(String.format("expected: %s, found: %s", str, chars[pos]));
    }
}
