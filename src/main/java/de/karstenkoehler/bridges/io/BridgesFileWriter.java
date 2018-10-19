package de.karstenkoehler.bridges.io;

import de.karstenkoehler.bridges.io.serializer.Serializer;
import de.karstenkoehler.bridges.io.serializer.SerializerImpl;
import de.karstenkoehler.bridges.model.BridgesPuzzle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BridgesFileWriter {
    private final Serializer serializer;

    public BridgesFileWriter() {
        serializer = new SerializerImpl();
    }

    public void writeFile(File file, BridgesPuzzle puzzle) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(serializer.serialize(puzzle));
        writer.close();
    }
}
