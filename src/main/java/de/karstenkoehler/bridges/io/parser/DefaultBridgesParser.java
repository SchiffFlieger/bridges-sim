package de.karstenkoehler.bridges.io.parser;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.model.Edge;
import de.karstenkoehler.bridges.model.Node;

import java.util.*;

public class DefaultBridgesParser implements Parser {

    @Override
    public ParseResult parse(final String input) throws ParseException {
        final Scanner scanner = new Scanner(input);
        final Map<Integer, Node> islands = new HashMap<>();
        final List<Edge> bridges = new ArrayList<>();

        List<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.startsWith("#") && !line.isEmpty()) {
                lines.add(line);
            }
        }

        if (!lines.get(0).equals("FIELD")) {
            throw new ParseException("FIELD section missing");
        }

        String[] tmp = lines.get(1).split("\\|");
        String[] size = tmp[0].split("x");

        final int width = Integer.parseInt(size[0].trim());
        final int height = Integer.parseInt(size[1].trim());
        int islandCount = Integer.parseInt(tmp[1].trim());

        if (!lines.get(2).equals("ISLANDS")) {
            throw new ParseException("ISLANDS section missing");
        }

        int index = 0;
        for (int i = 3; i < lines.size() && !lines.get(i).equals("BRIDGES"); i++) {
            index = i;

            String line = lines.get(i).replaceAll("\\(", "").replaceAll("\\)", "");
            String[] tmp2 = line.split("\\|");
            String[] coords = tmp2[0].split(",");

            int x = Integer.parseInt(coords[0].trim());
            int y = Integer.parseInt(coords[1].trim());
            int requiredBridges = Integer.parseInt(tmp2[1].trim());
            islands.put(i - 3, new Node(i - 3, x, y, requiredBridges));
        }
        index++;

        // TODO this should be a validate exception
        if (islands.size() != islandCount) {
            throw new ParseException(String.format("found %d islands. expected %d", islands.size(), islandCount));
        }

        if (index >= lines.size()) {
            return new ParseResult(islands, bridges, width, height); // no bridges defined
        }

        if (!lines.get(index).equals("BRIDGES")) {
            throw new ParseException("BRIDGES section missing");
        }
        index++;

        for (int bridgeIndex = 0; bridgeIndex + index < lines.size(); bridgeIndex++) {
            int i = bridgeIndex + index;
            String line = lines.get(i).replaceAll("\\(", "").replaceAll("\\)", "");
            String[] tmp2 = line.split("\\|");
            String[] coords = tmp2[0].split(",");

            int node1 = Integer.parseInt(coords[0].trim());
            int node2 = Integer.parseInt(coords[1].trim());
            int existingBridges = Boolean.parseBoolean(tmp2[1].trim()) ? 2 : 1;
            bridges.add(new Edge(bridgeIndex, node1, node2, existingBridges));
        }

        return new ParseResult(islands, bridges, width, height);
    }
}
