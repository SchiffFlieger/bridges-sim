package de.karstenkoehler.bridges.io.parser.token;

import java.util.Objects;

public class Token {
    public enum Type {
        FIELD_SECTION, ISLAND_SECTION, BRIDGES_SECTION,
        OPEN_PARENTHESIS, CLOSE_PARENTHESIS, COMMA, PIPE, X,
        NUMBER, BOOL, EOF
    }

    private final String value;
    private final Type type;

    public Token(String value, Type type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(value, token.value) &&
                type == token.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, type);
    }

    @Override
    public String toString() {
        return "Token{" +
                "value='" + value + '\'' +
                ", type=" + type +
                '}';
    }
}
