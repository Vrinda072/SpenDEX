package manager;

import model.Event;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * SpendEX — CalendarManager
 * Manages upcoming events and their impact on budget
 */
public class CalendarManager {
    private List<Event> events;

    // Constructor
    public CalendarManager() {
        this.events = new ArrayList<>();
    }

    // Add event
    public void addEvent(Event event) {
        if (event != null) {
            events.add(event);
        }
    }

    // Remove event
    public void removeEvent(Event event) {
        events.remove(event);
    }

    // Get all events
    public List<Event> getAllEvents() {
        return new ArrayList<>(events);
    }

    // Get upcoming events (next 7 days)
    public List<Event> getUpcomingEvents(LocalDate fromDate) {
        List<Event> upcoming = new ArrayList<>();
        LocalDate endDate = fromDate.plusDays(7);
        
        for (Event event : events) {
            if (!event.getDate().isBefore(fromDate) && 
                !event.getDate().isAfter(endDate)) {
                upcoming.add(event);
            }
        }
        return upcoming;
    }

    // Calculate total upcoming event costs
    public double getTotalUpcomingCosts(LocalDate fromDate) {
        double total = 0;
        for (Event event : getUpcomingEvents(fromDate)) {
            total += event.getExpectedCost();
        }
        return total;
    }

    // Get events by category
    public List<Event> getEventsByCategory(String category) {
        List<Event> filtered = new ArrayList<>();
        for (Event event : events) {
            if (event.getCategory().equals(category)) {
                filtered.add(event);
            }
        }
        return filtered;
    }
}
