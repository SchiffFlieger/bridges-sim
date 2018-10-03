package de.karstenkoehler.bridges;

import de.karstenkoehler.bridges.io.DefaultBridgesParser;
import de.karstenkoehler.bridges.io.ParseException;
import de.karstenkoehler.bridges.io.Parser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main_Koehler_Karsten {
    public static void main(String[] args) throws IOException, ParseException {
        Parser parser = new DefaultBridgesParser(readFile("src\\main\\resources\\data\\test_isolation_3.bgs"));
        parser.parse();

        System.out.printf("width: %d\nheight: %d\nislands found: %d\nbridges found: %d\n",
                parser.getWidth(), parser.getHeight(), parser.getIslands().size(), parser.getBridges().size());
    }

    private static String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), Charset.defaultCharset());
    }
}
