package de.karstenkoehler.bridges.io;

import de.karstenkoehler.bridges.io.serializer.Serializer;
import de.karstenkoehler.bridges.io.serializer.SerializerImpl;
import de.karstenkoehler.bridges.model.BridgesPuzzle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A bridges file writer can write a puzzle to a file on the hard drive. For serializing the puzzles this class
 * utilizes an instance of {@link SerializerImpl}.
 */
public class BridgesFileWriter {
    private final Serializer serializer;

    public BridgesFileWriter() {
        serializer = new SerializerImpl();
    }

    /**
     * Serializes the given puzzle and writes the string to the specified file.
     *
     * @param file   the file to write to
     * @param puzzle the puzzle to serialize
     * @throws IOException if there where issues writing the file
     */
    public void writeFile(File file, BridgesPuzzle puzzle) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(serializer.serialize(puzzle));
        }
    }
}
