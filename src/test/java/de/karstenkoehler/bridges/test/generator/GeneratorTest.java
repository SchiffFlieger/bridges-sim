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
                {PuzzleSpecification.withSpecs(false, 4, 4, 3)},
                {PuzzleSpecification.withSpecs(false, 5, 4, 4)},
                {PuzzleSpecification.withSpecs(false, 5, 5, 5)},
                {PuzzleSpecification.withSpecs(false, 5, 25, 25)},
                {PuzzleSpecification.withSpecs(false, 6, 4, 4)},
                {PuzzleSpecification.withSpecs(false, 16, 24, 76)},
                {PuzzleSpecification.withSpecs(false, 18, 20, 72)},
                {PuzzleSpecification.withSpecs(false, 19, 25, 95)},
                {PuzzleSpecification.withSpecs(false, 22, 25, 110)},
                {PuzzleSpecification.withSpecs(false, 23, 4, 18)},
                {PuzzleSpecification.withSpecs(false, 23, 11, 50)},
                {PuzzleSpecification.withSpecs(false, 24, 9, 43)},
                {PuzzleSpecification.withSpecs(false, 24, 12, 57)},
                {PuzzleSpecification.withSpecs(false, 25, 4, 20)},
                {PuzzleSpecification.withSpecs(false, 25, 5, 25)},
                {PuzzleSpecification.withSpecs(false, 25, 8, 40)},
                {PuzzleSpecification.withSpecs(false, 25, 14, 70)},
                {PuzzleSpecification.withSpecs(false, 25, 23, 115)},
                {PuzzleSpecification.withSpecs(false, 25, 25, 125)},
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
