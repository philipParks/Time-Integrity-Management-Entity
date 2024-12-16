package com.TIME.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

// Takes a LocalDate and a LocalTime and creates a LocalDateTime (add statements for conversion in brackets of Lambda)
public interface DateTimeInterface {
    LocalDateTime convertToDateTime(LocalDate localDate, LocalTime localTime);
}
