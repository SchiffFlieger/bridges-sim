package de.karstenkoehler.bridges.model.generator;

import de.karstenkoehler.bridges.io.validator.ValidateException;
import de.karstenkoehler.bridges.io.validator.Validator;
import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Island;
import de.karstenkoehler.bridges.model.PuzzleSpecification;

import java.util.*;

public class GeneratorImpl implements Generator {
    private static final Random random = new Random(System.nanoTime());
    private static final int TRIES_BEFORE_RESET = 150;

    private final Validator validator;
    private final List<Island> islands;
    private final List<Bridge> bridges;
    private int resetCounter;

    public GeneratorImpl(Validator validator) {
        this.validator = validator;
        islands = new ArrayList<>();
        bridges = new ArrayList<>();
    }

    @Override
    public BridgesPuzzle generate(PuzzleSpecification spec) {
        BridgesPuzzle puzzle = new BridgesPuzzle(getIslandMap(), this.bridges, spec.getWidth(), spec.getHeight());
        this.islands.add(newRandomIsland(spec.getWidth(), spec.getHeight()));

        while (this.islands.size() < spec.getIslandCount()) {
            if (resetCounter > TRIES_BEFORE_RESET) {
                this.resetCounter = 0;
                this.islands.clear();
                this.bridges.clear();
                this.islands.add(newRandomIsland(spec.getWidth(), spec.getHeight()));
            }

            Island start = selectRandomIsland();
            Island end = generateAdjacentIsland(start, spec.getWidth(), spec.getHeight());
            Bridge bridge = newRandomBridge(start, end);

            this.islands.add(end);
            this.bridges.add(bridge);

            setRequiredBridgesForAllIslands();
            puzzle = new BridgesPuzzle(getIslandMap(), getBridgeList(), spec.getWidth(), spec.getHeight());
            puzzle.fillMissingBridges();
            if (!isValid(puzzle)) {
                this.islands.remove(end.getId());
                this.bridges.remove(bridge);
                this.resetCounter++;
            } else {
                this.resetCounter = 0;
            }
        }

        puzzle.fillMissingBridges();
        if (!spec.generateSolution()) {
            puzzle.restart();
        }
        return puzzle;
    }

    private void setRequiredBridgesForAllIslands() {
        this.islands.forEach(island -> island.setRequiredBridges(0));

        for (Bridge bridge : this.bridges) {
            bridge.getStartIsland().setRequiredBridges(bridge.getStartIsland().getRequiredBridges() + bridge.getBridgeCount());
            bridge.getEndIsland().setRequiredBridges(bridge.getEndIsland().getRequiredBridges() + bridge.getBridgeCount());
        }
    }

    private Map<Integer, Island> getIslandMap() {
        this.islands.sort((o1, o2) -> {
            int diff = o1.getX() - o2.getX();
            if (diff != 0) {
                return diff;
            }

            return o1.getY() - o2.getY();
        });

        Map<Integer, Island> map = new HashMap<>();
        for (int i = 0; i < islands.size(); i++) {
            islands.get(i).setId(i);
            map.put(i, islands.get(i));
        }
        return map;
    }

    private List<Bridge> getBridgeList() {
        List<Bridge> bridges = new ArrayList<>(this.bridges.size());

        for (Bridge bridge : this.bridges) {
            Island a = bridge.getStartIsland();
            Island b = bridge.getEndIsland();
            if (a.getId() > b.getId()) {
                bridges.add(new Bridge(b, a, bridge.getBridgeCount()));
            } else {
                bridges.add(new Bridge(a, b, bridge.getBridgeCount()));
            }
        }

        bridges.sort((o1, o2) -> {
            int diff = o1.getStartIsland().getId() - o2.getStartIsland().getId();
            if (diff != 0) {
                return diff;
            }

            return o1.getEndIsland().getId() - o2.getEndIsland().getId();
        });

        return bridges;
    }

    private boolean isValid(BridgesPuzzle puzzle) {
        try {
            this.validator.validate(puzzle);
            puzzle.markInvalidBridges();
            return puzzle.getBridges().stream().allMatch(Bridge::isValid);
        } catch (ValidateException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Generates a random island based on the given island. The new island is aligned either horizontally
     * or vertically to the given island.
     *
     * @param island the base island
     * @param width  width of the puzzle
     * @param height height of the puzzle
     * @return an island at a random location
     */
    private Island generateAdjacentIsland(Island island, int width, int height) {
        // TODO currently bridges cross islands very often, maybe optimize that?
        if (random.nextDouble() > 0.5) {
            return newRandomHorizonzalIsland(island, width);
        } else {
            return newRandomVerticalIsland(island, height);
        }
    }

    private Island newRandomHorizonzalIsland(Island island, int width) {
        if (island.getX() <= 1) { // island at left border
            return new Island(nextIslandId(), intBetween(island.getX() + 2, width - 1), island.getY(), 0);
        } else if (width - island.getX() <= 2) { // island at right border
            return new Island(nextIslandId(), intBetween(0, island.getX() - 2), island.getY(), 0);
        }

        if (random.nextDouble() > 0.5) { // place new island on left side
            return new Island(nextIslandId(), intBetween(0, island.getX() - 2), island.getY(), 0);
        } else { // place new island on right side
            return new Island(nextIslandId(), intBetween(island.getX() + 2, width - 1), island.getY(), 0);
        }
    }

    private Island newRandomVerticalIsland(Island island, int height) {
        if (island.getY() <= 1) { // island at top border
            return new Island(nextIslandId(), island.getX(), intBetween(island.getY() + 2, height - 1), 0);
        } else if (height - island.getY() <= 2) { // island at bottom border
            return new Island(nextIslandId(), island.getX(), intBetween(0, island.getY() - 2), 0);
        }

        if (random.nextDouble() > 0.5) { // place new island on top side
            return new Island(nextIslandId(), island.getX(), intBetween(0, island.getY() - 2), 0);
        } else { // place new island on bottom side
            return new Island(nextIslandId(), island.getX(), intBetween(island.getY() + 2, height - 1), 0);
        }
    }


    /**
     * Selects one of the existing islands at random.
     *
     * @return the selected island
     */
    private Island selectRandomIsland() {
        return this.islands.get(random.nextInt(this.islands.size()));
    }


    /**
     * Generates a bridge connection between two islands. The bridge count is chosen randomly.
     *
     * @param start the first island
     * @param end   the second island
     * @return a bridge between the islands
     */
    private Bridge newRandomBridge(Island start, Island end) {
        return new Bridge(start, end, random.nextInt(2) + 1);
    }

    /**
     * Generates a random island as a starting point.
     *
     * @param width  width of the puzzle
     * @param height height of the puzzle
     * @return an island at a random location
     */
    private Island newRandomIsland(int width, int height) {
        return new Island(nextIslandId(), random.nextInt(width), random.nextInt(height), 0);
    }

    private int nextIslandId() {
        return this.islands.size();
    }

    /**
     * Returns a random integer between (inclusive) the given values. The order of the parameters does not matter.
     * The lower value is used as the lower bound and the higher value is used as the upper bound.
     *
     * @param a an integer
     * @param b an integer
     * @return a random integer
     */
    private static int intBetween(int a, int b) {
        int min = Math.min(a, b);
        int max = Math.max(a, b);
        return min + random.nextInt(max - min + 1);
    }
}
