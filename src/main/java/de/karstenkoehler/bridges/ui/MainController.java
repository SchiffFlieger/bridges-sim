package de.karstenkoehler.bridges.ui;

import de.karstenkoehler.bridges.io.validator.DefaultValidator;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.PuzzleSpecification;
import de.karstenkoehler.bridges.model.generator.Generator;
import de.karstenkoehler.bridges.model.generator.GeneratorImpl;
import de.karstenkoehler.bridges.ui.components.NewPuzzleStage;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class MainController {
    public static final EventType<Event> FILE_CHANGED = new EventType<>("FILE_CHANGED");

    @FXML
    private Pane controlPane;
    @FXML
    private CheckMenuItem cbxShowClickArea;
    @FXML
    private CheckMenuItem cbxShowGrid;
    @FXML
    private ChoiceBox<String> islandDisplayChoice;
    @FXML
    private Label stateText;
    @FXML
    private Canvas canvas;

    private CanvasController canvasController;

    private final Generator puzzleGenerator;
    private final FileHelper fileHelper;
    private NewPuzzleStage newPuzzleStage;

    public MainController() {
        this.puzzleGenerator = new GeneratorImpl(new DefaultValidator());
        this.fileHelper = new FileHelper();
    }

    @FXML
    private void initialize() {
        cbxShowGrid.selectedProperty().addListener(setGridVisibility());
        cbxShowClickArea.selectedProperty().addListener(setClickAreaVisibility());

        this.canvasController = new CanvasController(this.canvas, this.controlPane);
        this.islandDisplayChoice.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            NumberDisplay display = NumberDisplay.values()[newValue.intValue()];
            this.canvasController.setNumberDisplay(display);
            this.canvasController.drawThings();
        });

        canvas.addEventHandler(FILE_CHANGED, event -> this.fileHelper.fileModified());

        Optional<BridgesPuzzle> puzzle = this.fileHelper.openInitialFile(new File("src\\main\\resources\\data\\bsp_5x5.bgs"));
        puzzle.ifPresent(bridgesPuzzle -> this.canvasController.setPuzzle(bridgesPuzzle));
    }

    public void setMainStage(Stage mainStage) throws IOException {
        this.fileHelper.setStage(mainStage);
        mainStage.titleProperty().bind(Bindings.concat(
                "Bridges Simulator - Karsten Köhler - 8690570 - ",
                this.fileHelper.titleFilenameProperty()
        ));
        this.newPuzzleStage = new NewPuzzleStage(mainStage);
        this.newPuzzleStage.init();
    }

    @FXML
    private void onNewPuzzle(ActionEvent actionEvent) {
        this.fileHelper.saveIfNecessary(this.canvasController.getPuzzle());

        Optional<PuzzleSpecification> specs = newPuzzleStage.showAndWait();

        specs.ifPresent(puzzleSpecification -> {
            BridgesPuzzle puzzle = this.puzzleGenerator.generate(specs.get());
            this.canvasController.setPuzzle(puzzle);
            this.fileHelper.resetFile();
        });
    }

    @FXML
    private void onRestartPuzzle(ActionEvent actionEvent) {
        this.fileHelper.saveIfNecessary(this.canvasController.getPuzzle());
        this.canvasController.restartPuzzle();
    }

    @FXML
    private void onOpenPuzzle(ActionEvent actionEvent) {
        this.fileHelper.saveIfNecessary(this.canvasController.getPuzzle());
        Optional<BridgesPuzzle> puzzle = this.fileHelper.openFile();
        puzzle.ifPresent(bridgesPuzzle -> this.canvasController.setPuzzle(bridgesPuzzle));
    }

    @FXML
    private void onSavePuzzle(ActionEvent actionEvent) {
        this.fileHelper.saveToCurrentFile(this.canvasController.getPuzzle());
    }

    @FXML
    private void onSaveAs(ActionEvent actionEvent) {
        this.fileHelper.saveToNewFile(this.canvasController.getPuzzle());
    }

    @FXML
    private void onClose(ActionEvent actionEvent) {
        this.fileHelper.saveIfNecessary(this.canvasController.getPuzzle());
        Platform.exit();
    }

    @FXML
    private void onAbout(ActionEvent actionEvent) {
        System.out.println("about");
    }

    @FXML
    private void onNextBridge(ActionEvent actionEvent) {
        System.out.println("next bridge");
    }

    @FXML
    private void onSolve(ActionEvent actionEvent) {
        System.out.println("solve");
    }

    private ChangeListener<Boolean> setGridVisibility() {
        return (observable, old, selected) -> canvasController.setGridVisible(selected);
    }

    private ChangeListener<Boolean> setClickAreaVisibility() {
        return (observable, old, selected) -> canvasController.setClickAreaVisible(selected);
    }

}
