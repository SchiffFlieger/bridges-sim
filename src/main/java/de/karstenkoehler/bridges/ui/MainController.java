package de.karstenkoehler.bridges.ui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

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

    private Stage stage;
    private CanvasController canvasController;

    private final RetentionFileChooser chooser;
    private File currentFile;

    // TODO pass dependencies as constructor parameters
    public MainController() {
        chooser = new RetentionFileChooser();
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


        this.currentFile = new File("src\\main\\resources\\data\\bsp_5x5.bgs");
        this.canvasController.openFile(this.currentFile);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.addEventHandler(FILE_CHANGED, event -> this.fileChanged());
        this.canvasController.setStage(stage);
        this.setFile(this.currentFile);
    }


    @FXML
    private void onNewPuzzle(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ui/new.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Create new puzzle");
            stage.setScene(new Scene(root));

            stage.initOwner(this.stage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "could not open file ui/new.fxml");
            alert.showAndWait();
        }
    }

    @FXML
    private void onRestartPuzzle(ActionEvent actionEvent) {
        this.canvasController.restartPuzzle();
    }

    @FXML
    private void onOpenPuzzle(ActionEvent actionEvent) {
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            this.canvasController.openFile(file);
            this.setFile(file);
        }
    }

    @FXML
    private void onSavePuzzle(ActionEvent actionEvent) {
        if (this.currentFile == null) {
            this.currentFile = chooser.showSaveDialog(this.stage);
        }
        if (this.currentFile != null) {
            this.canvasController.saveToFile(currentFile);
            this.setFile(currentFile);
        }
    }

    @FXML
    private void onSaveAs(ActionEvent actionEvent) {
        File file = chooser.showSaveDialog(stage);
        if (file != null) {
            this.canvasController.saveToFile(file);
            this.setFile(file);
        }
    }

    @FXML
    private void onClose(ActionEvent actionEvent) {
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

    private void setFile(File file) {
        this.currentFile = file;
        this.stage.setTitle("Bridges Simulator - Karsten Köhler - 8690570 - " + file.getName());
    }

    private void fileChanged() {
        String title = this.stage.getTitle();
        if (!title.endsWith("*")) {
            this.stage.setTitle(title + "*");
        }
    }
}
