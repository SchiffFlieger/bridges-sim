package de.karstenkoehler.bridges.io;

import de.karstenkoehler.bridges.model.Edge;
import de.karstenkoehler.bridges.model.Node;

import java.util.*;

public class DefaultBridgesParser implements Parser {

    private final Scanner scanner;
    private final Map<Integer, Node> islands;
    private final List<Edge> bridges;
    private int width;
    private int height;

    public DefaultBridgesParser(final String input) {
        this.scanner = new Scanner(input);
        this.islands = new HashMap<>();
        this.bridges = new ArrayList<>();
    }

    @Override
    public void parse() throws ParseException {
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

        width = Integer.parseInt(size[0].trim());
        height = Integer.parseInt(size[1].trim());
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
            this.islands.put(i - 3, new Node(i - 3, x, y, requiredBridges));
        }
        index++;

        if (this.islands.size() != islandCount) {
            throw new ParseException(String.format("found %d islands. expected %d", islands.size(), islandCount));
        }

        if (index >= lines.size()) {
            return; // no bridges defined
        }

        if (!lines.get(index).equals("BRIDGES")) {
            throw new ParseException("BRIDGES section missing");
        }
        index++;

        for (int i = index; i < lines.size(); i++) {
            String line = lines.get(i).replaceAll("\\(", "").replaceAll("\\)", "");
            String[] tmp2 = line.split("\\|");
            String[] coords = tmp2[0].split(",");

            int node1 = Integer.parseInt(coords[0].trim());
            int node2 = Integer.parseInt(coords[1].trim());
            int existingBridges = Boolean.parseBoolean(tmp2[1].trim()) ? 2 : 1;
            this.bridges.add(new Edge(node1, node2, existingBridges));
        }
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public Map<Integer, Node> getIslands() {
        return this.islands;
    }

    @Override
    public List<Edge> getBridges() {
        return this.bridges;
    }
}
