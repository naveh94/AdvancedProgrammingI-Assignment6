package mechanics;

public class Counter {

    private int count;

    /**
     * Creates a new counter object that start at given value.
     * @param init the initializing value for the counter.
     */
    public Counter(int init) {
        this.count = init;
    }

    /**
     * Returns current count.
     * @return int
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Reset the counter to 0.
     */
    public void resetCount() {
        this.count = 0;
    }

    /**
     * Raise count by 1.
     */
    public void raiseCount() {
        this.count++;
    }
}
