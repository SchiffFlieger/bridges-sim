package de.karstenkoehler.bridges.ui.components;

import de.karstenkoehler.bridges.ui.NewPuzzleController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class NewPuzzleStage {

    private final Stage dialogStage;

    public NewPuzzleStage(Stage mainStage) {
        this.dialogStage = new Stage();
        this.dialogStage.initOwner(mainStage);
        this.dialogStage.initModality(Modality.WINDOW_MODAL);
        this.dialogStage.resizableProperty().setValue(false);
        this.dialogStage.getIcons().add(new Image(NewPuzzleStage.class.getResourceAsStream("/ui/icon.png")));
        this.dialogStage.setTitle("Create New Puzzle");
    }

    public void init(Node node) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/new.fxml"));
        Parent root = loader.load();
        NewPuzzleController controller = loader.getController();
        controller.setDependencies(dialogStage, node);
        dialogStage.setScene(new Scene(root));
    }

    public void showAndWait() {
        dialogStage.showAndWait();
    }
}
