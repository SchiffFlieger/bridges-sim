package de.karstenkoehler.bridges.io;

import de.karstenkoehler.bridges.io.parser.ParseException;
import de.karstenkoehler.bridges.io.parser.Parser;
import de.karstenkoehler.bridges.io.parser.TokenConsumingParser;
import de.karstenkoehler.bridges.io.parser.token.TokenizerImpl;
import de.karstenkoehler.bridges.io.validator.DefaultValidator;
import de.karstenkoehler.bridges.io.validator.ValidateException;
import de.karstenkoehler.bridges.io.validator.Validator;
import de.karstenkoehler.bridges.model.BridgesPuzzle;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BridgesFileReader {
    public BridgesPuzzle readFile(File file) throws ParseException, ValidateException, IOException {
        BridgesPuzzle puzzle = parsePuzzle(file);
        puzzle.fillMissingBridges();
        return puzzle;
    }

    private BridgesPuzzle parsePuzzle(File file) throws IOException, ParseException, ValidateException {
        Validator validator = new DefaultValidator();

        Parser parser = new TokenConsumingParser(new TokenizerImpl(readFileToString(file.getAbsolutePath())));
        BridgesPuzzle puzzle = parser.parse();
        validator.validate(puzzle);


        return puzzle;
    }

    private static String readFileToString(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), Charset.defaultCharset());
    }
}
