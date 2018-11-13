package de.karstenkoehler.bridges.model.generator;

public class Counter {
    private static final int TRIES_BEFORE_RESET = 10;
    private static final int TRIES_BEFORE_SPLIT = 25;
    private int splitBridgeCounter;
    private int resetCounter;

    public void resetCounters() {
        this.resetCounter = 0;
        this.splitBridgeCounter = 0;
    }

    public boolean shouldSplit() {
        if (this.splitBridgeCounter > TRIES_BEFORE_SPLIT) {
            this.resetCounter++;
            this.splitBridgeCounter = 0;
            return true;
        }
        return false;
    }

    public boolean shouldReset() {
        return this.resetCounter > TRIES_BEFORE_RESET;
    }

    public void incrementSplitCounter() {
        this.splitBridgeCounter++;
    }
}
