package com.finmanager.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.YearMonth;

public class DateUtilsTest {

    @Test
    public void testGetStartOfMonth() {
        YearMonth ym = YearMonth.of(2026, 2);
        LocalDate start = DateUtils.getStartOfMonth(ym);
        assertEquals(LocalDate.of(2026, 2, 1), start);
    }

    @Test
    public void testGetEndOfMonth() {
        YearMonth ym = YearMonth.of(2026, 2);
        LocalDate end = DateUtils.getEndOfMonth(ym);
        assertEquals(LocalDate.of(2026, 2, 28), end);
    }

    @Test
    public void testGetCurrentMonth() {
        YearMonth current = DateUtils.getCurrentMonth();
        assertNotNull(current);
    }

    @Test
    public void testGetPreviousMonth() {
        YearMonth ym = YearMonth.of(2026, 2);
        YearMonth prev = DateUtils.getPreviousMonth(ym);
        assertEquals(YearMonth.of(2026, 1), prev);
    }

    @Test
    public void testGetNextMonth() {
        YearMonth ym = YearMonth.of(2026, 2);
        YearMonth next = DateUtils.getNextMonth(ym);
        assertEquals(YearMonth.of(2026, 3), next);
    }

    @Test
    public void testIsDateInMonth() {
        LocalDate date = LocalDate.of(2026, 2, 15);
        YearMonth ym = YearMonth.of(2026, 2);
        assertTrue(DateUtils.isDateInMonth(date, ym));
    }

    @Test
    public void testIsLeapYear() {
        assertTrue(DateUtils.isLeapYear(2024));
        assertFalse(DateUtils.isLeapYear(2025));
    }
}
