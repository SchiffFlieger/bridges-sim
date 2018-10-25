package de.karstenkoehler.bridges.ui;

import de.karstenkoehler.bridges.io.BridgesFileReader;
import de.karstenkoehler.bridges.io.BridgesFileWriter;
import de.karstenkoehler.bridges.io.parser.ParseException;
import de.karstenkoehler.bridges.io.validator.ValidateException;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.ui.components.ErrorAlert;
import de.karstenkoehler.bridges.ui.components.RetentionFileChooser;
import de.karstenkoehler.bridges.ui.components.SaveAction;
import de.karstenkoehler.bridges.ui.components.YesNoCancelAlert;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class FileHelper {
    private final RetentionFileChooser chooser;
    private final YesNoCancelAlert saveRequest;
    private final StringProperty titleFilename;
    private final StringProperty filename;
    private final ObjectProperty<File> file;
    private final BooleanProperty modified;


    private final ErrorAlert error;
    private final BridgesFileReader reader;
    private final BridgesFileWriter writer;

    private Stage stage;

    public FileHelper() {
        this.chooser = new RetentionFileChooser();
        this.saveRequest = new YesNoCancelAlert();
        this.file = new SimpleObjectProperty<>();

        this.error = new ErrorAlert();
        this.reader = new BridgesFileReader();
        this.writer = new BridgesFileWriter();

        this.modified = new SimpleBooleanProperty(false);

        this.filename = new SimpleStringProperty();
        this.filename.bind(new FilenameBinding(this.file));

        this.titleFilename = new SimpleStringProperty();
        this.titleFilename.bind(Bindings.concat(filename, new FileChangedIndicator(modified)));
    }

    /**
     * Shows a file open dialog and then tries to read the chosen file. If there are any errors opening the file
     * or the user cancelled the dialog, this method returns an empty optional.
     *
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
     *
     * @param puzzle the puzzle to save
     */
    public void saveToNewFile(BridgesPuzzle puzzle) {
        this.file.setValue(chooser.showSaveDialog(this.stage));
        if (this.file.isNotNull().get()) {
            saveToFile(puzzle, file.get());
        }
    }

    /**
     * Saves the puzzle to its original location. If the puzzle was newly generated or the
     * location is not known, a dialog will be shown.
     *
     * @param puzzle the puzzle to save
     */
    public void saveToCurrentFile(BridgesPuzzle puzzle) {
        if (this.file.isNull().get()) {
            this.file.setValue(chooser.showSaveDialog(this.stage));
        }
        if (this.file.isNotNull().get()) {
            saveToFile(puzzle, file.get());
        }
    }

    /**
     * If the file puzzle has unsaved changes, this method shows a save request to the user. If requested
     * the puzzle is saved to the desired location.
     *
     * @param puzzle the puzzle to save
     */
    public SaveAction saveIfNecessary(BridgesPuzzle puzzle) {
        if (this.modified.get()) {
            SaveAction action = this.saveRequest.showAndWait(this.filename.get());
            if (action == SaveAction.SAVE) {
                saveToCurrentFile(puzzle);
            } else if (action == SaveAction.CANCEL) {
                return action;
            }
        }
        return null;
    }

    /**
     * Attempts to read the given file. Catches several errors and shows them to the user. If there are
     * any errors, an empty optional is returned.
     *
     * @param file the file to read
     * @return the loaded puzzle
     */
    private Optional<BridgesPuzzle> tryReadFile(File file) {
        try {
            BridgesPuzzle puzzle = reader.readFile(file);
            this.file.setValue(file);
            this.modified.setValue(false);
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
     *
     * @param puzzle the puzzle to save
     * @param file   the location to save to
     */
    private void saveToFile(BridgesPuzzle puzzle, File file) {
        try {
            writer.writeFile(file, puzzle);
            this.modified.setValue(false);
        } catch (IOException e) {
            this.error.showAndWait("could not write file:\n" + e.getMessage());
        }
    }

    public void fileModified() {
        this.modified.setValue(true);
    }

    public StringProperty titleFilenameProperty() {
        return titleFilename;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void resetFile() {
        this.file.setValue(null);
        this.modified.setValue(true);
    }

    // TODO allow empty puzzle in canvas controller, then remove this method
    public Optional<BridgesPuzzle> openInitialFile(File toLoad) {
        if (toLoad != null) {
            return tryReadFile(toLoad);
        }
        return Optional.empty();
    }

    private static class FileChangedIndicator extends StringBinding {
        private final BooleanProperty modified;

        FileChangedIndicator(BooleanProperty modified) {
            this.modified = modified;
            this.bind(modified);
        }

        @Override
        protected String computeValue() {
            return modified.get() ? "*" : "";
        }
    }

    private static class FilenameBinding extends StringBinding {
        private final ObjectProperty<File> file;

        FilenameBinding(ObjectProperty<File> file) {
            this.file = file;
            this.bind(file);
        }

        @Override
        protected String computeValue() {
            return file.get() != null ? file.asString().get() : "New file";
        }
    }
}
