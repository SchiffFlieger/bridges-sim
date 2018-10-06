package de.karstenkoehler.bridges.io.parser.token;

import de.karstenkoehler.bridges.io.parser.ParseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenizerImpl {

    private String[] chars;
    private int pos;

    private final Matcher numMatcher;
    private final Matcher boolMatcher;
    private final Matcher whitespace;

    public TokenizerImpl(final String input) {
        this.chars = input.split("");
        this.pos = 0;

        this.numMatcher = Pattern.compile("[0-9]").matcher("");
        this.boolMatcher = Pattern.compile("[tf]").matcher("");
        this.whitespace = Pattern.compile("\\s").matcher("");
    }

    public Token next() throws ParseException {
        while (pos < chars.length) {
            String current = chars[pos];

            if (whitespace.reset(current).matches()) {
                consume(current);
            } else if (current.equals("#")) {
                skipComment();
            } else if (current.equals("(")) {
                consume(current);
                return new Token("(", Token.Type.OpenParenthesis);
            } else if (current.equals(")")) {
                consume(current);
                return new Token(")", Token.Type.CloseParenthesis);
            } else if (current.equals(",")) {
                consume(current);
                return new Token(",", Token.Type.Comma);
            } else if (current.equals("|")) {
                consume(current);
                return new Token("|", Token.Type.Pipe);
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
                throw new ParseException("found unknown character '" + current + "'");
            }
        }

        return new Token("", Token.Type.EOF);
    }

    private Token fieldToken() throws ParseException {
        consumeString("FIELD");
        return new Token("FIELD", Token.Type.FieldSection);
    }

    private Token islandsToken() throws ParseException {
        consumeString("ISLANDS");
        return new Token("ISLANDS", Token.Type.IslandSection);
    }

    private Token bridgesToken() throws ParseException {
        consumeString("BRIDGES");
        return new Token("BRIDGES", Token.Type.BridgesSection);
    }

    private Token numberToken() throws ParseException {
        StringBuilder builder = new StringBuilder();
        do {
            builder.append(chars[pos]);
            consume(chars[pos]);
        } while (pos < chars.length && chars[pos].matches("[0-9]"));

        return new Token(builder.toString(), Token.Type.Number);
    }

    private Token boolToken() throws ParseException {
        if (chars[pos].equals("t")) {
            consumeString("true");
            return new Token("true", Token.Type.Bool);
        } else {
            consumeString("false");
            return new Token("false", Token.Type.Bool);
        }
    }

    private void skipComment() throws ParseException {
        consume("#");
        while (pos < chars.length && !chars[pos].equals("\n") && !chars[pos].equals("\r")) {
            consume(chars[pos]);
        }
    }

    private void consumeString(String str) throws ParseException {
        for (String c : str.split("")) {
            consume(c);
        }
    }

    private void consume(String str) throws ParseException {
        if (chars[pos].equals(str)) {
            pos++;
            return;
        }
        throw new ParseException(String.format("expected: %s, found: %s", str, chars[pos]));
    }
}
