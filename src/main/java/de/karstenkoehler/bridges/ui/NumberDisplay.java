package de.karstenkoehler.bridges.ui;

public enum NumberDisplay {
    SHOW_REQUIRED(0),
    SHOW_REMAINING(1);

    private final int value;

    NumberDisplay(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
