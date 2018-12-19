package de.karstenkoehler.bridges.ui;

import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.solver.Solver;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.scene.canvas.Canvas;

import java.util.concurrent.atomic.AtomicInteger;

public class SolveSimulationService extends Service<Void> {
    private final Solver puzzleSolver;
    private final Canvas canvas;
    private final CanvasController canvasController;
    private final AtomicInteger sleepCount;

    public SolveSimulationService(Solver puzzleSolver, Canvas canvas, CanvasController canvasController, AtomicInteger sleepCount) {
        this.puzzleSolver = puzzleSolver;
        this.canvas = canvas;
        this.canvasController = canvasController;
        this.sleepCount = sleepCount;
    }

    @Override
    protected Task<Void> createTask() {
        return new SimulationTask(puzzleSolver, canvas, canvasController, sleepCount);
    }

    private static class SimulationTask extends Task<Void> {
        private final Solver puzzleSolver;
        private final Canvas canvas;
        private final CanvasController canvasController;
        private final AtomicInteger sleepCount;

        public SimulationTask(Solver puzzleSolver, Canvas canvas, CanvasController canvasController, AtomicInteger sleepCount) {
            this.puzzleSolver = puzzleSolver;
            this.canvas = canvas;
            this.canvasController = canvasController;
            this.sleepCount = sleepCount;
        }

        @Override
        protected Void call() throws InterruptedException {
            while (!isCancelled()) {
                Connection next = puzzleSolver.nextSafeBridge(canvasController.getPuzzle());
                if (next == null) {
                    return null;
                }

                this.canvasController.getPuzzle().emphasizeBridge(next);
                next.setBridgeCount(next.getBridgeCount() + 1);

                Platform.runLater(() -> {
                    this.canvas.fireEvent(new Event(CanvasController.EVAL_STATE));
                    this.canvas.fireEvent(new Event(CanvasController.REDRAW));
                    this.canvas.fireEvent(new Event(MainController.FILE_CHANGED));
                });

                for (int i = 0; i < sleepCount.get(); i++) {
                    Thread.sleep(50);
                }
            }

            return null;
        }
    }
}
