package de.karstenkoehler.bridges.ui.components.toast;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ToastController {

    @FXML
    private ImageView image;
    @FXML
    private Label lblMessage;

    public void setImage(Image image) {
        this.image.setImage(image);
    }

    public void setMessage(final String message) {
        lblMessage.setText(message);
    }
}
