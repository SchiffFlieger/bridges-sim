package de.karstenkoehler.bridges.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class MainController {

    public ChoiceBox<String> islandDisplayChoice;
    public Label stateText;

    private Window stage;

    public void initialize() {
        islandDisplayChoice.getItems().addAll(
                "show required bridges", "show missing bridges"
        );
        islandDisplayChoice.getSelectionModel().select(0);
    }

    public void setStage(Window stage) {
        this.stage = stage;
    }

    public void onNewPuzzle(ActionEvent actionEvent) {
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

    public void onRestartPuzzle(ActionEvent actionEvent) {
        System.out.println("restart");
    }

    public void onOpenPuzzle(ActionEvent actionEvent) {
        System.out.println("open");
    }

    public void onSavePuzzle(ActionEvent actionEvent) {
        System.out.println("save");
    }

    public void onSaveAs(ActionEvent actionEvent) {
        System.out.println("save as");
    }

    public void onClose(ActionEvent actionEvent) {
        System.out.println("close");
    }

    public void onAbout(ActionEvent actionEvent) {
        System.out.println("about");
    }

    public void onNextBridge(ActionEvent actionEvent) {
        System.out.println("next bridge");
    }

    public void onSolve(ActionEvent actionEvent) {
        System.out.println("solve");
    }
}
