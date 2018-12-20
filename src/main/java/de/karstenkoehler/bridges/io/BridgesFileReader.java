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

/**
 * A bridges file reader can read puzzles from the hard drive. Therefore it uses instances of {@link TokenizerImpl}
 * and {@link TokenConsumingParser}.
 */
public class BridgesFileReader {

    /**
     * Generates a {@link BridgesPuzzle} from the content of a {@link File}. The content of the file
     * is parsed and validated. The returned puzzle is fully initialized and ready to use.
     *
     * @param file the file to read from
     * @return the puzzle specified in the file
     * @throws ParseException    if the file content had syntactic errors
     * @throws ValidateException if the file content had semantic errors
     * @throws IOException       if there where issues reading the file
     */
    public BridgesPuzzle readFile(File file) throws ParseException, ValidateException, IOException {
        BridgesPuzzle puzzle = readAndParse(file);
        puzzle.fillMissingConnections();
        return puzzle;
    }

    /**
     * Reads, parses and validates the puzzle specified in the file.
     *
     * @param file the file to read from
     * @return the puzzle specified in the file
     * @throws ParseException    if the file content had syntactic errors
     * @throws ValidateException if the file content had semantic errors
     * @throws IOException       if there where issues reading the file
     */
    private BridgesPuzzle readAndParse(File file) throws IOException, ParseException, ValidateException {
        Validator validator = new DefaultValidator();

        Parser parser = new TokenConsumingParser(new TokenizerImpl(readFileToString(file.getAbsolutePath())));
        BridgesPuzzle puzzle = parser.parse();
        validator.validate(puzzle);

        return puzzle;
    }

    /**
     * Reads the content of the given file into a string.
     *
     * @param path the file to read from
     * @return the content of the file
     * @throws IOException if there where issues reading the file
     */
    private static String readFileToString(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), Charset.defaultCharset());
    }
}
