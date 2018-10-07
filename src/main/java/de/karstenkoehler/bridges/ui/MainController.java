package de.karstenkoehler.bridges.ui;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class MainController {


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

    private Window stage;
    private CanvasController canvasController;

    @FXML
    private void initialize() {
        islandDisplayChoice.getItems().addAll(
                "show required bridges", "show missing bridges"
        );
        islandDisplayChoice.getSelectionModel().select(0);

        cbxShowGrid.selectedProperty().addListener(setGridVisibility());

        this.canvasController = new CanvasController(this.canvas);
        canvasController.drawThings();
    }

    public void setStage(Window stage) {
        this.stage = stage;
    }

    @FXML
    private void onNewPuzzle(ActionEvent actionEvent) {
        System.out.println("new");

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
        System.out.println("restart");
    }

    @FXML
    private void onOpenPuzzle(ActionEvent actionEvent) {
        System.out.println("open");
    }

    @FXML
    private void onSavePuzzle(ActionEvent actionEvent) {
        System.out.println("save");
    }

    @FXML
    private void onSaveAs(ActionEvent actionEvent) {
        System.out.println("save as");
    }

    @FXML
    private void onClose(ActionEvent actionEvent) {
        System.out.println("close");
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
}
