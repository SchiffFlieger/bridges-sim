package de.karstenkoehler.bridges.io.parser;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.parser.token.Token;
import de.karstenkoehler.bridges.io.parser.token.Tokenizer;
import de.karstenkoehler.bridges.model.Edge;
import de.karstenkoehler.bridges.model.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenConsumingParser extends AbstractTokenParser implements Parser {

    private final Map<Integer, Node> islands = new HashMap<>();
    private final List<Edge> bridges = new ArrayList<>();
    private int width, height, islandCount;

    public TokenConsumingParser(Tokenizer tokenizer) {
        super(tokenizer);
    }

    @Override
    public ParseResult parse() throws ParseException {
        start();
        check(Token.Type.EOF);
        return new ParseResult(islands, bridges, width, height);
    }

    private void start() throws ParseException {
        field();
        islands();
        bridges();
    }

    private void field() throws ParseException {
        consume(Token.Type.FIELD_SECTION);

        this.width = readNumberToken();
        consume(Token.Type.X);
        this.height = readNumberToken();
        consume(Token.Type.PIPE);
        this.islandCount = readNumberToken();
    }


    private void islands() throws ParseException {
        consume(Token.Type.ISLAND_SECTION);

        for (int i = 0; i < this.islandCount; i++) {
            island();
        }
    }

    private void island() throws ParseException {
        consume(Token.Type.OPEN_PARENTHESIS);

        int x = readNumberToken();
        consume(Token.Type.COMMA);
        int y = readNumberToken();
        consume(Token.Type.PIPE);
        int bridges = readNumberToken();

        int id = this.islands.size();
        this.islands.put(id, new Node(id, x, y, bridges));

        consume(Token.Type.CLOSE_PARENTHESIS);
    }

    private void bridges() throws ParseException {
        if (current.getType() != Token.Type.BRIDGES_SECTION) {
            return;
        }

        consume(Token.Type.BRIDGES_SECTION);
        while (current.getType() == Token.Type.OPEN_PARENTHESIS) {
            bridge();
        }
    }

    private void bridge() throws ParseException {
        consume(Token.Type.OPEN_PARENTHESIS);

        int node1 = readNumberToken();
        consume(Token.Type.COMMA);
        int node2 = readNumberToken();
        consume(Token.Type.PIPE);
        int bridges = readBoolToken() ? 2 : 1; // double or single bridge

        int id = this.bridges.size();
        this.bridges.add(new Edge(id, node1, node2, bridges));

        consume(Token.Type.CLOSE_PARENTHESIS);
    }

    private int readNumberToken() throws ParseException {
        check(Token.Type.NUMBER);
        int val = Integer.parseInt(token().getValue());
        consume(Token.Type.NUMBER);
        return val;
    }

    private boolean readBoolToken() throws ParseException {
        check(Token.Type.BOOL);
        boolean val = Boolean.parseBoolean(token().getValue());
        consume(Token.Type.BOOL);
        return val;
    }
}
