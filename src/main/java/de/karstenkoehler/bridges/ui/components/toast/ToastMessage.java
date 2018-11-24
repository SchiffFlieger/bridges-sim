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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class ToastMessage {
    private ToastMessage(Stage parent, final String message) {
        Stage stage = createStage(parent);
        Parent root = createRootPane(message);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        SequentialTransition sequence = createAnimation(stage, scene);
        sequence.play();

        stage.setScene(scene);

        addListenerForPositioning(parent, stage);
        stage.show();
        stage.show();
    }

    /**
     * Ensures that the location of the toast on the screen is relative to its parent.
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

    private Parent createRootPane(String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/toast.fxml"));
            Parent root = loader.load();
            ToastController controller = loader.getController();
            controller.setMessage(message);
            return root;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Label(message);
    }

    public static void show(Stage parent, String message) {
        new ToastMessage(parent, message);
    }
}
