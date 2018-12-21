package de.karstenkoehler.bridges.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A container class for storing the islands of a {@link BridgesPuzzle}. It offers several methods for
 * efficiently requesting island references by specific parameters.
 */
public class IslandStore {
    private final List<Island> asList;
    private final Map<Integer, Island> byId;
    private final Map<Integer, Map<Integer, Island>> byCoordinates;

    /**
     * Creates a new island store.
     *
     * @param islands the islands to store
     */
    public IslandStore(List<Island> islands) {
        this.asList = islands;
        this.byId = new HashMap<>();
        this.byCoordinates = new HashMap<>();

        for (Island island : islands) {
            byId.put(island.getId(), island);

            if (!this.byCoordinates.containsKey(island.getX())) {
                this.byCoordinates.put(island.getX(), new HashMap<>());
            }

            this.byCoordinates.get(island.getX()).put(island.getY(), island);
        }
    }

    /**
     * Returns the island with the given id.
     *
     * @param id the id of the island
     * @return the island with the given id
     */
    public Island getById(int id) {
        return this.byId.get(id);
    }

    /**
     * Returns the island with the given coordinates.
     *
     * @param x the x coordinate of the island
     * @param y the y coordinate of the island
     * @return the island with the given coordinates
     */
    public Island getByCoordinates(int x, int y) {
        if (!this.byCoordinates.containsKey(x)) {
            return null;
        }
        return this.byCoordinates.get(x).get(y);
    }

    /**
     * Returns a list of all islands.
     *
     * @return a list of all islands
     */
    public List<Island> getAsList() {
        return this.asList;
    }
}
