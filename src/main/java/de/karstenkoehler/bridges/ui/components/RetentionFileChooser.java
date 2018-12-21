package de.karstenkoehler.bridges.ui.components;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.function.Function;

/**
 * An extension of the default javafx {@link FileChooser}. It remembers the last opened directory and start the
 * file chooser in this directory.
 */
public class RetentionFileChooser {
    private final FileChooser chooser;

    /**
     * Creates a new file chooser that remembers its last opened directory.
     */
    public RetentionFileChooser() {
        chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Bridges File", "*.bgs"));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
    }

    /**
     * Prompts the user to select an existing .bgs file to open.
     *
     * @param owner the parent stage of the file chooser
     * @return the chosen file
     */
    public File showOpenDialog(Window owner) {
        return callFunction(chooser::showOpenDialog, owner, "Open Bridges File");
    }

    /**
     * Prompts the user to select either an existing or a new file to save to.
     *
     * @param owner the parent stage of the file chooser
     * @return the chosen file
     */
    public File showSaveDialog(Window owner) {
        return callFunction(chooser::showSaveDialog, owner, "Save Bridges File");
    }

    /**
     * Calls the given function of the file chooser. If a file was chosen, the directory of the file
     * is set as starting point for the next usage of the file chooser.
     *
     * @param fun   the function of the file chooser to call
     * @param owner the parent stage of the file chooser
     * @param title the title of the file chooser stage
     * @return the chosen file
     */
    private File callFunction(Function<Window, File> fun, Window owner, String title) {
        chooser.setTitle(title);
        File file = fun.apply(owner);
        if (file != null) {
            chooser.setInitialDirectory(file.getParentFile());
        }
        return file;
    }
}
