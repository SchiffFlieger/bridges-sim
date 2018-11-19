package de.karstenkoehler.bridges.test.model;

import de.karstenkoehler.bridges.model.PuzzleSpecification;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PuzzleSpecificationTest {
    /**
     * Since there is randomness involved, some tests may fail only under certain conditions. Therefore we
     * repeat those test cases several times in order to spot errors inflicted by randomness. Still there
     * is no guarantee to catch these errors.
     */
    private static final int ITERATIONS = 10000;

    @Test
    public void allRandom() {
        for (int i = 0; i < ITERATIONS; i++) {
            PuzzleSpecification spec = PuzzleSpecification.random(true);
            assertTrue(spec.generateSolution());
            assertTrue(spec.getWidth() >= 4);
            assertTrue(spec.getWidth() <= 25);
            assertTrue(spec.getHeight() >= 4);
            assertTrue(spec.getHeight() <= 25);
            assertTrue(spec.getIslandCount() >= 2);
            assertTrue(spec.getIslandCount() <= spec.getWidth() * spec.getHeight() / 5.0);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void widthTooLow() {
        PuzzleSpecification.withSpecs(false, 3, 5, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void widthTooHigh() {
        PuzzleSpecification.withSpecs(false, 26, 5, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void heightTooLow() {
        PuzzleSpecification.withSpecs(false, 5, 3, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void heightTooHigh() {
        PuzzleSpecification.withSpecs(false, 5, 26, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void tooFewIslands() {
        PuzzleSpecification.withSpecs(false, 5, 5, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void tooManyIslands() {
        PuzzleSpecification.withSpecs(false, 5, 5, 6);
    }

    @Test
    public void specsOk() {
        PuzzleSpecification.withSpecs(false, 5, 5, 3);
    }
}
