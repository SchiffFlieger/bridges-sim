package de.karstenkoehler.bridges.ui;

import de.karstenkoehler.bridges.model.PuzzleSpecification;
import de.karstenkoehler.bridges.ui.components.toast.ToastMessage;
import javafx.beans.value.ChangeListener;
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

        rbtnUseRandom.setTooltip(new Tooltip("Uses a random specification for the generated puzzle."));
        rbtnChooseParameters.setTooltip(new Tooltip("Choose the specification for the generated puzzle by yourself."));

        txtWidth.setTooltip(new Tooltip("The width of the puzzle. You can choose values from 4 to 25."));
        txtHeight.setTooltip(new Tooltip("The height of the puzzle. You can choose values from 4 to 25."));
        txtIslands.setTooltip(new Tooltip("The number of islands in the puzzle. You can choose values from 2 to (Width * Height) / 5."));

        cbxGenerateSolution.setTooltip(new Tooltip("If this box is checked, the generated puzzle is already solved."));
        cbxChooseNumOfIslands.setTooltip(new Tooltip("Check this box to choose the number of islands to generate. Otherwise the number of islands is chosen randomly."));

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
            int width = tryParseInt(txtWidth, "Width");
            int height = tryParseInt(txtHeight, "Height");

            if (!cbxChooseNumOfIslands.isSelected()) {
                this.specs = PuzzleSpecification.withBounds(solution, width, height);
                this.stage.close();
                return;
            }

            int islands = tryParseInt(txtIslands, "Number of islands");
            this.specs = PuzzleSpecification.withSpecs(solution, width, height, islands);
            this.stage.close();
        } catch (IllegalArgumentException | InputException e) {
            ToastMessage.show(this.stage, e.getMessage());
        }
    }

    private int tryParseInt(TextField field, String fieldName) throws InputException {
        if (field.getText().isEmpty()) {
            throw new InputException(fieldName + " is empty");
        }

        try {
            return Integer.parseInt(field.getText());
        } catch (IllegalArgumentException e) {
            throw new InputException(fieldName + " needs to be an integer");
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

    private static class InputException extends Exception {
        public InputException(String message) {
            super(message);
        }
    }
}
