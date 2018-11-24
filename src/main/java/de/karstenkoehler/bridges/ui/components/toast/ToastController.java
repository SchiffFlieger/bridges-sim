package de.karstenkoehler.bridges.ui.components.toast;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ToastController {

    @FXML
    private Label lblMessage;

    public void setMessage(final String message) {
        lblMessage.setText(message);
    }
}
