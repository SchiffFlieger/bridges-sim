package de.karstenkoehler.bridges.io;

import de.karstenkoehler.bridges.model.Edge;
import de.karstenkoehler.bridges.model.Node;

import java.util.List;
import java.util.Map;

public class ParseResult {
    private final Map<Integer, Node> islands;
    private final List<Edge> bridges;
    private final int width;
    private final int height;

    public ParseResult(Map<Integer, Node> islands, List<Edge> bridges, int width, int height) {
        this.islands = islands;
        this.bridges = bridges;
        this.width = width;
        this.height = height;
    }

    public Map<Integer, Node> getIslands() {
        return islands;
    }

    public List<Edge> getBridges() {
        return bridges;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
