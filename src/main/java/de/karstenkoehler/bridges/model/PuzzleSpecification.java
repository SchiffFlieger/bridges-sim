package de.karstenkoehler.bridges.model;

import de.karstenkoehler.bridges.model.generator.Generator;

import java.util.Random;

/**
 * This class stores the specification for creating a new random {@link BridgesPuzzle} using the
 * {@link Generator} interface. It offers no public constructor and thus can only be instantiated
 * by its static methods.
 */
public class PuzzleSpecification {
    private static final int MIN_SIZE = 4;
    private static final int MAX_SIZE = 25;

    private final boolean solution;
    private final int width;
    private final int height;
    private final int islandCount;

    private static final Random random = new Random(System.nanoTime());

    /**
     * Creates a new specification where the width, height and number of islands are chosen randomly.
     *
     * @param solution true if the generated bridges puzzle should be solved
     * @return a new specification instance
     */
    public static PuzzleSpecification random(boolean solution) {
        int width = intBetween(MIN_SIZE, MAX_SIZE);
        int height = intBetween(MIN_SIZE, MAX_SIZE);

        return new PuzzleSpecification(solution, width, height, randomIslandCount(width, height));
    }

    /**
     * Creates a new specification with the given width and height properties. The number of islands is chosen
     * randomly.
     *
     * @param solution true if the generated bridges puzzle should be solved
     * @param width    the width of the puzzle
     * @param height   the height of the puzzle
     * @return a new specification instance
     * @throws IllegalArgumentException if the given width and height are out of bounds
     */
    public static PuzzleSpecification withBounds(boolean solution, int width, int height) {
        checkBounds(width, height);

        return new PuzzleSpecification(solution, width, height, randomIslandCount(width, height));
    }

    /**
     * Creates a new specification with the given width, height and number of island properties.
     *
     * @param solution    true if the generated bridges puzzle should be solved
     * @param width       the width of the puzzle
     * @param height      the height of the puzzle
     * @param islandCount the number of islands in the puzzle
     * @return a new specification instance
     * @throws IllegalArgumentException if the given width and height are out of bounds or the number
     *                                  of islands does not fit the puzzle size
     */
    public static PuzzleSpecification withSpecs(boolean solution, int width, int height, int islandCount) {
        checkBounds(width, height);

        if (islandCount < 2 || islandCount > maxIslandCount(width, height)) {
            throw new IllegalArgumentException("Number of islands must be in range [2, (Width*Height) / 5]");
        }

        return new PuzzleSpecification(solution, width, height, islandCount);
    }

    /**
     * A private constructor, so that this class can only be instantiated by its static methods.
     */
    private PuzzleSpecification(boolean solution, int width, int height, int islandCount) {
        this.solution = solution;
        this.width = width;
        this.height = height;
        this.islandCount = islandCount;
    }

    /**
     * Returns true if the generated puzzle should be solved.
     *
     * @return true if the generated puzzle should be solved
     */
    public boolean generateSolution() {
        return this.solution;
    }

    /**
     * Returns the desired width of the puzzle to generate.
     *
     * @return the desired width of the puzzle to generate
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the desired height of the puzzle to generate.
     *
     * @return the desired height of the puzzle to generate
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the desired number of islands of the puzzle to generate.
     *
     * @return the desired number of islands of the puzzle to generate
     */
    public int getIslandCount() {
        return islandCount;
    }

    /**
     * Returns a random integer between {@param min} (inclusive) and {@param max} (inclusive).
     *
     * @param min lower bound
     * @param max upper bound
     * @return a random integer
     */
    private static int intBetween(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    /**
     * Returns a random island count for the given bounds. Also handles special cases.
     *
     * @param width  width of the puzzle
     * @param height height of the puzzle
     * @return a random integer
     */
    private static int randomIslandCount(int width, int height) {
        if (width == 4 && height == 4) { // 4x4 is a special case, default boundaries do not work
            return 4;
        }

        return intBetween(4, maxIslandCount(width, height));
    }

    /**
     * Returns the maximum number of allowed islands for a puzzle with given boundaries.
     *
     * @param width  width of the puzzle
     * @param height height of the puzzle
     * @return maximum number of islands
     */
    private static int maxIslandCount(int width, int height) {
        return (int) (width * height / 5.0);
    }

    /**
     * Checks if the given bounds are valid. Otherwise, throws an {@link IllegalArgumentException}.
     *
     * @param width  width of the puzzle
     * @param height height of the puzzle
     */
    private static void checkBounds(int width, int height) {
        if (width < MIN_SIZE || width > MAX_SIZE) {
            throw new IllegalArgumentException("Width must be in range [4, 25]");
        }
        if (height < MIN_SIZE || height > MAX_SIZE) {
            throw new IllegalArgumentException("Height must be in range [4, 25]");
        }
    }

    @Override
    public String toString() {
        return "PuzzleSpecification{" +
                "width=" + width +
                ", height=" + height +
                ", islandCount=" + islandCount +
                '}';
    }
}
