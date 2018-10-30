package de.karstenkoehler.bridges.ui.components;

import de.karstenkoehler.bridges.model.PuzzleSpecification;
import de.karstenkoehler.bridges.ui.NewPuzzleController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class NewPuzzleStage {

    private final Stage dialogStage;
    private NewPuzzleController controller;

    public NewPuzzleStage(Stage mainStage) {
        this.dialogStage = new Stage();
        this.dialogStage.initOwner(mainStage);
        this.dialogStage.initModality(Modality.WINDOW_MODAL);
        this.dialogStage.resizableProperty().setValue(false);
        this.dialogStage.setTitle("Create new puzzle");
    }

    public void init() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/new.fxml"));
        Parent root = loader.load();
        this.controller = loader.getController();
        this.controller.setStage(dialogStage);

        dialogStage.setScene(new Scene(root));
    }

    public Optional<PuzzleSpecification> showAndWait() {

        dialogStage.showAndWait();
        return Optional.ofNullable(this.controller.getSpecs());
    }
}
