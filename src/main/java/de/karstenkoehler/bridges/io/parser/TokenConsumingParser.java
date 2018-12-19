package de.karstenkoehler.bridges.io.parser;

import de.karstenkoehler.bridges.io.parser.token.Token;
import de.karstenkoehler.bridges.io.parser.token.Tokenizer;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.Island;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of the {@link Parser} interface based on a {@link Tokenizer}. It reads tokens from the tokenizer
 * and uses the tokens as indicators for creating the puzzle.
 * <p>
 * This implementation makes use of the following EBNF-specification. All the private methods are basically derived
 * from that specification. <p>
 *
 * <code>
 * start   := field, islands, [ connections ]; <br>
 * field   := 'FIELD', number, 'x', number, '|', number; <br>
 * islands := 'ISLANDS', { island }; <br>
 * connections := 'BRIDGES', { bridge }; <br>
 * island  := '(', number, ',', number, '|', number, ')'; <br>
 * bridge  := '(', number, ',', number, '|', ( 'true' | 'false' ), ')'; <br>
 * </code>
 */
public class TokenConsumingParser extends AbstractTokenParser implements Parser {

    private final List<Island> islands = new ArrayList<>();
    private final List<Connection> connections = new ArrayList<>();
    private int width, height, islandCount;

    /**
     * @param tokenizer the tokenizer to read from
     */
    public TokenConsumingParser(Tokenizer tokenizer) {
        super(tokenizer);
    }

    /**
     * @see Parser#parse()
     */
    @Override
    public BridgesPuzzle parse() throws ParseException {
        start();
        consume(Token.Type.EOF);
        return new BridgesPuzzle(islands, connections, width, height);
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
        this.islands.add(new Island(id, x, y, bridges));

        consume(Token.Type.CLOSE_PARENTHESIS);
    }

    private void bridges() throws ParseException {
        if (token().getType() != Token.Type.BRIDGES_SECTION) {
            return;
        }

        consume(Token.Type.BRIDGES_SECTION);
        while (token().getType() == Token.Type.OPEN_PARENTHESIS) {
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

        if (node1 >= islandCount || node2 >= islandCount) {
            throw new ParseException("A bridge references unknown island.");
        }

        this.connections.add(new Connection(islands.get(node1), islands.get(node2), bridges));

        consume(Token.Type.CLOSE_PARENTHESIS);
    }

    private int readNumberToken() throws ParseException {
        Token tk = token();
        consume(Token.Type.NUMBER);
        return Integer.parseInt(tk.getValue());
    }

    private boolean readBoolToken() throws ParseException {
        Token tk = token();
        consume(Token.Type.BOOL);
        return Boolean.parseBoolean(tk.getValue());
    }
}
