package de.karstenkoehler.bridges.ui;

import de.karstenkoehler.bridges.io.BridgesFileReader;
import de.karstenkoehler.bridges.io.BridgesFileWriter;
import de.karstenkoehler.bridges.io.parser.ParseException;
import de.karstenkoehler.bridges.io.validator.ValidateException;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.ui.components.ErrorAlert;
import de.karstenkoehler.bridges.ui.components.RetentionFileChooser;
import de.karstenkoehler.bridges.ui.components.SaveAction;
import de.karstenkoehler.bridges.ui.components.SaveRequestAlert;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class FileHelper {
    private final RetentionFileChooser chooser;
    private final SaveRequestAlert saveRequest;
    private final StringProperty filename;
    private final ObjectProperty<File> current;

    private final ErrorAlert error;
    private final BridgesFileReader reader;
    private final BridgesFileWriter writer;

    private Stage stage;
    private boolean modified;

    public FileHelper() {
        this.chooser = new RetentionFileChooser();
        this.saveRequest = new SaveRequestAlert();
        this.filename = new SimpleStringProperty();
        this.current = new SimpleObjectProperty<>();

        this.error = new ErrorAlert();
        this.reader = new BridgesFileReader();
        this.writer = new BridgesFileWriter();

        this.modified = false;

        filename.bind(current.asString());
    }

    /**
     * Shows a file open dialog and then tries to read the chosen file. If there are any errors opening the file
     * or the user cancelled the dialog, this method returns an empty optional.
     * @return the loaded puzzle
     */
    public Optional<BridgesPuzzle> openFile() {
        File toLoad = chooser.showOpenDialog(stage);
        if (toLoad != null) {
            return tryReadFile(toLoad);
        }
        return Optional.empty();
    }

    /**
     * Asks for a file location and then saves the puzzle to that file. If the user cancels the file save dialog,
     * the puzzle does not get saved.
     * @param puzzle the puzzle to save
     */
    public void saveToNewFile(BridgesPuzzle puzzle) {
        this.current.setValue(chooser.showSaveDialog(this.stage));
        if (this.current.isNotNull().get()) {
            saveToFile(puzzle, current.get());
        }
    }

    /**
     * Saves the puzzle to its original location. If the puzzle was newly generated or the
     * location is not known, a dialog will be shown.
     * @param puzzle the puzzle to save
     */
    public void saveToCurrentFile(BridgesPuzzle puzzle) {
        if (this.current.isNull().get()) {
            this.current.setValue(chooser.showSaveDialog(this.stage));
        }
        if (this.current.isNotNull().get()) {
            saveToFile(puzzle, current.get());
        }
    }

    /**
     * If the current puzzle has unsaved changes, this method shows a save request to the user. If requested
     * the puzzle is saved to the desired location.
     * @param puzzle the puzzle to save
     */
    public void saveIfNecessary(BridgesPuzzle puzzle) {
        if (this.modified) {
            String filename = getFilename();
            SaveAction action = this.saveRequest.showAndWait(filename);
            if (action == SaveAction.Save) {
                saveToCurrentFile(puzzle);
            } else if (action == SaveAction.SaveAs) {
                saveToNewFile(puzzle);
            }
        }
    }

    /**
     * Attempts to read the given file. Catches several errors and shows them to the user. If there are
     * any errors, an empty optional is returned.
     * @param file the file to read
     * @return the loaded puzzle
     */
    private Optional<BridgesPuzzle> tryReadFile(File file) {
        try {
            BridgesPuzzle puzzle = reader.readFile(file);
            this.current.setValue(file);
            this.modified = false;
            return Optional.ofNullable(puzzle);
        } catch (ParseException e) {
            this.error.showAndWait("syntactic error in file:\n" + e.getMessage());
        } catch (ValidateException e) {
            this.error.showAndWait("semantic error in file:\n" + e.getMessage());
        } catch (IOException e) {
            this.error.showAndWait("could not read file:\n" + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Saves the puzzle to the given file location. If any errors occur, they are shown to the user.
     * @param puzzle the puzzle to save
     * @param file the location to save to
     */
    private void saveToFile(BridgesPuzzle puzzle, File file) {
        try {
            writer.writeFile(file, puzzle);
            this.modified = false;
        } catch (IOException e) {
            this.error.showAndWait("could not write file:\n" + e.getMessage());
        }
    }

    /**
     * Returns the name of the currently opened puzzle. The name of the puzzle corresponds to the file
     * the puzzle is saved to. If the puzzle was newly created or the file name is not known, this method
     * returns 'New file' instead.
     * @return the name of the puzzle
     */
    private String getFilename() {
        if (this.current.isNull().get()) {
            return "New file";
        }
        return this.current.get().getName();
    }

    public void fileModified() {
        this.modified = true;
    }

    public StringProperty filenameProperty() {
        return filename;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // TODO allow empty puzzle in canvas controller, then remove this method
    public Optional<BridgesPuzzle> openInitialFile(File toLoad) {
        if (toLoad != null) {
            return tryReadFile(toLoad);
        }
        return Optional.empty();
    }
}
