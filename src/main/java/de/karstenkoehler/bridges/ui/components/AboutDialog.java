package de.karstenkoehler.bridges.ui.components;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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

    public static void showAndWait() {
        if (alert == null) {
            alert = create();
        }

        alert.showAndWait();
    }
}
