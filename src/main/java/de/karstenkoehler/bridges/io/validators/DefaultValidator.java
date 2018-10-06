package de.karstenkoehler.bridges.io.validators;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.ValidateException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DefaultValidator implements Validator {

    private final Set<Validator> validators;

    public DefaultValidator() {
        // ToDo maybe replace this with reflection?
        this.validators = new HashSet<>(Arrays.asList(
                new BridgeReferenceToIslandValidator(), new BridgesConnectDifferentIslandsValidator(),
                new BridgesNotDiagonalValidator(), new FieldSizeValidator(), new IslandOnUniqueFieldsValidator(),
                new IslandsOnFieldValidator(), new NoIslandsOnAdjacentFieldsValidator(), new RequiredBridgesCountValidator(),
                new MaxOneBridgePerIslandPairValidator()
        ));
    }

    @Override
    public void validate(ParseResult result) throws ValidateException {
        // TODO PB-66
        // TODO PB-67

        for (Validator validator : this.validators) {
            validator.validate(result);
        }
    }

}
