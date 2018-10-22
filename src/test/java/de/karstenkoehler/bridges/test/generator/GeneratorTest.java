package de.karstenkoehler.bridges.test.generator;

import de.karstenkoehler.bridges.io.validator.DefaultValidator;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.PuzzleSpecification;
import de.karstenkoehler.bridges.model.generator.Generator;
import de.karstenkoehler.bridges.model.generator.GeneratorImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GeneratorTest {
    @Test
    public void simpleSmall() {
        PuzzleSpecification spec = PuzzleSpecification.withSpecs(5, 5, 2);
        Generator generator = new GeneratorImpl(new DefaultValidator());
        BridgesPuzzle puzzle = generator.generate(spec);

        assertEquals(spec.getWidth(), puzzle.getWidth());
        assertEquals(spec.getHeight(), puzzle.getHeight());
        assertEquals(spec.getIslandCount(), puzzle.getIslands().size());
    }

    @Test
    public void mediumSmall() {
        PuzzleSpecification spec = PuzzleSpecification.withSpecs(5, 5, 3);
        Generator generator = new GeneratorImpl(new DefaultValidator());
        BridgesPuzzle puzzle = generator.generate(spec);

        assertEquals(spec.getWidth(), puzzle.getWidth());
        assertEquals(spec.getHeight(), puzzle.getHeight());
        assertEquals(spec.getIslandCount(), puzzle.getIslands().size());
    }

    @Test
    public void hardSmall() {
        PuzzleSpecification spec = PuzzleSpecification.withSpecs(5, 5, 5);
        Generator generator = new GeneratorImpl(new DefaultValidator());
        BridgesPuzzle puzzle = generator.generate(spec);

        assertEquals(spec.getWidth(), puzzle.getWidth());
        assertEquals(spec.getHeight(), puzzle.getHeight());
        assertEquals(spec.getIslandCount(), puzzle.getIslands().size());
    }

    @Test
    public void simpleBig() {
        PuzzleSpecification spec = PuzzleSpecification.withSpecs(25, 25, 5);
        Generator generator = new GeneratorImpl(new DefaultValidator());
        BridgesPuzzle puzzle = generator.generate(spec);

        assertEquals(spec.getWidth(), puzzle.getWidth());
        assertEquals(spec.getHeight(), puzzle.getHeight());
        assertEquals(spec.getIslandCount(), puzzle.getIslands().size());
    }

    @Test
    public void mediumBig() {
        PuzzleSpecification spec = PuzzleSpecification.withSpecs(25, 25, 20);
        Generator generator = new GeneratorImpl(new DefaultValidator());
        BridgesPuzzle puzzle = generator.generate(spec);

        assertEquals(spec.getWidth(), puzzle.getWidth());
        assertEquals(spec.getHeight(), puzzle.getHeight());
        assertEquals(spec.getIslandCount(), puzzle.getIslands().size());
    }

    @Test
    public void hardBig() {
        PuzzleSpecification spec = PuzzleSpecification.withSpecs(25, 25, 50);
        Generator generator = new GeneratorImpl(new DefaultValidator());
        BridgesPuzzle puzzle = generator.generate(spec);

        assertEquals(spec.getWidth(), puzzle.getWidth());
        assertEquals(spec.getHeight(), puzzle.getHeight());
        assertEquals(spec.getIslandCount(), puzzle.getIslands().size());
    }
}
