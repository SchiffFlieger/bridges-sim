package de.karstenkoehler.bridges.ui;

import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.solver.Solver;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.scene.canvas.Canvas;

public class SolveSimulationService extends Service<Void> {
    private final Solver puzzleSolver;
    private final Canvas canvas;
    private final CanvasController canvasController;
    private final IntegerProperty sleepTime;

    public SolveSimulationService(Solver puzzleSolver, Canvas canvas, CanvasController canvasController, IntegerProperty sleepTime) {
        this.puzzleSolver = puzzleSolver;
        this.canvas = canvas;
        this.canvasController = canvasController;
        this.sleepTime = sleepTime;
    }

    @Override
    protected Task<Void> createTask() {
        return new SimulationTask(puzzleSolver, canvas, canvasController, sleepTime);
    }

    private static class SimulationTask extends Task<Void> {
        private final Solver puzzleSolver;
        private final Canvas canvas;
        private final CanvasController canvasController;
        private final IntegerProperty sleepTime;

        public SimulationTask(Solver puzzleSolver, Canvas canvas, CanvasController canvasController, IntegerProperty sleepTime) {
            this.puzzleSolver = puzzleSolver;
            this.canvas = canvas;
            this.canvasController = canvasController;
            this.sleepTime = sleepTime;
        }

        @Override
        protected Void call() throws InterruptedException {
            while (!isCancelled()) {
                Bridge next = puzzleSolver.nextSafeBridge(canvasController.getPuzzle());
                if (next == null) {
                    return null;
                }

                this.canvasController.getPuzzle().emphasizeBridge(next);
                next.setBridgeCount(next.getBridgeCount() + 1);

                Platform.runLater(() -> {
                    this.canvas.fireEvent(new Event(CanvasController.EVAL_STATE));
                    this.canvas.fireEvent(new Event(CanvasController.REDRAW));
                });

                Thread.sleep(sleepTime.get());
            }

            return null;
        }
    }
}
