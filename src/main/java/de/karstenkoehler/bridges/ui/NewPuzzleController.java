package de.karstenkoehler.bridges.ui;

import de.karstenkoehler.bridges.model.PuzzleSpecification;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class NewPuzzleController {

    @FXML
    private RadioButton rbtnUseRandom;
    @FXML
    private RadioButton rbtnChooseParameters;
    @FXML
    private CheckBox cbxChooseNumOfIslands;
    @FXML
    private CheckBox cbxGenerateSolution;
    @FXML
    private TextField txtWidth;
    @FXML
    private TextField txtHeight;
    @FXML
    private TextField txtIslands;
    @FXML
    private Label lblWidth;
    @FXML
    private Label lblHeight;

    private Stage stage;
    private PuzzleSpecification specs;

    @FXML
    private void initialize() {
        this.rbtnUseRandom.selectedProperty().addListener(useRandomSelected());
        this.rbtnChooseParameters.selectedProperty().addListener(chooseParametersSelected());
        this.cbxChooseNumOfIslands.selectedProperty().addListener(numOfIslandsSelected());

        numbersOnly(txtWidth);
        numbersOnly(txtHeight);
        numbersOnly(txtIslands);
    }

    @FXML
    private void onOk() {
        boolean solution = this.cbxGenerateSolution.isSelected();

        if (rbtnUseRandom.isSelected()) {
            this.specs = PuzzleSpecification.random(solution);
            this.stage.close();
            return;
        }

        try {
            int width = Integer.parseInt(txtWidth.getText());
            int height = Integer.parseInt(txtHeight.getText());

            if (!cbxChooseNumOfIslands.isSelected()) {
                this.specs = PuzzleSpecification.withBounds(solution, width, height);
                this.stage.close();
                return;
            }

            int islands = Integer.parseInt(txtIslands.getText());
            this.specs = PuzzleSpecification.withSpecs(solution, width, height, islands);
            this.stage.close();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onCancel() {
        this.specs = null;
        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setOnCloseRequest(event -> this.specs = null);


        stage.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                this.onOk();
            }
        });
    }

    public PuzzleSpecification getSpecs() {
        return specs;
    }

    private ChangeListener<Boolean> useRandomSelected() {
        return (observable, old, selected) -> {
            if (selected) {
                setElementsDisabled(true);
            }
        };
    }

    private ChangeListener<Boolean> chooseParametersSelected() {
        return (observable, old, selected) -> {
            if (selected) {
                setElementsDisabled(false);
            }
        };
    }

    private ChangeListener<Boolean> numOfIslandsSelected() {
        return ((observable, old, selected) -> txtIslands.setDisable(!selected));
    }

    private void setElementsDisabled(boolean value) {
        cbxChooseNumOfIslands.setDisable(value);
        txtWidth.setDisable(value);
        txtHeight.setDisable(value);
        txtIslands.setDisable(value);
        lblWidth.setDisable(value);
        lblHeight.setDisable(value);
    }

    private void numbersOnly(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                field.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
}
