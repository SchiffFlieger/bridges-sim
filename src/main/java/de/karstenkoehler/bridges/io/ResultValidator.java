package de.karstenkoehler.bridges.io;

public interface ResultValidator {
    void validate(final ParseResult result) throws ValidateException;
}
