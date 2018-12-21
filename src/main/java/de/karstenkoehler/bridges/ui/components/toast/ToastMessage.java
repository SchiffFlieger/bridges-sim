package de.karstenkoehler.bridges.ui.components.toast;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Displays an Android-style toast message for 2.5 seconds. The constructor is private,
 * use the static method {@link ToastMessage#show(Stage, Type, String)} instead.
 */
public class ToastMessage {

    /**
     * Creates and displays a new toast message.
     *
     * @param parent  the parent stage of the toast message
     * @param type    the type of the message
     * @param message the message text
     */
    private ToastMessage(Stage parent, Type type, String message) {
        Stage stage = createStage(parent);
        Parent root = createRootPane(type, message);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        SequentialTransition sequence = createAnimation(stage, scene);
        sequence.play();

        stage.setScene(scene);

        addListenerForPositioning(parent, stage);
        stage.show();
        parent.requestFocus();
    }

    /**
     * Ensures that the location of the toast on the screen is relative to its parent.
     *
     * @param parent the parent stage of the toast message
     * @param toast  the stage of the toast message
     */
    private void addListenerForPositioning(Stage parent, Stage toast) {
        ChangeListener<Number> widthListener = (observable, oldValue, width) -> {
            toast.setX((parent.getX() + parent.getWidth() * 0.5) - (width.doubleValue() / 2));
        };
        ChangeListener<Number> heightListener = (observable, oldValue, height) -> {
            toast.setY((parent.getY() + parent.getHeight() * 0.9) - (height.doubleValue() / 2));
        };

        toast.widthProperty().addListener(widthListener);
        toast.heightProperty().addListener(heightListener);

        toast.setOnShown(e -> {
            toast.widthProperty().removeListener(widthListener);
            toast.heightProperty().removeListener(heightListener);
        });
    }

    /**
     * Creates the stage for the toast message and sets its properties.
     *
     * @param parent the parent stage of the toast message
     * @return the stage for the toast message
     */
    private Stage createStage(Stage parent) {
        Stage stage = new Stage(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.initOwner(parent);
        return stage;
    }

    private SequentialTransition createAnimation(Stage stage, Scene scene) {
        Timeline wait = new Timeline(new KeyFrame(Duration.millis(2500)));
        Timeline fade = new Timeline(new KeyFrame(Duration.millis(200), new KeyValue(scene.getRoot().opacityProperty(), 0)));

        SequentialTransition sequence = new SequentialTransition(wait, fade);
        sequence.setOnFinished(event -> stage.close());
        return sequence;
    }

    /**
     * Loads the FXML description and binds it to its controller.
     *
     * @param type    the type of the message
     * @param message the message text
     * @return the root pane of the toast message
     */
    private Parent createRootPane(Type type, String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/toast.fxml"));
            Parent root = loader.load();
            ToastController controller = loader.getController();
            controller.setImage(getImage(type));
            controller.setMessage(message);
            return root;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Label(message);
    }

    /**
     * Return the corresponding icon for the given message type.
     *
     * @param type the type of the message
     * @return the corresponding icon for the message type
     */
    private Image getImage(Type type) {
        if (type == Type.INFO) {
            return new Image(ToastMessage.class.getResourceAsStream("/ui/info.png"));
        } else if (type == Type.ERROR) {
            return new Image(ToastMessage.class.getResourceAsStream("/ui/error.png"));
        }

        return null;
    }

    /**
     * Displays an Android-style toast message in the bottom center of the parent stage for 2.5 seconds.
     *
     * @param parent  the parent of the toast message
     * @param type    the type of the toast message
     * @param message the message text
     */
    public static void show(Stage parent, Type type, String message) {
        new ToastMessage(parent, type, message);
    }

    /**
     * The type of the toast message. Changes the icon that is shown with the message.
     */
    public enum Type {
        ERROR, INFO
    }
}
