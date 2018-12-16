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

    private final Validator validator;
    private final List<Island> islands;
    private final List<Bridge> bridges;
    private final Counter counter;

    public GeneratorImpl(Validator validator) {
        this.validator = validator;
        this.islands = new ArrayList<>();
        this.bridges = new ArrayList<>();
        this.counter = new Counter();
    }

    @Override
    public BridgesPuzzle generate(PuzzleSpecification spec) {
        while (true) {
            try {
                BridgesPuzzle puzzle = generateNewPuzzle(spec);
                if (isInvalid(puzzle)) {
                    continue;
                }
                if (!spec.generateSolution()) {
                    puzzle.restart();
                }
                return puzzle;
            } catch (IndicatorException e) {
//                System.out.println("Indicator");
            }
        }
    }

    private BridgesPuzzle generateNewPuzzle(PuzzleSpecification spec) throws IndicatorException {
        this.resetGenerator(spec);
        BridgesPuzzle puzzle = new BridgesPuzzle(getIslandList(), getBridgeList(), spec.getWidth(), spec.getHeight());

        while (this.islands.size() < spec.getIslandCount()) {
            if (shouldReset()) {
                resetGenerator(spec);
            }
            if (shouldSplit()) {
                Bridge longest = getLongestBridge();
                if (longest != null) {
                    addIslandInExistingBridge(longest);
                }

                continue;
            }

            puzzle = tryNextIsland(spec);
        }

        puzzle.fillMissingBridges();
        return puzzle;
    }

    private BridgesPuzzle tryNextIsland(PuzzleSpecification spec) throws IndicatorException {
        Island start = selectRandomIsland();
        Island newIsland = generateAdjacentIsland(start, spec.getWidth(), spec.getHeight());
        Bridge bridge = newRandomBridge(start, newIsland);

        this.islands.add(newIsland);
        this.bridges.add(bridge);

        setRequiredBridgesForAllIslands();
        BridgesPuzzle puzzle = new BridgesPuzzle(getIslandList(), getBridgeList(), spec.getWidth(), spec.getHeight());
        puzzle.fillMissingBridges();

        if (isInvalid(puzzle)) {
            this.islands.remove(newIsland);
            this.bridges.remove(bridge);
            incrementSplitCounter();
        } else {
            this.resetCounters();
        }
        return puzzle;
    }

    private Bridge getLongestBridge() {
        Optional<Bridge> opt = bridges.stream().max(Comparator.comparingInt(Bridge::getLength));
        if (!opt.isPresent()) {
            return null;
        }
        Bridge longest = opt.get();
        if (longest.getLength() < 4) {
            return null;
        }
        return longest;
    }

    private void addIslandInExistingBridge(Bridge longest) throws IndicatorException {
        this.bridges.remove(longest);
        Island start = longest.getStartIsland();
        Island end = longest.getEndIsland();

        int minX = Math.min(start.getX(), end.getX());
        int minY = Math.min(start.getY(), end.getY());

        if (longest.isHorizontal()) {
            Island newIsland = createNewValidIslandHorizontal(longest, minX, minY);
            this.islands.add(newIsland);
            this.bridges.add(newRandomBridge(start, newIsland));
            this.bridges.add(newRandomBridge(newIsland, end));
        } else {
            Island newIsland = createNewValidIslandVertical(longest, minX, minY);
            this.islands.add(newIsland);
            this.bridges.add(newRandomBridge(start, newIsland));
            this.bridges.add(newRandomBridge(newIsland, end));
        }
    }

    private Island createNewValidIslandHorizontal(Bridge longest, int minX, int minY) throws IndicatorException {
        int counter = 0;
        Island newIsland = new Island(0, minX + random.nextInt(longest.getLength()), minY, 0);
        while (isForbidden(newIsland)) {
            newIsland = new Island(0, minX + random.nextInt(longest.getLength()), minY, 0);
            counter++;
            if (counter > 1000) {
                throw new IndicatorException();
            }
        }
        return newIsland;
    }

    private Island createNewValidIslandVertical(Bridge longest, int minX, int minY) throws IndicatorException {
        int counter = 0;
        Island newIsland = new Island(0, minX, minY + random.nextInt(longest.getLength()), 0);
        while (isForbidden(newIsland)) {
            newIsland = new Island(0, minX, minY + random.nextInt(longest.getLength()), 0);
            counter++;
            if (counter > 1000) {
                throw new IndicatorException();
            }
        }
        return newIsland;
    }

    private boolean isForbidden(Island island) {
        return this.islands.stream().anyMatch(other -> Math.abs(island.getX() - other.getX()) + Math.abs(island.getY() - other.getY()) <= 1);
    }

    private void resetGenerator(PuzzleSpecification spec) {
        this.resetCounters();
        this.islands.clear();
        this.bridges.clear();
        this.islands.add(newRandomIsland(spec.getWidth(), spec.getHeight()));
    }

    private void setRequiredBridgesForAllIslands() {
        this.islands.forEach(island -> island.setRequiredBridges(0));

        for (Bridge bridge : this.bridges) {
            bridge.getStartIsland().setRequiredBridges(bridge.getStartIsland().getRequiredBridges() + bridge.getBridgeCount());
            bridge.getEndIsland().setRequiredBridges(bridge.getEndIsland().getRequiredBridges() + bridge.getBridgeCount());
        }
    }

    private List<Island> getIslandList() {
        setRequiredBridgesForAllIslands();

        this.islands.sort((o1, o2) -> {
            int diff = o1.getX() - o2.getX();
            if (diff != 0) {
                return diff;
            }

            return o1.getY() - o2.getY();
        });

        for (int i = 0; i < this.islands.size(); i++) {
            this.islands.get(i).setId(i);
        }

        return this.islands;
    }

    private List<Bridge> getBridgeList() {
        List<Bridge> result = new ArrayList<>(this.bridges.size());

        for (Bridge bridge : this.bridges) {
            Island a = bridge.getStartIsland();
            Island b = bridge.getEndIsland();
            if (a.getId() > b.getId()) {
                result.add(new Bridge(b, a, bridge.getBridgeCount()));
            } else {
                result.add(new Bridge(a, b, bridge.getBridgeCount()));
            }
        }

        result.sort((o1, o2) -> {
            int diff = o1.getStartIsland().getId() - o2.getStartIsland().getId();
            if (diff != 0) {
                return diff;
            }

            return o1.getEndIsland().getId() - o2.getEndIsland().getId();
        });

        return result;
    }

    private boolean isInvalid(BridgesPuzzle puzzle) {
        try {
            this.validator.validate(puzzle);
            puzzle.markInvalidBridges();
            return puzzle.getBridges().stream().anyMatch(bridge -> !bridge.isValid());
        } catch (ValidateException e) {
            return true;
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
    private Island generateAdjacentIsland(Island island, int width, int height) throws IndicatorException {
        int counter = 0;
        Island newIsland = random.nextBoolean() ? newRandomHorizontalIsland(island, width) : newRandomVerticalIsland(island, height);
        while (isForbidden(newIsland)) {
            newIsland = random.nextBoolean() ? newRandomHorizontalIsland(island, width) : newRandomVerticalIsland(island, height);
            counter++;
            if (counter > 200) {
                throw new IndicatorException();
            }
        }
        return newIsland;
    }

    private Island newRandomHorizontalIsland(Island island, int width) {
        return new Island(0, random.nextInt(width), island.getY(), 0);
    }

    private Island newRandomVerticalIsland(Island island, int height) {
        return new Island(0, island.getX(), random.nextInt(height), 0);
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
        return new Island(0, random.nextInt(width), random.nextInt(height), 0);
    }

    private void resetCounters() {
        counter.resetCounters();
    }

    private boolean shouldSplit() {
        return counter.shouldSplit();
    }

    private boolean shouldReset() {
        return counter.shouldReset();
    }

    private void incrementSplitCounter() {
        counter.incrementSplitCounter();
    }

    private static class IndicatorException extends Exception {
    }
}
