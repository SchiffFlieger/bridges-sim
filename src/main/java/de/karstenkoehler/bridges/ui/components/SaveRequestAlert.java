package de.karstenkoehler.bridges.ui.components;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class SaveRequestAlert {
    private final Alert alert;
    private final ButtonType saveButton;
    private final ButtonType saveAsButton;
    private final ButtonType dontSaveButton;
    private final ButtonType cancelButton;

    public SaveRequestAlert() {
        saveButton = new ButtonType("Save", ButtonBar.ButtonData.YES);
        saveAsButton = new ButtonType("Save As...", ButtonBar.ButtonData.YES);
        dontSaveButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Save Changes?");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.getButtonTypes().setAll(saveButton, saveAsButton, dontSaveButton, cancelButton);
    }

    public SaveAction showAndWait(String filename) {
        alert.setContentText(filename + " has been modified, save changes?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            ButtonType buttonType = result.get();
            if (buttonType.equals(saveButton)) {
                return SaveAction.Save;
            } else if (buttonType.equals(saveAsButton)) {
                return SaveAction.SaveAs;
            } else if (buttonType.equals(dontSaveButton)) {
                return SaveAction.DontSave;
            } else if (buttonType.equals(cancelButton)) {
                return SaveAction.Cancel;
            }
        }
        throw new RuntimeException("fatal error: unexpected dialog result");
    }
}
