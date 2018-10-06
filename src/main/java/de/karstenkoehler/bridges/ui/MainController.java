package de.karstenkoehler.bridges.ui;

import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class MainController {

    public ChoiceBox<String> islandDisplayChoice;
    public Label stateText;

    public void initialize() {
        islandDisplayChoice.getItems().addAll(
                "show required bridges", "show missing bridges"
        );
        islandDisplayChoice.getSelectionModel().select(0);
    }

    public void onNewPuzzle(ActionEvent actionEvent) {
        System.out.println("new");
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
