package de.karstenkoehler.bridges.io.validators;

import de.karstenkoehler.bridges.io.ParseResult;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This is the default validator to use for validation. It includes all existing validators
 * in this package and applies them to the parsed structure.  If there are any errors in
 * the structure, this validator points them out.
 */
public class DefaultValidator implements Validator {

    private final Set<Validator> validators;

    public DefaultValidator() {
        // ToDo maybe replace this with reflection?
        this.validators = new HashSet<>(Arrays.asList(
                new BridgeReferenceToIslandValidator(), new BridgesConnectDifferentIslandsValidator(),
                new BridgesNotDiagonalValidator(), new FieldSizeValidator(), new IslandOnUniqueFieldsValidator(),
                new IslandsOnFieldValidator(), new NoIslandsOnAdjacentFieldsValidator(), new RequiredBridgesCountValidator(),
                new MaxOneBridgePerIslandPairValidator(), new IslandOrderValidator(), new BridgeReferenceOrderValidator(),
                new BridgeOrderValidator()
        ));
    }

    @Override
    public void validate(ParseResult result) throws ValidateException {
        for (Validator validator : this.validators) {
            validator.validate(result);
        }
    }

}
