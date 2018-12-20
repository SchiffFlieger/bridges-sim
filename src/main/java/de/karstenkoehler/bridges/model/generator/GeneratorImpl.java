package de.karstenkoehler.bridges.model.generator;

import de.karstenkoehler.bridges.io.validator.ValidateException;
import de.karstenkoehler.bridges.io.validator.Validator;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.Island;
import de.karstenkoehler.bridges.model.PuzzleSpecification;

import java.util.*;

/**
 * An implementation of the {@link Generator} interface. A simple algorithm is used to create the puzzles. The first
 * island is randomly placed on the field. Then one of the existing islands is chosen randomly and a new island is
 * placed either horizontally or vertically. The new island is connected with a bridge, taking care that no connections
 * are crossing. This step repeats until the desired number of islands is reached.
 * <p>
 * If no new island can be added during a step, an attempt is made to split an existing bridge. This creates a new
 * island in the middle of this bridge. If this is not possible, all existing islands are discarded and the
 * generator starts from scratch.
 */
public class GeneratorImpl implements Generator {
    private static final Random random = new Random(System.nanoTime());

    private final Validator validator;
    private final List<Island> islands;
    private final List<Connection> connections;
    private final Counter counter;

    /**
     * @param validator a validator to check if the generated puzzles are correct
     */
    public GeneratorImpl(Validator validator) {
        this.validator = validator;
        this.islands = new ArrayList<>();
        this.connections = new ArrayList<>();
        this.counter = new Counter();
    }

    /**
     * @see Generator#generate(PuzzleSpecification)
     */
    @Override
    public BridgesPuzzle generate(PuzzleSpecification spec) {
        while (true) {
            try {
                // The generateNewPuzzle-method still has a bug that allows the
                // last set bridge to cross other islands/connections, therefore another
                // validation step is needed.
                BridgesPuzzle puzzle = generateNewPuzzle(spec);
                if (isInvalid(puzzle)) {
                    continue;
                }
                if (!spec.generateSolution()) {
                    puzzle.restart();
                }
                return puzzle;
            } catch (RetryException ignored) {
            }
        }
    }

    /**
     * Generates a random puzzle from the given specification.
     *
     * @param spec the specification for the generated puzzle
     * @return a randomly generated puzzle
     * @throws RetryException if the generator got stuck and wants to restart from scratch
     */
    private BridgesPuzzle generateNewPuzzle(PuzzleSpecification spec) throws RetryException {
        this.resetGenerator(spec);
        BridgesPuzzle puzzle = new BridgesPuzzle(getIslandList(), getBridgeList(), spec.getWidth(), spec.getHeight());

        while (this.islands.size() < spec.getIslandCount()) {
            if (counter.shouldReset()) {
                resetGenerator(spec);
            }
            if (counter.shouldSplit()) {
                Connection longest = getLongestBridge();
                if (longest != null) {
                    addIslandInExistingBridge(longest);
                }

                continue;
            }

            puzzle = tryNextIsland(spec);
        }

        puzzle.fillMissingConnections();
        return puzzle;
    }

    /**
     * Tries to add a new island to the existing puzzle. To do so, this method randomly selects an existing
     * island and connects a new island with a bridge to it.
     *
     * @param spec the specification for the generated puzzle
     * @return the generated puzzle
     */
    private BridgesPuzzle tryNextIsland(PuzzleSpecification spec) throws RetryException {
        Island start = selectRandomIsland();
        Island newIsland = generateAdjacentIsland(start, spec.getWidth(), spec.getHeight());
        Connection bridge = newRandomBridge(start, newIsland);

        this.islands.add(newIsland);
        this.connections.add(bridge);

        setRequiredBridgesForAllIslands();
        BridgesPuzzle puzzle = new BridgesPuzzle(getIslandList(), getBridgeList(), spec.getWidth(), spec.getHeight());
        puzzle.fillMissingConnections();

        if (isInvalid(puzzle)) {
            this.islands.remove(newIsland);
            this.connections.remove(bridge);
            counter.incrementSplitCounter();
        } else {
            counter.resetCounters();
        }
        return puzzle;
    }

    private Connection getLongestBridge() {
        Optional<Connection> opt = connections.stream().max(Comparator.comparingInt(Connection::getLength));
        if (!opt.isPresent()) {
            return null;
        }
        Connection longest = opt.get();
        if (longest.getLength() < 4) {
            return null;
        }
        return longest;
    }

    /**
     * This method takes an existing bridge between two islands and splits it up. A new island is created
     * somewhere in the path of the bridge, creating two new connections.
     *
     * @param longest the bridge to split
     */
    private void addIslandInExistingBridge(Connection longest) throws RetryException {
        this.connections.remove(longest);
        Island start = longest.getStartIsland();
        Island end = longest.getEndIsland();

        int minX = Math.min(start.getX(), end.getX());
        int minY = Math.min(start.getY(), end.getY());

        if (longest.isHorizontal()) {
            Island newIsland = createNewValidIslandHorizontal(longest, minX, minY);
            this.islands.add(newIsland);
            this.connections.add(newRandomBridge(start, newIsland));
            this.connections.add(newRandomBridge(newIsland, end));
        } else {
            Island newIsland = createNewValidIslandVertical(longest, minX, minY);
            this.islands.add(newIsland);
            this.connections.add(newRandomBridge(start, newIsland));
            this.connections.add(newRandomBridge(newIsland, end));
        }
    }

    private Island createNewValidIslandHorizontal(Connection longest, int minX, int minY) throws RetryException {
        int counter = 0;
        Island newIsland = new Island(0, minX + random.nextInt(longest.getLength()), minY, 0);
        while (isForbidden(newIsland)) {
            newIsland = new Island(0, minX + random.nextInt(longest.getLength()), minY, 0);
            counter++;
            if (counter > 1000) {
                throw new RetryException();
            }
        }
        return newIsland;
    }

    private Island createNewValidIslandVertical(Connection longest, int minX, int minY) throws RetryException {
        int counter = 0;
        Island newIsland = new Island(0, minX, minY + random.nextInt(longest.getLength()), 0);
        while (isForbidden(newIsland)) {
            newIsland = new Island(0, minX, minY + random.nextInt(longest.getLength()), 0);
            counter++;
            if (counter > 1000) {
                throw new RetryException();
            }
        }
        return newIsland;
    }

    private boolean isForbidden(Island island) {
        return this.islands.stream().anyMatch(other -> Math.abs(island.getX() - other.getX()) + Math.abs(island.getY() - other.getY()) <= 1);
    }

    private void resetGenerator(PuzzleSpecification spec) {
        counter.resetCounters();
        this.islands.clear();
        this.connections.clear();
        this.islands.add(newRandomIsland(spec.getWidth(), spec.getHeight()));
    }

    /**
     * Iterates through all existing islands and updates their required bridge count.
     */
    private void setRequiredBridgesForAllIslands() {
        this.islands.forEach(island -> island.setRequiredBridges(0));

        for (Connection connection : this.connections) {
            connection.getStartIsland().setRequiredBridges(connection.getStartIsland().getRequiredBridges() + connection.getBridgeCount());
            connection.getEndIsland().setRequiredBridges(connection.getEndIsland().getRequiredBridges() + connection.getBridgeCount());
        }
    }

    /**
     * To create a valid {@link BridgesPuzzle} you need a list of islands that matches specific conditions
     * like being sorted by coordinate and having appropriate ids. Because in this implementation new
     * islands are created randomly without any order, we need to prepare the existing list to match those
     * conditions.
     *
     * @return the prepared list of all existing islands
     */
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

    /**
     * To create a valid {@link BridgesPuzzle} you need a list of connections that matches specific conditions
     * like beeing sorted by island id and having appropriate ids themselves. Because in this implementation new
     * connections are created randomly without any order, we need to prepare the existing list to match those
     * conditions.
     *
     * @return the prepared list of all existing connections
     */
    private List<Connection> getBridgeList() {
        List<Connection> result = new ArrayList<>(this.connections.size());

        for (Connection connection : this.connections) {
            Island a = connection.getStartIsland();
            Island b = connection.getEndIsland();
            if (a.getId() > b.getId()) {
                result.add(new Connection(b, a, connection.getBridgeCount()));
            } else {
                result.add(new Connection(a, b, connection.getBridgeCount()));
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
            return puzzle.getConnections().stream().anyMatch(bridge -> !bridge.isValid());
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
    private Island generateAdjacentIsland(Island island, int width, int height) throws RetryException {
        int counter = 0;
        Island newIsland = random.nextBoolean() ? newRandomHorizontalIsland(island, width) : newRandomVerticalIsland(island, height);
        while (isForbidden(newIsland)) {
            newIsland = random.nextBoolean() ? newRandomHorizontalIsland(island, width) : newRandomVerticalIsland(island, height);
            counter++;
            if (counter > 200) {
                throw new RetryException();
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
    private Connection newRandomBridge(Island start, Island end) {
        return new Connection(start, end, random.nextInt(2) + 1);
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

    private static class RetryException extends Exception {
    }
}
