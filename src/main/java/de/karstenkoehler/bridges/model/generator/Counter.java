package de.karstenkoehler.bridges.model.generator;

/**
 * This is a utility class for {@link GeneratorImpl}. It encapsulates some of the retry logic.
 */
class Counter {
    private static final int TRIES_BEFORE_RESET = 10;
    private static final int TRIES_BEFORE_SPLIT = 25;
    private int splitBridgeCounter;
    private int resetCounter;

    /**
     * Resets the counter to its initial state.
     */
    void resetCounters() {
        this.resetCounter = 0;
        this.splitBridgeCounter = 0;
    }

    /**
     * Indicates if the generator should split up a bridge.
     *
     * @return true if the generator should split up a bridge, false otherwise
     */
    boolean shouldSplit() {
        if (this.splitBridgeCounter > TRIES_BEFORE_SPLIT) {
            this.resetCounter++;
            this.splitBridgeCounter = 0;
            return true;
        }
        return false;
    }

    /**
     * Indicates if the generator should discard its progress and start from scratch.
     *
     * @return true if the generator should start from scratch, false otherwise
     */
    boolean shouldReset() {
        return this.resetCounter > TRIES_BEFORE_RESET;
    }

    /**
     * Is called every time the generator fails to create a new random island.
     */
    void incrementSplitCounter() {
        this.splitBridgeCounter++;
    }
}
