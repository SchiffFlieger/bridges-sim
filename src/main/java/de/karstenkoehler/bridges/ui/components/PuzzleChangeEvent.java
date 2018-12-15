package de.karstenkoehler.bridges.ui.components;

import de.karstenkoehler.bridges.model.BridgesPuzzle;
import javafx.event.Event;
import javafx.event.EventType;

public class PuzzleChangeEvent extends Event {
    private final BridgesPuzzle puzzle;

    public PuzzleChangeEvent(EventType<? extends Event> eventType, BridgesPuzzle puzzle) {
        super(eventType);
        this.puzzle = puzzle;
    }

    public BridgesPuzzle getPuzzle() {
        return puzzle;
    }
}
