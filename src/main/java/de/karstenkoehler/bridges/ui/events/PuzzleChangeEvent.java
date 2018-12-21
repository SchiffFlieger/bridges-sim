package de.karstenkoehler.bridges.ui.events;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import javafx.event.Event;

import static de.karstenkoehler.bridges.ui.events.EventTypes.CHANGE_PUZZLE;

/**
 * This event is used when a new bridges puzzle is generated in a background task.
 */
public class PuzzleChangeEvent extends Event {
    private final BridgesPuzzle puzzle;

    /**
     * Creates a new event of type {@link EventTypes#CHANGE_PUZZLE}.
     *
     * @param puzzle the puzzle to change to
     */
    public PuzzleChangeEvent(BridgesPuzzle puzzle) {
        super(CHANGE_PUZZLE);
        this.puzzle = puzzle;
    }

    /**
     * Returns the puzzle to change to.
     *
     * @return the puzzle to change to
     */
    public BridgesPuzzle getPuzzle() {
        return puzzle;
    }
}
