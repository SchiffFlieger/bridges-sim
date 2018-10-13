package de.karstenkoehler.bridges.io;

import de.karstenkoehler.bridges.io.parser.ParseException;
import de.karstenkoehler.bridges.io.parser.Parser;
import de.karstenkoehler.bridges.io.parser.TokenConsumingParser;
import de.karstenkoehler.bridges.io.parser.token.TokenizerImpl;
import de.karstenkoehler.bridges.io.validators.DefaultValidator;
import de.karstenkoehler.bridges.io.validators.ValidateException;
import de.karstenkoehler.bridges.io.validators.Validator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BridgesFileReader {
    public ParseResult readFile(File file) throws ParseException, ValidateException, IOException {
        ParseResult result = getParseResult(file);
        result.fillMissingBridges();
        return result;
    }

    private ParseResult getParseResult(File file) throws IOException, ParseException, ValidateException {
        Validator validator = new DefaultValidator();

        Parser parser = new TokenConsumingParser(new TokenizerImpl(readFileToString(file.getAbsolutePath())));
        ParseResult result = parser.parse();
        validator.validate(result);


        return result;
    }

    private static String readFileToString(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), Charset.defaultCharset());
    }
}
