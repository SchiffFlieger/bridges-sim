package de.karstenkoehler.bridges.model;

import java.util.Random;

public class PuzzleSpecification {
    private static final int MIN_SIZE = 4;
    private static final int MAX_SIZE = 25;

    private final boolean solution;
    private final int width;
    private final int height;
    private final int islandCount;

    private static final Random random = new Random(System.nanoTime());

    public static PuzzleSpecification random(boolean solution) {
        return PuzzleSpecification.withBounds(solution, intBetween(MIN_SIZE, MAX_SIZE), intBetween(MIN_SIZE, MAX_SIZE));
    }

    public static PuzzleSpecification withBounds(boolean solution, int width, int height) {
        int min = Math.min(width, height);
        int max = maxIslandCount(width, height);

        if (width * height == 16) { // 4x4 special case, given default boundaries do not work
            min = 3;
            max = 4;
        }
        return PuzzleSpecification.withSpecs(solution, width, height, intBetween(min, max));
    }

    public static PuzzleSpecification withSpecs(boolean solution, int width, int height, int islandCount) {
        if (width < MIN_SIZE || width > MAX_SIZE) {
            throw new IllegalArgumentException("width must be in range [4, 25].");
        }
        if (height < MIN_SIZE || height > MAX_SIZE) {
            throw new IllegalArgumentException("height must be in range [4, 25].");
        }
        if (islandCount < 2 || islandCount > maxIslandCount(width, height)) {
            throw new IllegalArgumentException("island count must be in range [2, width*height/5].");
        }

        return new PuzzleSpecification(solution, width, height, islandCount);
    }

    public static PuzzleSpecification withSpecsNoCheck(boolean solution, int width, int height, int islandCount) {
        return new PuzzleSpecification(solution, width, height, islandCount);
    }

    private PuzzleSpecification(boolean solution, int width, int height, int islandCount) {
        this.solution = solution;
        this.width = width;
        this.height = height;
        this.islandCount = islandCount;
    }

    public boolean generateSolution() {
        return this.solution;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

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
     * Returns the maximum number of allowed islands for a puzzle with given boundaries.
     *
     * @param width  width of the puzzle
     * @param height height of the puzzle
     * @return maximum number of islands
     */
    private static int maxIslandCount(int width, int height) {
        return (int) (width * height / 5.0);
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
