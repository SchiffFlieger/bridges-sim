package de.karstenkoehler.bridges.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class NewPuzzleController {

    @FXML
    private RadioButton useRandom;
    @FXML
    private RadioButton chooseParameters;
    @FXML
    private CheckBox chooseNumOfIslands;
    @FXML
    private TextField width;
    @FXML
    private TextField height;
    @FXML
    private TextField islands;


    @FXML
    private void onOk(ActionEvent actionEvent) {
        System.out.println("ok");
    }

    @FXML
    private void onCancel(ActionEvent actionEvent) {
        System.out.println("cancel");
    }
}
