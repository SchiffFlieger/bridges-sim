package de.karstenkoehler.bridges;

import de.karstenkoehler.bridges.io.BridgesFileWriter;
import de.karstenkoehler.bridges.io.validator.DefaultValidator;
import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.PuzzleSpecification;
import de.karstenkoehler.bridges.model.PuzzleState;
import de.karstenkoehler.bridges.model.generator.Generator;
import de.karstenkoehler.bridges.model.generator.GeneratorImpl;
import de.karstenkoehler.bridges.model.solver.Solver;
import de.karstenkoehler.bridges.model.solver.SolverImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainTestSolver {

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            Thread t = new Thread(() -> new SolveTester().start());
            t.start();
            threads.add(t);
        }

        for (Thread t : threads) {
            t.join();
        }
    }


    private static class SolveTester {
        private final Generator generator;
        private final Solver solver;
        private final BridgesFileWriter writer;
        private final int count;

        public SolveTester() {
            this.generator = new GeneratorImpl(new DefaultValidator());
            this.solver = new SolverImpl();
            this.writer = new BridgesFileWriter();
            this.count = 0;
        }

        public void start() {
            try {
                findErrorInSolver(generator, solver, count);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void findErrorInSolver(Generator generator, Solver solver, int count) throws IOException {
            while (true) {
                PuzzleSpecification spec = PuzzleSpecification.random(false);
                BridgesPuzzle puzzle = generator.generate(spec);

                trySolve(solver, count, puzzle);
                count++;
            }
        }

        private void trySolve(Solver solver, int count, BridgesPuzzle puzzle) throws IOException {
            while (true) {
                Bridge next = solver.nextSafeBridge(puzzle);
                if (next == null) {
                    break;
                }
                next.setBridgeCount(next.getBridgeCount() + 1);

                PuzzleState state = puzzle.getState();

                if (state == PuzzleState.SOLVED) {
                    break;
                }
                if (state == PuzzleState.ERROR || state == PuzzleState.NO_LONGER_SOLVABLE) {
                    System.out.println("found error " + count);
                    File file = new File("err_file_" + count + ".bgs");
                    puzzle.restart();

                    this.writer.writeFile(file, puzzle);
                    break;
                }
            }
        }
    }
}
