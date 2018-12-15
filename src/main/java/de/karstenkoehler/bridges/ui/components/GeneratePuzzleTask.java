package de.karstenkoehler.bridges.ui.components;

import de.karstenkoehler.bridges.io.validator.DefaultValidator;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.PuzzleSpecification;
import de.karstenkoehler.bridges.model.generator.Generator;
import de.karstenkoehler.bridges.model.generator.GeneratorImpl;
import javafx.concurrent.Task;

public class GeneratePuzzleTask extends Task<BridgesPuzzle> {

    private final Generator generator;
    private final PuzzleSpecification specs;

    public GeneratePuzzleTask(PuzzleSpecification specs) {
        this.specs = specs;
        this.generator = new GeneratorImpl(new DefaultValidator());
    }

    @Override
    protected BridgesPuzzle call() {
        return generator.generate(this.specs);
    }
}
