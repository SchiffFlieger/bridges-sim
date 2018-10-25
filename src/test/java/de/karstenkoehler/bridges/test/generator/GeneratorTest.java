package de.karstenkoehler.bridges.test.generator;

import de.karstenkoehler.bridges.io.validator.DefaultValidator;
import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Island;
import de.karstenkoehler.bridges.model.PuzzleSpecification;
import de.karstenkoehler.bridges.model.generator.Generator;
import de.karstenkoehler.bridges.model.generator.GeneratorImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(Parameterized.class)
public class GeneratorTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {PuzzleSpecification.withSpecs(false, 5, 5, 2)},
                {PuzzleSpecification.withSpecs(false, 5, 5, 3)},
                {PuzzleSpecification.withSpecs(false, 5, 5, 5)},
                {PuzzleSpecification.withSpecs(false, 25, 25, 5)},
                {PuzzleSpecification.withSpecs(false, 25, 25, 20)},
                {PuzzleSpecification.withSpecs(false, 25, 25, 50)},

                {PuzzleSpecification.withSpecs(true, 5, 5, 2)},
                {PuzzleSpecification.withSpecs(true, 5, 5, 3)},
                {PuzzleSpecification.withSpecs(true, 5, 5, 5)},
                {PuzzleSpecification.withSpecs(true, 25, 25, 5)},
                {PuzzleSpecification.withSpecs(true, 25, 25, 20)},
                {PuzzleSpecification.withSpecs(true, 25, 25, 50)},
        });
    }

    @Parameterized.Parameter
    public PuzzleSpecification spec;

    private static Generator generator = new GeneratorImpl(new DefaultValidator());

    @Test
    public void test() {
        BridgesPuzzle puzzle = generator.generate(spec);

        assertEquals(spec.getWidth(), puzzle.getWidth());
        assertEquals(spec.getHeight(), puzzle.getHeight());
        assertEquals(spec.getIslandCount(), puzzle.getIslands().size());

        if (spec.generateSolution()) {
            int islandSum = puzzle.getIslands().stream().mapToInt(Island::getRequiredBridges).sum();
            int bridgeSum = puzzle.getBridges().stream().mapToInt(Bridge::getBridgeCount).sum();
            assertEquals(islandSum, bridgeSum * 2);
        } else {
            int bridgeSum = puzzle.getBridges().stream().mapToInt(Bridge::getBridgeCount).sum();
            assertEquals(0, bridgeSum);
        }

        puzzle.getIslands().forEach(island -> assertNotEquals(0, island.getRequiredBridges()));
    }
}
