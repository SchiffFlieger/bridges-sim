package de.karstenkoehler.bridges.io.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.ValidateException;

public interface Validator {
    void validate(final ParseResult result) throws ValidateException;
}
