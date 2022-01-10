import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class RentalDateUtilTest {

    @Test
    @DisplayName("Different string of dates should be parsed to LocalDate")
    void testConvertToLocalDate() {
        assertEquals(LocalDate.of(2020, 1, 6), RentalDateUtil.convertToLocalDate("1/6/20"));
        assertEquals(LocalDate.of(2020, 7, 2), RentalDateUtil.convertToLocalDate("7/2/20"));
        assertEquals(LocalDate.of(2020, 7, 2), RentalDateUtil.convertToLocalDate("7/2/20"));
        assertEquals(LocalDate.of(2020, 11, 12), RentalDateUtil.convertToLocalDate("11/12/20"));
        assertEquals(LocalDate.of(2006, 11, 12), RentalDateUtil.convertToLocalDate("11/12/06"));

    }

    @Test
    @DisplayName("Different string of dates should display in 1 to two digit format.")
    void testTwoDigitFormat() {
        assertEquals("1/6/20", RentalDateUtil.getTwoDigitFormat(LocalDate.of(2020, 1, 6)));
        assertEquals("11/16/20", RentalDateUtil.getTwoDigitFormat(LocalDate.of(2020, 11, 16)));
        assertEquals("1/16/55", RentalDateUtil.getTwoDigitFormat(LocalDate.of(2055, 1, 16)));
    }

    @Test
    @DisplayName("Total number of weekdays between two dates should be returned.")
    void testNumberOfWeekdaysBetweenRange() {
        LocalDate todayThursday = LocalDate.of(2022, 1, 6);
        assertEquals(2, RentalDateUtil.getNumberOfWeekdaysBetweenRange(todayThursday, todayThursday.plusDays(4)));
        assertEquals(260, RentalDateUtil.getNumberOfWeekdaysBetweenRange(LocalDate.of(2022,1, 8),
                LocalDate.of(2022,1, 8).plusDays(365)));
        assertEquals(5, RentalDateUtil.getNumberOfWeekdaysBetweenRange(LocalDate.of(2022,1, 8),
                LocalDate.of(2022,1, 8).plusWeeks(1)));
    }

    @Test
    @DisplayName("Total number of weekends between two dates should be returned.")
    void testNumberOfWeekendsBetweenRange() {
        LocalDate todayThursday = LocalDate.of(2022, 1, 6);
        assertEquals(2, RentalDateUtil.getNumberOfWeekendsBetweenRange(todayThursday, todayThursday.plusDays(4)));
        assertEquals(105, RentalDateUtil.getNumberOfWeekendsBetweenRange(LocalDate.of(2022, 1,1),
                LocalDate.of(2022, 1,1).plusDays(365)));
        assertEquals(2, RentalDateUtil.getNumberOfWeekendsBetweenRange(LocalDate.now(), LocalDate.now().plusWeeks(1)));
    }

    @Test
    @DisplayName("Should filter out independence day falling on weekend and weekday.")
    void testFilterOutIndependenceDay() {
        List<LocalDate> independenceOnSunday = LocalDate.of(2021, 7, 3)
                .datesUntil(LocalDate.of(2021, 7, 6))
                .collect(Collectors.toList());
        assertFalse(RentalDateUtil.filterOutHolidays(independenceOnSunday)
                .contains(LocalDate.of(2021, 7, 5)));

        List<LocalDate> independenceOnSaturday = LocalDate.of(2020, 7, 3)
                .datesUntil(LocalDate.of(2020, 7, 6))
                .collect(Collectors.toList());
        assertFalse(RentalDateUtil.filterOutHolidays(independenceOnSaturday)
                .contains(LocalDate.of(2020, 7, 3)));

        List<LocalDate> independenceOnWeekday = LocalDate.of(2017, 7, 3)
                .datesUntil(LocalDate.of(2017, 7, 6))
                .collect(Collectors.toList());
        assertFalse(RentalDateUtil.filterOutHolidays(independenceOnWeekday)
                .contains(LocalDate.of(2017, 7, 4)));
    }

    @Test
    @DisplayName("Should filter out labor day.")
    void testFilterOutLaborDay() {
        List<LocalDate> laborDayWeek = LocalDate.of(2021, 9, 1)
                .datesUntil(LocalDate.of(2021, 9, 8))
                .collect(Collectors.toList());
        assertFalse(RentalDateUtil.filterOutHolidays(laborDayWeek)
                .contains( LocalDate.of(2021, 9, 6)));

    }

    @Test
    @DisplayName("Should filter out labor and independence day.")
    void testFilterOutHolidays() {
        List<LocalDate> daysOfYear = LocalDate.of(2021, 9, 1)
                .datesUntil(LocalDate.of(2022, 9, 1))
                .collect(Collectors.toList());
        daysOfYear = RentalDateUtil.filterOutHolidays(daysOfYear);
        LocalDate laborDay = LocalDate.of(2021, 9, 6);
        LocalDate independenceDay = LocalDate.of(2021, 7, 4);
        assertTrue(!daysOfYear.contains(laborDay)
                && !daysOfYear.contains(independenceDay)
                && daysOfYear.size() == 363);
    }

    @Test
    @DisplayName("Should retrieve all holidays for year range.")
    void testHolidaysWithinRange() {
        Set<LocalDate> holidays = RentalDateUtil.getHolidaysWithinRange(2021, 2022);
        assertTrue(holidays.stream().noneMatch(holiday -> !Set.of(
                LocalDate.of(2022, 7, 4),
                LocalDate.of(2021, 7, 5),
                LocalDate.of(2022, 9, 5),
                LocalDate.of(2021, 9, 6))
                .contains(holiday)));

        holidays = RentalDateUtil.getHolidaysWithinRange(2021, 2021);
        assertTrue(holidays.stream().noneMatch(holiday -> !Set.of(
                LocalDate.of(2021, 7, 5),
                LocalDate.of(2021, 9, 6))
                .contains(holiday)));
    }
}
