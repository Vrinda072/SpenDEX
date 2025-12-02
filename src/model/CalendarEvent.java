package model;

import java.time.LocalDate;
import java.util.Objects;

public class CalendarEvent {
    private final String title;
    private final String description;
    private final LocalDate date;
    private final double cost;
    private final boolean isMandatory;

    public CalendarEvent(String title, String description, LocalDate date, double cost, boolean isMandatory) {
        this.title = title != null ? title : "";
        this.description = description != null ? description : "";
        this.date = date != null ? date : LocalDate.now();
        this.cost = Math.max(0, cost);
        this.isMandatory = isMandatory;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getCost() {
        return cost;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalendarEvent that = (CalendarEvent) o;
        return Double.compare(that.cost, cost) == 0 &&
               isMandatory == that.isMandatory &&
               title.equals(that.title) &&
               description.equals(that.description) &&
               date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, date, cost, isMandatory);
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%.2f)", title, date, cost);
    }
}
