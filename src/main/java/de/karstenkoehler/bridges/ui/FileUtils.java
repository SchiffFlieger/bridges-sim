package de.karstenkoehler.bridges.ui;

import de.karstenkoehler.bridges.io.BridgesFileReader;
import de.karstenkoehler.bridges.io.BridgesFileWriter;
import de.karstenkoehler.bridges.io.parser.ParseException;
import de.karstenkoehler.bridges.io.validator.ValidateException;
import de.karstenkoehler.bridges.model.BridgesPuzzle;
import de.karstenkoehler.bridges.ui.components.RetentionFileChooser;
import de.karstenkoehler.bridges.ui.components.SaveRequest;
import de.karstenkoehler.bridges.ui.components.toast.ToastMessage;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * A utility class for handling opening and saving of files. It is responsible
 * for showing warnings if the user is about to discard unsaved changes.
 */
public class FileUtils {
    private final RetentionFileChooser chooser;
    private final SaveRequest saveRequest;
    private final StringProperty titleFilename;
    private final StringProperty filename;
    private final ObjectProperty<File> file;
    private final BooleanProperty modified;


    private final BridgesFileReader reader;
    private final BridgesFileWriter writer;

    private Stage stage;

    /**
     * Creates a new instance of file utils.
     */
    public FileUtils() {
        this.chooser = new RetentionFileChooser();
        this.saveRequest = new SaveRequest();
        this.file = new SimpleObjectProperty<>();

        this.reader = new BridgesFileReader();
        this.writer = new BridgesFileWriter();

        this.modified = new SimpleBooleanProperty(false);

        this.filename = new SimpleStringProperty();
        this.filename.bind(new FilenameBinding(this.file));

        this.titleFilename = new SimpleStringProperty();
        this.titleFilename.bind(Bindings.concat(filename, new FileChangedIndicator(modified)));
    }

    /**
     * Sets the stage of the application's main window.
     *
     * @param stage the main stage of the application
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Indicates that the currently opened file was modified.
     */
    public void fileModified() {
        this.modified.setValue(true);
    }

    /**
     * Indicates that a completely new file is being opened that does not yet
     * have a filename associated with it.
     */
    public void setNewFile() {
        this.file.setValue(null);
        this.modified.setValue(true);
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
     * Opens the first file to show on startup of the application.
     *
     * @param toLoad the .bgs file to load
     * @return the bridges puzzle loaded from the file
     */
    public Optional<BridgesPuzzle> openInitialFile(File toLoad) {
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
    public SaveRequest.SaveAction saveIfNecessary(BridgesPuzzle puzzle) {
        if (this.modified.get()) {
            SaveRequest.SaveAction action = this.saveRequest.showAndWait(this.filename.get());
            if (action == SaveRequest.SaveAction.SAVE) {
                saveToCurrentFile(puzzle);
            } else if (action == SaveRequest.SaveAction.CANCEL) {
                return action;
            }
        }
        return null;
    }

    /**
     * A string property that holds the value to display in the application's title bar.
     *
     * @return the title string property
     */
    public StringProperty titleFilenameProperty() {
        return titleFilename;
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
            ToastMessage.show(this.stage, ToastMessage.Type.ERROR, "Could not write file:\n" + e.getMessage());
        }
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
        } catch (ValidateException | ParseException e) {
            ToastMessage.show(this.stage, ToastMessage.Type.ERROR, "Error in file:\n" + e.getMessage());
        } catch (IOException e) {
            ToastMessage.show(this.stage, ToastMessage.Type.ERROR, "Could not read file:\n" + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * A string property that binds to a boolean property. If the boolean value is <code>true</code>, the
     * string property holds a single asterisk. If the boolean value is <code>false</code>, the string
     * property is empty. Is used to indicate unsaved changes in the title bar of the application.
     */
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

    /**
     * A string property that binds to an object property. The object property contains the currently
     * opened bridges puzzle file. The string property contains the filename of that file. If the file
     * is <code>null</code>, which means there is no saved file associated with the opened puzzle, the
     * string property contains the value 'New file'.
     */
    private static class FilenameBinding extends StringBinding {
        private final ObjectProperty<File> file;

        FilenameBinding(ObjectProperty<File> file) {
            this.file = file;
            this.bind(file);
        }

        @Override
        protected String computeValue() {
            return file.get() != null ? file.get().getName() : "New file";
        }
    }

}
