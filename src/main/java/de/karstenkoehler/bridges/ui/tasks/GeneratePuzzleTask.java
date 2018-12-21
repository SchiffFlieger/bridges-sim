package de.karstenkoehler.bridges.ui.tasks;

import de.karstenkoehler.bridges.io.validator.DefaultValidator;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.PuzzleSpecification;
import de.karstenkoehler.bridges.model.generator.Generator;
import de.karstenkoehler.bridges.model.generator.GeneratorImpl;
import javafx.concurrent.Task;

/**
 * A background task that generates a new {@link BridgesPuzzle} asynchronously. Register a listener
 * on the {@link Task#onSucceededProperty()} method to use the generated puzzle.
 */
public class GeneratePuzzleTask extends Task<BridgesPuzzle> {

    private final Generator generator;
    private final PuzzleSpecification specs;

    /**
     * Creates a new task that can be run asynchronously in a background thread.
     *
     * @param specs the specification for the puzzle to generate
     */
    public GeneratePuzzleTask(PuzzleSpecification specs) {
        this.specs = specs;
        this.generator = new GeneratorImpl(new DefaultValidator());
    }

    /**
     * Generates a new random {@link BridgesPuzzle}.
     *
     * @see Task#call()
     */
    @Override
    protected BridgesPuzzle call() {
        return generator.generate(this.specs);
    }
}
