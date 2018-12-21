package de.karstenkoehler.bridges.ui.components;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * A class that encapsulates the about dialog of the application. Use the static method
 * {@link AboutDialog#showAndWait()} to display the dialog.
 */
public class AboutDialog {

    private static Alert alert;

    private static Alert create() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setGraphic(null);
        alert.setHeaderText(null);
        alert.setContentText("BridgesSim 0.5\nMade by Karsten Köhler");

        Image icon = new Image(AboutDialog.class.getResourceAsStream("/ui/icon.png"));
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(icon);

        return alert;
    }

    /**
     * Display the about information of the application.
     */
    public static void showAndWait() {
        if (alert == null) {
            alert = create();
        }

        alert.showAndWait();
    }
}
