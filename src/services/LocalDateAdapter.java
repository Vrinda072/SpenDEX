package services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Simple LocalDate utility for formatting (no JSON dependencies)
 */
public class LocalDateAdapter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public static String formatDate(LocalDate date) {
        return date != null ? date.format(formatter) : "";
    }

    public static LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, formatter);
    }
}
