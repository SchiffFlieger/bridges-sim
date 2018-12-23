package de.karstenkoehler.bridges.ui.tasks;

import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.solver.Solver;
import de.karstenkoehler.bridges.ui.PlayingFieldController;
import de.karstenkoehler.bridges.ui.events.EventTypes;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.scene.Node;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A reusable background task for the automatic solver. Register listeners to the {@link Service}'s state
 * change properties (e. g. {@link Service#onSucceededProperty()})to receive notifications about the
 * current state of the background task.
 */
public class SolveSimulationService extends Service<Void> {
    private final Solver puzzleSolver;
    private final Node eventNode;
    private final PlayingFieldController fieldController;
    private final AtomicInteger sleepCount;

    /**
     * Creates a new reusable service for the automatic solve simulation.
     *
     * @param puzzleSolver    the solver to use
     * @param eventNode       a node of the scene to fire events to
     * @param fieldController the controller of the playing field
     * @param sleepCount      a counter for the speed of the simulation
     */
    public SolveSimulationService(Solver puzzleSolver, Node eventNode, PlayingFieldController fieldController, AtomicInteger sleepCount) {
        this.puzzleSolver = puzzleSolver;
        this.eventNode = eventNode;
        this.fieldController = fieldController;
        this.sleepCount = sleepCount;
    }

    /**
     * @see Service#createTask()
     */
    @Override
    protected Task<Void> createTask() {
        return new SimulationTask(puzzleSolver, eventNode, fieldController, sleepCount);
    }

    /**
     * A background task for the automatic solver.
     */
    private static class SimulationTask extends Task<Void> {
        private final Solver puzzleSolver;
        private final Node eventNode;
        private final PlayingFieldController fieldController;
        private final AtomicInteger sleepCount;

        /**
         * Creates a single use background task for the automatic solve simulation.
         *
         * @param puzzleSolver    the solver to use
         * @param eventNode       a node of the scene to fire events to
         * @param fieldController the controller of the playing field
         * @param sleepCount      a counter for the speed of the simulation
         */
        SimulationTask(Solver puzzleSolver, Node eventNode, PlayingFieldController fieldController, AtomicInteger sleepCount) {
            this.puzzleSolver = puzzleSolver;
            this.eventNode = eventNode;
            this.fieldController = fieldController;
            this.sleepCount = sleepCount;
        }

        /**
         * Once started, this task adds a safe bridge and then waits for a given amount of time. This steps
         * are repeated until the task is cancelled or there are no more safe bridges.
         *
         * @return void
         * @throws InterruptedException if any thread has interrupted the current thread
         * @see Task#call()
         */
        @Override
        protected Void call() throws InterruptedException {
            while (!isCancelled()) {
                Connection next = puzzleSolver.nextSafeBridge(fieldController.getPuzzle());
                if (next == null) {
                    return null;
                }

                this.fieldController.getPuzzle().emphasizeBridge(next);
                next.setBridgeCount(next.getBridgeCount() + 1);

                Platform.runLater(() -> {
                    this.eventNode.fireEvent(new Event(EventTypes.EVAL_STATE));
                    this.eventNode.fireEvent(new Event(EventTypes.REDRAW));
                    this.eventNode.fireEvent(new Event(EventTypes.FILE_MODIFIED));
                });

                for (int i = 0; i < sleepCount.get(); i++) {
                    Thread.sleep(50);
                }
            }

            return null;
        }
    }
}
