package de.karstenkoehler.bridges.ui;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.model.PuzzleSpecification;
import de.karstenkoehler.bridges.ui.components.GeneratePuzzleTask;
import de.karstenkoehler.bridges.ui.components.PuzzleChangeEvent;
import de.karstenkoehler.bridges.ui.components.toast.ToastMessage;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class NewPuzzleController {
    @FXML
    private ProgressBar progress;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnOk;

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
    private Node node;

    @FXML
    private void initialize() {
        this.rbtnUseRandom.selectedProperty().addListener(useRandomSelected());
        this.rbtnChooseParameters.selectedProperty().addListener(chooseParametersSelected());
        this.cbxChooseNumOfIslands.selectedProperty().addListener(numOfIslandsSelected());

        makeConstraints(txtWidth);
        makeConstraints(txtHeight);
        makeConstraints(txtIslands);

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
            generateAndClose(PuzzleSpecification.random(solution));
            return;
        }

        try {
            int width = tryParseInt(txtWidth, "Width");
            int height = tryParseInt(txtHeight, "Height");

            if (!cbxChooseNumOfIslands.isSelected()) {
                generateAndClose(PuzzleSpecification.withBounds(solution, width, height));
                return;
            }

            int islands = tryParseInt(txtIslands, "Number of islands");
            generateAndClose(PuzzleSpecification.withSpecs(solution, width, height, islands));
        } catch (IllegalArgumentException | InputException e) {
            ToastMessage.show(this.stage, ToastMessage.Type.ERROR, e.getMessage());
        }
    }

    private void generateAndClose(PuzzleSpecification specs) {
        disableControls();

        Task<BridgesPuzzle> task = new GeneratePuzzleTask(specs);
        task.onSucceededProperty().set(event -> {
            this.node.fireEvent(new PuzzleChangeEvent(MainController.CHANGE_PUZZLE, task.getValue()));
            this.stage.close();
            enableControls();
        });


        new Thread(task).start();
    }

    private void disableControls() {
        this.progress.setVisible(true);

        this.btnOk.setDisable(true);
        this.btnCancel.setDisable(true);
        this.txtWidth.setDisable(true);
        this.txtHeight.setDisable(true);
        this.txtIslands.setDisable(true);
        this.rbtnUseRandom.setDisable(true);
        this.rbtnChooseParameters.setDisable(true);
        this.cbxChooseNumOfIslands.setDisable(true);
        this.cbxGenerateSolution.setDisable(true);
        this.lblWidth.setDisable(true);
        this.lblHeight.setDisable(true);
    }

    private void enableControls() {
        this.progress.setVisible(false);

        this.btnOk.setDisable(false);
        this.btnCancel.setDisable(false);
        this.cbxGenerateSolution.setDisable(false);
        this.rbtnUseRandom.setDisable(false);
        this.rbtnChooseParameters.setDisable(false);

        if (rbtnChooseParameters.isSelected()) {
            this.cbxChooseNumOfIslands.setDisable(false);
            this.lblWidth.setDisable(false);
            this.lblHeight.setDisable(false);
            this.txtWidth.setDisable(false);
            this.txtHeight.setDisable(false);
            this.txtIslands.setDisable(false);
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
        stage.close();
        enableControls();
    }

    public void setDependencies(Stage stage, Node node) {
        this.stage = stage;
        this.node = node;

        stage.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                this.onOk();
            }
        });
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

    private void makeConstraints(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 4) {
                field.setText(oldValue);
            }
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
