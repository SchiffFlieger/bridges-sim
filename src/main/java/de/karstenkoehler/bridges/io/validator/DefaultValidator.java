package de.karstenkoehler.bridges.io.validator;

import de.karstenkoehler.bridges.model.BridgesPuzzle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This is the default validator to use for validation. It includes all existing validator
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
    public void validate(BridgesPuzzle puzzle) throws ValidateException {
        for (Validator validator : this.validators) {
            validator.validate(puzzle);
        }
    }

}
