package de.karstenkoehler.bridges;

import de.feu.ps.bridges.shared.BridgesTester;
import de.karstenkoehler.bridges.io.BridgesFileReader;
import de.karstenkoehler.bridges.io.BridgesFileWriter;
import de.karstenkoehler.bridges.io.parser.ParseException;
import de.karstenkoehler.bridges.io.validator.DefaultValidator;
import de.karstenkoehler.bridges.io.validator.ValidateException;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.PuzzleSpecification;
import de.karstenkoehler.bridges.model.generator.Generator;
import de.karstenkoehler.bridges.model.generator.GeneratorImpl;
import de.karstenkoehler.bridges.model.solver.Solver;
import de.karstenkoehler.bridges.model.solver.SolverImpl;

import java.io.File;
import java.io.IOException;

/**
 * A simple implementation to satisfy the {@link BridgesTester} interface.
 */
public class BridgesTesterImpl implements BridgesTester {
    private Generator generator;
    private Solver solver;
    private BridgesFileWriter writer;
    private BridgesFileReader reader;

    public BridgesTesterImpl() {
        this.generator = new GeneratorImpl(new DefaultValidator());
        this.solver = new SolverImpl();
        this.writer = new BridgesFileWriter();
        this.reader = new BridgesFileReader();
    }

    @Override
    public void testGeneratePuzzle(String filePath, int width, int height, int isles) {
        File file = new File(filePath);
        if (file.exists()) {
            throw new IllegalArgumentException("file already exists");
        }

        try {
            PuzzleSpecification spec = PuzzleSpecification.withSpecs(false, width, height, isles);
            writer.writeFile(file, generator.generate(spec));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testSolvePuzzle(String puzzlePath, String solutionPath) {
        File inFile = new File(puzzlePath);
        if (!inFile.exists()) {
            throw new IllegalArgumentException("file not found");
        }

        File outFile = new File(solutionPath);
        if (outFile.exists()) {
            throw new IllegalArgumentException("file already exists");
        }

        try {
            BridgesPuzzle puzzle = reader.readFile(inFile);
            puzzle.fillMissingConnections();

            while (true) {
                Connection next = solver.nextSafeBridge(puzzle);
                if (next == null) {
                    break;
                }
                next.setBridgeCount(next.getBridgeCount() + 1);
            }

            writer.writeFile(outFile, puzzle);

        } catch (ParseException | ValidateException | IOException e) {
            e.printStackTrace();
        }
    }
}
