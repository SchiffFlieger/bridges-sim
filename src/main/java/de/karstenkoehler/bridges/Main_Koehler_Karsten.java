package de.karstenkoehler.bridges;

import de.karstenkoehler.bridges.io.DefaultBridgesParser;
import de.karstenkoehler.bridges.io.ParseException;
import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.Parser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main_Koehler_Karsten {
    public static void main(String[] args) throws IOException, ParseException {
        Parser parser = new DefaultBridgesParser();
        ParseResult result = parser.parse(readFile("src\\main\\resources\\data\\test_isolation_3.bgs"));

        System.out.printf("width: %d\nheight: %d\nislands found: %d\nbridges found: %d\n",
                result.getWidth(), result.getHeight(), result.getIslands().size(), result.getBridges().size());
    }

    private static String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), Charset.defaultCharset());
    }
}
