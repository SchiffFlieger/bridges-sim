package de.karstenkoehler.bridges.ui.components;

import de.karstenkoehler.bridges.ui.NewPuzzleController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        this.dialogStage.setTitle("Create new puzzle");
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
