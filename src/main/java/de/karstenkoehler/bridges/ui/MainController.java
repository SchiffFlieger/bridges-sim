package de.karstenkoehler.bridges.ui;

import de.karstenkoehler.bridges.io.validator.DefaultValidator;
import de.karstenkoehler.bridges.model.Bridge;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.PuzzleSpecification;
import de.karstenkoehler.bridges.model.PuzzleState;
import de.karstenkoehler.bridges.model.generator.Generator;
import de.karstenkoehler.bridges.model.generator.GeneratorImpl;
import de.karstenkoehler.bridges.model.solver.Solver;
import de.karstenkoehler.bridges.model.solver.SolverImpl;
import de.karstenkoehler.bridges.ui.components.NewPuzzleStage;
import de.karstenkoehler.bridges.ui.components.SaveAction;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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

import static de.karstenkoehler.bridges.ui.CanvasController.*;

public class MainController {
    public static final EventType<Event> FILE_CHANGED = new EventType<>("FILE_CHANGED");
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

    private final Generator puzzleGenerator;
    private final Solver puzzleSolver;
    private final FileHelper fileHelper;
    private NewPuzzleStage newPuzzleStage;

    public MainController() {
        this.puzzleGenerator = new GeneratorImpl(new DefaultValidator());
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

        IntegerProperty sleepTime = new SimpleIntegerProperty();
        sleepTime.bind(Bindings.subtract(1550, slSpeed.valueProperty()));
        this.service = new SolveSimulationService(puzzleSolver, canvasController, sleepTime);

        this.service.setOnCancelled(event -> enableControls());
        this.service.setOnFailed(event -> enableControls());
        this.service.setOnSucceeded(event -> enableControls());

        this.service.setOnRunning(event -> disableControls());
        this.service.setOnScheduled(event -> disableControls());

        Optional<BridgesPuzzle> puzzle = this.fileHelper.openInitialFile(new File("src\\main\\resources\\data\\bsp_25x25.bgs"));
        puzzle.ifPresent(bridgesPuzzle -> this.canvasController.setPuzzle(bridgesPuzzle));
    }

    public void setMainStage(Stage mainStage) throws IOException {
        mainStage.addEventHandler(FILE_CHANGED, event -> this.fileHelper.fileModified());
        mainStage.addEventHandler(REDRAW, event -> this.canvasController.drawThings());
        mainStage.addEventHandler(ERROR, event -> System.out.println("could not draw bridge"));

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
        this.newPuzzleStage.init();

        mainStage.fireEvent(new Event(EVAL_STATE));
    }

    @FXML
    private void onNewPuzzle() {
        SaveAction action = this.fileHelper.saveIfNecessary(this.canvasController.getPuzzle());
        if (action == SaveAction.CANCEL) {
            return;
        }

        Optional<PuzzleSpecification> specs = newPuzzleStage.showAndWait();

        specs.ifPresent(puzzleSpecification -> {
            BridgesPuzzle puzzle = this.puzzleGenerator.generate(specs.get());
            this.canvasController.setPuzzle(puzzle);
            this.fileHelper.resetFile();
        });
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
        System.out.println("about");
    }

    @FXML
    private void onNextBridge() {
        Bridge next = puzzleSolver.nextSafeBridge(canvasController.getPuzzle());
        if (next == null) {
            return;
        }

        this.canvasController.getPuzzle().emphasizeBridge(next);
        next.setBridgeCount(next.getBridgeCount() + 1);
        canvasController.drawThings();
    }

    @FXML
    private void onSolve() {
        if (!service.isRunning()) {
            service.restart();
        } else {
            service.cancel();
        }
    }

    private void enableControls() {
        btnToggleSimulation.setText("Auto Step");
        slSpeed.setDisable(false);
        btnNextBridge.setDisable(false);
        menubar.setDisable(false);
    }

    private void disableControls() {
        btnToggleSimulation.setText("Stop");
        slSpeed.setDisable(true);
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
