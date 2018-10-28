package de.karstenkoehler.bridges.model;

public enum PuzzleState {
    SOLVED("Puzzle is solved."),
    NOT_SOLVED("Puzzle is not solved."),
    NO_LONGER_SOLVABLE("Puzzle is no longer solvable."),
    ERROR("A rule has been violated.");

    private final String state;

    PuzzleState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return this.state;
    }
}
