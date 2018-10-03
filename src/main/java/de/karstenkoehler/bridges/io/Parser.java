package de.karstenkoehler.bridges.io;

import de.karstenkoehler.bridges.model.Edge;
import de.karstenkoehler.bridges.model.Node;

import java.util.List;
import java.util.Map;

public interface Parser {
    void parse() throws ParseException;

    int getWidth();

    int getHeight();

    Map<Integer, Node> getIslands();

    List<Edge> getBridges();
}
