package de.karstenkoehler.bridges.ui.components.toast;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Controller class for the view defined in toast.fxml.
 */
public class ToastController {

    @FXML
    private ImageView image;
    @FXML
    private Label lblMessage;

    /**
     * Sets the icon for the toast message.
     *
     * @param image the new icon
     */
    void setImage(Image image) {
        this.image.setImage(image);
    }

    /**
     * Sets the message for the toast message.
     *
     * @param message the new message
     */
    void setMessage(final String message) {
        lblMessage.setText(message);
    }
}
