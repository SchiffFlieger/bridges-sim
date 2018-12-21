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

/**
 * The stage for the 'new puzzle' dialog.
 */
public class NewPuzzleStage {

    private final Stage dialogStage;

    /**
     * Creates a new instance of the dialog stage and sets all required properties.
     *
     * @param mainStage the parent stage
     */
    public NewPuzzleStage(Stage mainStage) {
        this.dialogStage = new Stage();
        this.dialogStage.initOwner(mainStage);
        this.dialogStage.initModality(Modality.WINDOW_MODAL);
        this.dialogStage.resizableProperty().setValue(false);
        this.dialogStage.getIcons().add(new Image(NewPuzzleStage.class.getResourceAsStream("/ui/icon.png")));
        this.dialogStage.setTitle("Create New Puzzle");
    }

    /**
     * Initializes the dialog stage with the FXML controls and binds them to the controller.
     *
     * @param node a node of the scene to fire events to
     */
    public void init(Node node) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/new.fxml"));
            Parent root = loader.load();
            NewPuzzleController controller = loader.getController();
            controller.setDependencies(dialogStage, node);
            dialogStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Display the dialog stage.
     */
    public void showAndWait() {
        dialogStage.showAndWait();
    }
}
