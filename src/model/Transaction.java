package model;

import java.time.LocalDate;

public interface Transaction {
    double getAmount();
    String getTitle();
    LocalDate getDate();
    Category getCategory();
    String getType();
}
