package data;

public enum Status {
    TO_DO(1),
    DONE(2),
    FAILED(3);

    private final int value;

    Status(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value + " - " + name();
    }
}