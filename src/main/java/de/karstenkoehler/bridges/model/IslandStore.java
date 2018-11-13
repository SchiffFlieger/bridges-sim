package de.karstenkoehler.bridges.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IslandStore {
    private final List<Island> asList;
    private final Map<Integer, Island> byId;
    private final Map<Integer, Map<Integer, Island>> byCoordinates;

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

    public Island getById(int id) {
        return this.byId.get(id);
    }

    public Island getByCoordinates(int x, int y) {
        if (!this.byCoordinates.containsKey(x)) {
            return null;
        }
        return this.byCoordinates.get(x).get(y);
    }

    public List<Island> getAsList() {
        return this.asList;
    }
}
