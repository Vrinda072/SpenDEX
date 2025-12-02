package model;

public enum Category {
    FOOD("Food"),
    TRAVEL("Travel"),
    BILLS("Bills"),
    ENTERTAINMENT("Entertainment"),
    OTHER("Other");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
