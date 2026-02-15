package com.finmanager.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;

public class DateUtils {

    public static LocalDate getStartOfMonth(YearMonth yearMonth) {
        return yearMonth.atDay(1);
    }

    public static LocalDate getEndOfMonth(YearMonth yearMonth) {
        return yearMonth.atEndOfMonth();
    }

    public static YearMonth getCurrentMonth() {
        return YearMonth.now();
    }

    public static YearMonth getPreviousMonth(YearMonth yearMonth) {
        return yearMonth.minusMonths(1);
    }

    public static YearMonth getNextMonth(YearMonth yearMonth) {
        return yearMonth.plusMonths(1);
    }

    public static YearMonth getMonthsAgo(int months) {
        return YearMonth.now().minusMonths(months);
    }

    public static int getDaysBetween(LocalDate start, LocalDate end) {
        return (int) java.time.temporal.ChronoUnit.DAYS.between(start, end);
    }

    public static int getMonthsBetween(YearMonth start, YearMonth end) {
        return (int) java.time.temporal.ChronoUnit.MONTHS.between(start, end);
    }

    public static boolean isDateInMonth(LocalDate date, YearMonth yearMonth) {
        return date.getYear() == yearMonth.getYear() && date.getMonth() == yearMonth.getMonth();
    }

    public static LocalDate getFirstDayOfYear(int year) {
        return LocalDate.of(year, 1, 1);
    }

    public static LocalDate getLastDayOfYear(int year) {
        return LocalDate.of(year, 12, 31);
    }

    public static boolean isLeapYear(int year) {
        return java.time.Year.isLeap(year);
    }
}
