package de.karstenkoehler.bridges.ui.components;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * A custom dialog for warning the user about unsaved changes.
 */
public class SaveRequest {
    private final Alert alert;
    private final ButtonType yesButton;
    private final ButtonType noButton;
    private final ButtonType cancelButton;

    /**
     * Creates a new instance of the dialog.
     */
    public SaveRequest() {
        yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Save Changes?");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.getButtonTypes().setAll(yesButton, noButton, cancelButton);

        Image icon = new Image(AboutDialog.class.getResourceAsStream("/ui/icon.png"));
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(icon);

    }

    /**
     * Prompts the user if he wants to save his changes to a file.
     *
     * @param filename the name of the file
     * @return the selection made by the user
     */
    public SaveAction showAndWait(String filename) {
        alert.setContentText(filename + " has been modified, save changes?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            ButtonType buttonType = result.get();
            if (buttonType.equals(yesButton)) {
                return SaveAction.SAVE;
            } else if (buttonType.equals(noButton)) {
                return SaveAction.DONT_SAVE;
            } else if (buttonType.equals(cancelButton)) {
                return SaveAction.CANCEL;
            }
        }
        return null;
    }

    /**
     * Represents the action the user wants to take when prompted to save his changes.
     */
    public enum SaveAction {
        SAVE, DONT_SAVE, CANCEL
    }
}
