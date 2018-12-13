package de.karstenkoehler.bridges.ui.components;

import javafx.scene.control.Alert;

public class AboutAlert {

    private static Alert alert;

    private static Alert create() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setGraphic(null);
        alert.setHeaderText(null);
        alert.setContentText("BridgesSim 0.5\nMade by Karsten Köhler");

        return alert;
    }

    public static void showAndWait() {
        if (alert == null) {
            alert = create();
        }

        alert.showAndWait();
    }
}
