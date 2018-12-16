package de.karstenkoehler.bridges;

import de.karstenkoehler.bridges.ui.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main_Koehler_Karsten extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = createRootPane(stage);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.getIcons().add(new Image(Main_Koehler_Karsten.class.getResourceAsStream("/ui/icon.png")));
        stage.show();
    }

    private Parent createRootPane(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/main.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();
        controller.setMainStage(stage);
        return root;
    }
}
