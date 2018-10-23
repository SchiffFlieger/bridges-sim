package de.karstenkoehler.bridges.ui.components;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class ErrorAlert {
private final Alert alert;

    public ErrorAlert() {
        this.alert = new Alert(Alert.AlertType.ERROR);
        this.alert.setTitle("Error");
        this.alert.setHeaderText(null);
    }

    public Optional<ButtonType> showAndWait(String content) {
        this.alert.setContentText(content);
        return alert.showAndWait();
    }
}
