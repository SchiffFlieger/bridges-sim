package de.karstenkoehler.bridges.ui;

import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.solver.Solver;
import javafx.beans.property.IntegerProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class SolveSimulationService extends Service<Void> {
    private final Solver puzzleSolver;
    private final CanvasController canvasController;
    private final IntegerProperty sleepTime;

    public SolveSimulationService(Solver puzzleSolver, CanvasController canvasController, IntegerProperty sleepTime) {
        this.puzzleSolver = puzzleSolver;
        this.canvasController = canvasController;
        this.sleepTime = sleepTime;
    }

    @Override
    protected Task<Void> createTask() {
        return new SimulationTask(puzzleSolver, canvasController, sleepTime);
    }

    private static class SimulationTask extends Task<Void> {
        private final Solver puzzleSolver;
        private final CanvasController canvasController;
        private final IntegerProperty sleepTime;

        public SimulationTask(Solver puzzleSolver, CanvasController canvasController, IntegerProperty sleepTime) {
            this.puzzleSolver = puzzleSolver;
            this.canvasController = canvasController;
            this.sleepTime = sleepTime;
        }

        @Override
        protected Void call() throws InterruptedException {
            for (int i = 0; i < 150; i++) {
                Bridge next = puzzleSolver.nextSafeBridge(canvasController.getPuzzle());
                if (next == null) {
                    return null;
                }

                this.canvasController.getPuzzle().emphasizeBridge(next);
                next.setBridgeCount(next.getBridgeCount() + 1);
                canvasController.drawThings();

                Thread.sleep(sleepTime.get());
            }

            return null;
        }
    }
}
