package de.karstenkoehler.bridges.ui.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * This class is just a container for storing event type instances.
 */
public abstract class EventTypes {
    /**
     * This event type is fired when the active puzzle changes.
     */
    public static final EventType<PuzzleChangeEvent> CHANGE_PUZZLE = new EventType<>("CHANGE_PUZZLE");

    /**
     * This event type is fired to indicate that the active puzzle has changes that were not saved to a .bgs file.
     */
    public static final EventType<Event> FILE_MODIFIED = new EventType<>("FILE_MODIFIED");

    /**
     * This event type is fired if the canvas displaying the puzzle needs to be refreshed.
     */
    public static final EventType<Event> REDRAW = new EventType<>("REDRAW");

    /**
     * This event type is fired if the state of the puzzle has to be reevaluated.
     */
    public static final EventType<Event> EVAL_STATE = new EventType<>("EVAL_STATE");
}
