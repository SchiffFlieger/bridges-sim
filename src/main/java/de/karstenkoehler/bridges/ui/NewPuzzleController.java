package de.karstenkoehler.bridges.ui;

import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class NewPuzzleController {

    public RadioButton useRandom;
    public RadioButton chooseParameters;
    public CheckBox chooseNumOfIslands;
    public TextField width;
    public TextField height;
    public TextField islands;

    public void onOk(ActionEvent actionEvent) {
        System.out.println("ok");
    }

    public void onCancel(ActionEvent actionEvent) {
        System.out.println("cancel");
    }
}
