package de.karstenkoehler.bridges.ui;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.function.Function;

public class RetentionFileChooser {
    private final FileChooser chooser;

    public RetentionFileChooser() {
        chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Bridges File", "*.bgs"));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
    }

    public File showOpenDialog(Window owner) {
        return callFunction(chooser::showOpenDialog, owner, "Open Bridges File");
    }

    public File showSaveDialog(Window owner) {
        return callFunction(chooser::showSaveDialog, owner, "Save Bridges File");
    }

    private File callFunction(Function<Window, File> fun, Window owner, String title) {
        chooser.setTitle(title);
        File file = fun.apply(owner);
        if (file != null) {
            chooser.setInitialDirectory(file.getParentFile());
        }
        return file;
    }
}
