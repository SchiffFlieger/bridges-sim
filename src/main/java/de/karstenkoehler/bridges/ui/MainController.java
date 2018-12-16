package de.karstenkoehler.bridges.ui;

import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.PuzzleState;
import de.karstenkoehler.bridges.model.solver.Solver;
import de.karstenkoehler.bridges.model.solver.SolverImpl;
import de.karstenkoehler.bridges.ui.components.AboutAlert;
import de.karstenkoehler.bridges.ui.components.NewPuzzleStage;
import de.karstenkoehler.bridges.ui.components.PuzzleChangeEvent;
import de.karstenkoehler.bridges.ui.components.SaveAction;
import de.karstenkoehler.bridges.ui.components.toast.ToastMessage;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Service;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static de.karstenkoehler.bridges.ui.CanvasController.EVAL_STATE;
import static de.karstenkoehler.bridges.ui.CanvasController.REDRAW;

public class MainController {
    public static final EventType<Event> FILE_CHANGED = new EventType<>("FILE_CHANGED");
    public static final EventType<PuzzleChangeEvent> CHANGE_PUZZLE = new EventType<>("CHANGE_PUZZLE");

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

    private CanvasController canvasController;

    private final Solver puzzleSolver;
    private final FileHelper fileHelper;
    private NewPuzzleStage newPuzzleStage;
    private Stage stage;

    public MainController() {
        this.fileHelper = new FileHelper();
        this.puzzleSolver = new SolverImpl();
    }

    @FXML
    private void initialize() {
        cbxShowGrid.selectedProperty().addListener(setGridVisibility());
        cbxShowClickArea.selectedProperty().addListener(setClickAreaVisibility());

        this.rbtnShowRequired.selectedProperty().addListener((observable, oldValue, newValue) -> {
            this.canvasController.setNumberDisplay(NumberDisplay.SHOW_REQUIRED);
            this.canvasController.drawThings();
        });

        this.rbtnShowRemaining.selectedProperty().addListener((observable, oldValue, newValue) -> {
            this.canvasController.setNumberDisplay(NumberDisplay.SHOW_REMAINING);
            this.canvasController.drawThings();
        });

        this.canvasController = new CanvasController(this.canvas, this.controlPane, NumberDisplay.SHOW_REQUIRED);

        rbtnBridgeHintsAlways.selectedProperty().addListener((observable, oldValue, selected) -> {
            if (selected) {
                this.canvasController.setBridgeHintsVisible(BridgeHintsVisible.ALWAYS);
                this.canvasController.drawThings();
            }
        });

        rbtnBridgeHintsPossible.selectedProperty().addListener((observable, oldValue, selected) -> {
            if (selected) {
                this.canvasController.setBridgeHintsVisible(BridgeHintsVisible.IF_POSSIBLE);
                this.canvasController.drawThings();
            }
        });

        rbtnBridgeHintsNever.selectedProperty().addListener((observable, oldValue, selected) -> {
            if (selected) {
                this.canvasController.setBridgeHintsVisible(BridgeHintsVisible.NEVER);
                this.canvasController.drawThings();
            }
        });
        this.canvasController.setBridgeHintsVisible(BridgeHintsVisible.NEVER);

        System.out.println(slSpeed.valueProperty().intValue());
        AtomicInteger sleepCount = new AtomicInteger(slSpeed.valueProperty().intValue());
        slSpeed.valueProperty().addListener((observable, oldValue, newValue) -> sleepCount.set(slSpeed.maxProperty().intValue() + 1 - newValue.intValue()));

        this.service = new SolveSimulationService(puzzleSolver, canvas, canvasController, sleepCount);

        this.service.setOnCancelled(event -> enableControls());
        this.service.setOnFailed(event -> enableControls());
        this.service.setOnSucceeded(event -> enableControls());

        this.service.setOnRunning(event -> disableControls());
        this.service.setOnScheduled(event -> disableControls());

        Optional<BridgesPuzzle> puzzle = this.fileHelper.openInitialFile(new File("src\\main\\resources\\data\\bsp_25x25.bgs"));
        puzzle.ifPresent(bridgesPuzzle -> this.canvasController.setPuzzle(bridgesPuzzle));
    }

    public void setMainStage(Stage mainStage) throws IOException {
        this.stage = mainStage;
        mainStage.addEventHandler(FILE_CHANGED, event -> this.fileHelper.fileModified());
        mainStage.addEventHandler(REDRAW, event -> this.canvasController.drawThings());
        mainStage.addEventHandler(CHANGE_PUZZLE, event -> {
            this.canvasController.setPuzzle(event.getPuzzle());
            this.fileHelper.resetFile();
        });

        mainStage.addEventHandler(EVAL_STATE, event -> {
            PuzzleState state = this.canvasController.getPuzzle().getState();
            lblState.setText(state.toString());
        });

        mainStage.setOnCloseRequest(event -> {
            SaveAction action = this.fileHelper.saveIfNecessary(this.canvasController.getPuzzle());
            if (action == SaveAction.CANCEL) {
                event.consume();
            }
        });

        mainStage.titleProperty().bind(Bindings.concat(
                "Bridges Simulator - Karsten Köhler - 8690570 - ",
                this.fileHelper.titleFilenameProperty()
        ));

        this.fileHelper.setStage(mainStage);
        this.newPuzzleStage = new NewPuzzleStage(mainStage);
        this.newPuzzleStage.init(this.canvas);

        mainStage.fireEvent(new Event(EVAL_STATE));
    }

    @FXML
    private void onNewPuzzle() {
        SaveAction action = this.fileHelper.saveIfNecessary(this.canvasController.getPuzzle());
        if (action == SaveAction.CANCEL) {
            return;
        }

        newPuzzleStage.showAndWait();
    }

    @FXML
    private void onRestartPuzzle() {
        SaveAction action = this.fileHelper.saveIfNecessary(this.canvasController.getPuzzle());
        if (action == SaveAction.CANCEL) {
            return;
        }

        this.canvasController.restartPuzzle();
    }

    @FXML
    private void onOpenPuzzle() {
        SaveAction action = this.fileHelper.saveIfNecessary(this.canvasController.getPuzzle());
        if (action == SaveAction.CANCEL) {
            return;
        }

        Optional<BridgesPuzzle> puzzle = this.fileHelper.openFile();
        puzzle.ifPresent(bridgesPuzzle -> this.canvasController.setPuzzle(bridgesPuzzle));
    }

    @FXML
    private void onSavePuzzle() {
        this.fileHelper.saveToCurrentFile(this.canvasController.getPuzzle());
    }

    @FXML
    private void onSaveAs() {
        this.fileHelper.saveToNewFile(this.canvasController.getPuzzle());
    }

    @FXML
    private void onClose() {
        SaveAction action = this.fileHelper.saveIfNecessary(this.canvasController.getPuzzle());
        if (action == SaveAction.CANCEL) {
            return;
        }

        Platform.exit();
    }

    @FXML
    private void onAbout() {
        AboutAlert.showAndWait();
    }

    @FXML
    private void onNextBridge() {
        Bridge next = getNextSafeBridge();
        if (next == null) return;

        this.canvasController.getPuzzle().emphasizeBridge(next);
        next.setBridgeCount(next.getBridgeCount() + 1);

        this.canvas.fireEvent(new Event(EVAL_STATE));
        this.canvas.fireEvent(new Event(REDRAW));
        this.canvas.fireEvent(new Event(FILE_CHANGED));
    }

    @FXML
    private void onSolve() {
        Bridge next = getNextSafeBridge();
        if (next == null) return;

        if (!service.isRunning()) {
            service.restart();
        } else {
            service.cancel();
        }
    }

    private Bridge getNextSafeBridge() {
        PuzzleState state = canvasController.getPuzzle().getState();
        if (state != PuzzleState.NOT_SOLVED) {
            showError(state);
            return null;
        }

        Bridge next = puzzleSolver.nextSafeBridge(canvasController.getPuzzle());
        if (next == null) {
            ToastMessage.show(this.stage, ToastMessage.Type.INFO, "There are no more safe bridges");
            return null;
        }
        return next;
    }

    private void showError(PuzzleState state) {
        if (state == PuzzleState.SOLVED) {
            ToastMessage.show(this.stage, ToastMessage.Type.INFO, "The puzzle is already solved");
        } else if (state == PuzzleState.ERROR) {
            ToastMessage.show(this.stage, ToastMessage.Type.ERROR, "You need to fix the errors first");
        } else if (state == PuzzleState.NO_LONGER_SOLVABLE) {
            ToastMessage.show(this.stage, ToastMessage.Type.ERROR, "The puzzle is no longer solvable");
        }
    }

    private void enableControls() {
        btnToggleSimulation.setText("Auto Step");
        btnNextBridge.setDisable(false);
        menubar.setDisable(false);
    }

    private void disableControls() {
        btnToggleSimulation.setText("Stop");
        btnNextBridge.setDisable(true);
        menubar.setDisable(true);
    }

    private ChangeListener<Boolean> setGridVisibility() {
        return (observable, old, selected) -> canvasController.setGridVisible(selected);
    }

    private ChangeListener<Boolean> setClickAreaVisibility() {
        return (observable, old, selected) -> canvasController.setClickAreaVisible(selected);
    }
}
