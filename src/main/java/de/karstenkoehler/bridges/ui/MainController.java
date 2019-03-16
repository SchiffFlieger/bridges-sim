package de.karstenkoehler.bridges.ui;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.Connection;
import de.karstenkoehler.bridges.model.PuzzleState;
import de.karstenkoehler.bridges.model.merger.Merger;
import de.karstenkoehler.bridges.model.merger.MergerImpl;
import de.karstenkoehler.bridges.model.solver.Solver;
import de.karstenkoehler.bridges.model.solver.SolverImpl;
import de.karstenkoehler.bridges.ui.components.AboutDialog;
import de.karstenkoehler.bridges.ui.components.NewPuzzleStage;
import de.karstenkoehler.bridges.ui.components.SaveRequest;
import de.karstenkoehler.bridges.ui.components.toast.ToastMessage;
import de.karstenkoehler.bridges.ui.shapes.BridgeShape;
import de.karstenkoehler.bridges.ui.shapes.IslandShape;
import de.karstenkoehler.bridges.ui.tasks.SolveSimulationService;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Service;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static de.karstenkoehler.bridges.ui.events.EventTypes.*;

/**
 * The controller of the main window of the application.
 */
public class MainController {

    @FXML
    private Button btnNextBridge;
    @FXML
    private MenuBar menubar;

    @FXML
    private Button btnToggleSimulation;

    @FXML
    private RadioMenuItem rbtnShowRemaining;
    @FXML
    private RadioMenuItem rbtnShowRequired;

    @FXML
    private RadioMenuItem rbtnBridgeHintsAlways;
    @FXML
    private RadioMenuItem rbtnBridgeHintsPossible;
    @FXML
    private RadioMenuItem rbtnBridgeHintsNever;

    @FXML
    private Pane controlPane;
    @FXML
    private CheckMenuItem cbxShowClickArea;
    @FXML
    private CheckMenuItem cbxShowGrid;
    @FXML
    private Label lblState;
    @FXML
    private Canvas canvas;

    @FXML
    private Slider slSpeed;

    private Service<Void> service;

    private PlayingFieldController fieldController;

    private final Solver puzzleSolver;
    private final Merger merger;
    private final FileUtils fileUtils;
    private NewPuzzleStage newPuzzleStage;
    private Stage stage;

    /**
     * Creates a new controller instance.
     */
    public MainController() {
        this.fileUtils = new FileUtils();
        this.puzzleSolver = new SolverImpl();
        this.merger = new MergerImpl();
    }

    /**
     * Initializes the controls in the main window and prepares some dependencies.
     */
    @FXML
    private void initialize() {
        initializeTooltips();
        initializeFieldController();
        registerMenuSelectionListeners();
        initializeSolveService();

        Optional<BridgesPuzzle> puzzle = this.fileUtils.openInitialFile(new File("src\\main\\resources\\data\\bsp_25x25.bgs"));
        puzzle.ifPresent(bridgesPuzzle -> this.fieldController.setPuzzle(bridgesPuzzle));
    }

    /**
     * Initializes the tooltips of some controls.
     */
    private void initializeTooltips() {
        btnNextBridge.setTooltip(new Tooltip("Build a safe bridge in the current puzzle."));
        btnToggleSimulation.setTooltip(new Tooltip("Automatically build safe bridges with a slight delay."));
        slSpeed.setTooltip(new Tooltip("Adjust the speed of the automatic solver."));
    }

    /**
     * Creates and initializes the instance of the playing field controller.
     */
    private void initializeFieldController() {
        this.fieldController = new PlayingFieldController(this.canvas, this.controlPane, IslandShape.NumberDisplay.SHOW_REQUIRED);
        this.fieldController.setBridgeHintsVisible(BridgeShape.BridgeHintsVisible.NEVER);
    }

    /**
     * Registers event listeners to the selectable options in the view menu.
     */
    private void registerMenuSelectionListeners() {
        cbxShowGrid.selectedProperty().addListener((observable, old, selected) -> fieldController.setGridVisible(selected));
        cbxShowClickArea.selectedProperty().addListener((observable, old, selected) -> fieldController.setClickAreaVisible(selected));

        this.rbtnShowRequired.selectedProperty().addListener((observable, oldValue, selected) -> {
            if (selected) {
                this.fieldController.setNumberDisplay(IslandShape.NumberDisplay.SHOW_REQUIRED);
                this.fieldController.draw();
            }
        });

        this.rbtnShowRemaining.selectedProperty().addListener((observable, oldValue, selected) -> {
            if (selected) {
                this.fieldController.setNumberDisplay(IslandShape.NumberDisplay.SHOW_REMAINING);
                this.fieldController.draw();
            }
        });

        rbtnBridgeHintsAlways.selectedProperty().addListener((observable, oldValue, selected) -> {
            if (selected) {
                this.fieldController.setBridgeHintsVisible(BridgeShape.BridgeHintsVisible.ALWAYS);
                this.fieldController.draw();
            }
        });

        rbtnBridgeHintsPossible.selectedProperty().addListener((observable, oldValue, selected) -> {
            if (selected) {
                this.fieldController.setBridgeHintsVisible(BridgeShape.BridgeHintsVisible.IF_POSSIBLE);
                this.fieldController.draw();
            }
        });

        rbtnBridgeHintsNever.selectedProperty().addListener((observable, oldValue, selected) -> {
            if (selected) {
                this.fieldController.setBridgeHintsVisible(BridgeShape.BridgeHintsVisible.NEVER);
                this.fieldController.draw();
            }
        });
    }

    /**
     * Initializes the solve simulation service with its dependencies.
     */
    private void initializeSolveService() {
        AtomicInteger sleepCount = new AtomicInteger(slSpeed.valueProperty().intValue());
        slSpeed.valueProperty().addListener((observable, oldValue, newValue) -> sleepCount.set(slSpeed.maxProperty().intValue() + 1 - newValue.intValue()));

        this.service = new SolveSimulationService(puzzleSolver, canvas, fieldController, sleepCount);

        this.service.setOnCancelled(event -> enableControls());
        this.service.setOnFailed(event -> enableControls());
        this.service.setOnSucceeded(event -> enableControls());

        this.service.setOnRunning(event -> disableControls());
        this.service.setOnScheduled(event -> disableControls());
    }

    /**
     * Sets the stage and registers some event listeners on it.
     *
     * @param mainStage the main stage of the application
     */
    public void setMainStage(Stage mainStage) {
        this.stage = mainStage;
        registerMainStageEvents();

        mainStage.titleProperty().bind(Bindings.concat(
                "Bridges Simulator - Karsten Köhler - 8690570 - ",
                this.fileUtils.titleFilenameProperty()
        ));

        this.fileUtils.setStage(mainStage);
        this.newPuzzleStage = new NewPuzzleStage(mainStage);
        this.newPuzzleStage.init(this.canvas);

        mainStage.fireEvent(new Event(EVAL_STATE));
    }

    /**
     * Registers some event handlers on the main stage.
     */
    private void registerMainStageEvents() {
        this.stage.addEventHandler(FILE_MODIFIED, event -> this.fileUtils.fileModified());
        this.stage.addEventHandler(REDRAW, event -> this.fieldController.draw());
        this.stage.addEventHandler(CHANGE_PUZZLE, event -> {
            this.fieldController.setPuzzle(event.getPuzzle());
            this.fileUtils.setNewFile();
        });

        this.stage.addEventHandler(EVAL_STATE, event -> {
            PuzzleState state = this.fieldController.getPuzzle().getState();
            lblState.setText(state.toString());
        });

        this.stage.setOnCloseRequest(event -> {
            SaveRequest.SaveAction action = this.fileUtils.saveIfNecessary(this.fieldController.getPuzzle());
            if (action == SaveRequest.SaveAction.CANCEL) {
                event.consume();
            }
        });
    }

    /**
     * Displays the dialog stage for creating a new random puzzle.
     */
    @FXML
    private void onNewPuzzle() {
        SaveRequest.SaveAction action = this.fileUtils.saveIfNecessary(this.fieldController.getPuzzle());
        if (action == SaveRequest.SaveAction.CANCEL) {
            return;
        }

        newPuzzleStage.showAndWait();
    }

    @FXML
    private void onMergePuzzles() {
        SaveRequest.SaveAction action = this.fileUtils.saveIfNecessary(this.fieldController.getPuzzle());
        if (action == SaveRequest.SaveAction.CANCEL) {
            return;
        }

        Optional<BridgesPuzzle> left = this.fileUtils.openFile();
        Optional<BridgesPuzzle> right = this.fileUtils.openFile();

        if (left.isPresent() && right.isPresent()) {
            BridgesPuzzle puzzle = merger.merge(left.get(), right.get());
            fieldController.setPuzzle(puzzle);
            this.fileUtils.setNewFile();
        }
    }

    /**
     * Restart the currently active puzzle.
     */
    @FXML
    private void onRestartPuzzle() {
        SaveRequest.SaveAction action = this.fileUtils.saveIfNecessary(this.fieldController.getPuzzle());
        if (action == SaveRequest.SaveAction.CANCEL) {
            return;
        }

        this.fieldController.restartPuzzle();
    }

    /**
     * Displays a file chooser for opening a puzzle from file.
     */
    @FXML
    private void onOpenPuzzle() {
        SaveRequest.SaveAction action = this.fileUtils.saveIfNecessary(this.fieldController.getPuzzle());
        if (action == SaveRequest.SaveAction.CANCEL) {
            return;
        }

        Optional<BridgesPuzzle> puzzle = this.fileUtils.openFile();
        puzzle.ifPresent(bridgesPuzzle -> this.fieldController.setPuzzle(bridgesPuzzle));
    }

    /**
     * Saves the current puzzle state to the known file. If there is no file associated with
     * the puzzle, a file chooser is shown to chose a new file.
     */
    @FXML
    private void onSavePuzzle() {
        this.fileUtils.saveToCurrentFile(this.fieldController.getPuzzle());
    }

    /**
     * Shows a file chooser for choosing the file to save to.
     */
    @FXML
    private void onSaveAs() {
        this.fileUtils.saveToNewFile(this.fieldController.getPuzzle());
    }

    /**
     * Closes the application.
     */
    @FXML
    private void onClose() {
        SaveRequest.SaveAction action = this.fileUtils.saveIfNecessary(this.fieldController.getPuzzle());
        if (action == SaveRequest.SaveAction.CANCEL) {
            return;
        }

        Platform.exit();
    }

    /**
     * Displays some basic information about this application.
     */
    @FXML
    private void onAbout() {
        AboutDialog.showAndWait();
    }

    /**
     * Calculates and draws a safe bridge to the puzzle.
     */
    @FXML
    private void onNextBridge() {
        Connection next = getNextSafeBridge();
        if (next == null) return;

        this.fieldController.getPuzzle().emphasizeBridge(next);
        next.setBridgeCount(next.getBridgeCount() + 1);

        this.canvas.fireEvent(new Event(EVAL_STATE));
        this.canvas.fireEvent(new Event(REDRAW));
        this.canvas.fireEvent(new Event(FILE_MODIFIED));
    }

    /**
     * Starts or stops the solve simulation.
     */
    @FXML
    private void onSolve() {
        Connection next = getNextSafeBridge();
        if (next == null) return;

        if (!service.isRunning()) {
            service.restart();
        } else {
            service.cancel();
        }
    }

    /**
     * Searches for a safe bridge in the puzzle. If there is no safe bridge an error message is shown.
     *
     * @return the connection that holds the safe bridge, null if there are no more safe bridges
     */
    private Connection getNextSafeBridge() {
        PuzzleState state = fieldController.getPuzzle().getState();
        if (state != PuzzleState.NOT_SOLVED) {
            showError(state);
            return null;
        }

        Connection next = puzzleSolver.nextSafeBridge(fieldController.getPuzzle());
        if (next == null) {
            ToastMessage.show(this.stage, ToastMessage.Type.INFO, "There are no more safe bridges");
            return null;
        }
        return next;
    }

    /**
     * Displays an suitable error message for the current puzzle state.
     *
     * @param state the current state of the puzzle
     */
    private void showError(PuzzleState state) {
        if (state == PuzzleState.SOLVED) {
            ToastMessage.show(this.stage, ToastMessage.Type.INFO, "The puzzle is already solved");
        } else if (state == PuzzleState.ERROR) {
            ToastMessage.show(this.stage, ToastMessage.Type.ERROR, "You need to fix the errors first");
        } else if (state == PuzzleState.NO_LONGER_SOLVABLE) {
            ToastMessage.show(this.stage, ToastMessage.Type.ERROR, "The puzzle is no longer solvable");
        }
    }

    /**
     * Enables the controls of this stage.
     */
    private void enableControls() {
        btnToggleSimulation.setText("Auto Step");
        btnNextBridge.setDisable(false);
        menubar.setDisable(false);
    }

    /**
     * Disables the controls of this stage.
     */
    private void disableControls() {
        btnToggleSimulation.setText("Stop");
        btnNextBridge.setDisable(true);
        menubar.setDisable(true);
    }
}
