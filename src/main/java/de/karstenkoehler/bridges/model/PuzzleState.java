package de.karstenkoehler.bridges.model;

/**
 * Represents the four states that a bridges puzzle can take on.
 */
public enum PuzzleState {
    SOLVED("Puzzle is solved"),
    NOT_SOLVED("Puzzle is not solved"),
    NO_LONGER_SOLVABLE("Puzzle is no longer solvable"),
    ERROR("A rule has been violated"),
    NOT_LOADED("No puzzle.");

    private final String state;

    PuzzleState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return this.state;
    }
}
