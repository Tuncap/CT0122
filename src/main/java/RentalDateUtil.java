import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class RentalDateUtil {

    public static String getTwoDigitFormat(LocalDate date){
        return date == null ? null : date.format(DateTimeFormatter.ofPattern("M/d/yy"));
    }

    public static LocalDate convertToLocalDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/[yyyy][yy]");
        return LocalDate.parse(date.trim(), formatter);
    }

     public static List<LocalDate> getAllDatesWithinRange(LocalDate startDate, LocalDate endDate){
        return startDate.datesUntil(endDate).collect(Collectors.toList());
     }

     public static List<LocalDate> filterOutWeekends(List<LocalDate> dates){
        return dates.stream().filter(date -> DayOfWeek.SATURDAY != date.getDayOfWeek() &&
                DayOfWeek.SUNDAY != date.getDayOfWeek()).collect(Collectors.toList());
     }

    public static List<LocalDate> filterOutWeekdays(List<LocalDate> dates){
        return dates.stream().filter(date -> DayOfWeek.SATURDAY == date.getDayOfWeek() ||
                DayOfWeek.SUNDAY == date.getDayOfWeek()).collect(Collectors.toList());
    }

    public static List<LocalDate> filterOutHolidays(List<LocalDate> dates){
        List<LocalDate> newDates = new ArrayList<>();
        int currentYear = Integer.MIN_VALUE;
        LocalDate laborDay = null;
        LocalDate independenceDay = null;
        for(LocalDate date : dates){

            if (currentYear != date.getYear()) {
                LocalDate firstSevenDaysOfSeptember = LocalDate.of(date.getYear(), Month.SEPTEMBER, 8);
                firstSevenDaysOfSeptember = date.isBefore(firstSevenDaysOfSeptember) ? firstSevenDaysOfSeptember
                        : firstSevenDaysOfSeptember.plusYears(1);
                laborDay = LocalDate.of(firstSevenDaysOfSeptember.getYear(), Month.SEPTEMBER, 1)
                        .datesUntil(firstSevenDaysOfSeptember).filter(d -> d.getDayOfWeek() == DayOfWeek.MONDAY).findFirst().get();

                LocalDate defaultIndependenceDay = LocalDate.of(date.getYear(), Month.JULY, 4);
                if (defaultIndependenceDay.getDayOfWeek() == DayOfWeek.SATURDAY) {
                    independenceDay = defaultIndependenceDay.minusDays(1);
                } else if (defaultIndependenceDay.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    independenceDay = defaultIndependenceDay.plusDays(1);
                } else {
                    independenceDay = defaultIndependenceDay;
                }
                currentYear = date.getYear();
            }

            if(!date.isEqual(independenceDay) && !date.isEqual(laborDay)){
                newDates.add(date);
            }
        }

        return newDates;
    }

    public static Set<LocalDate> getHolidaysWithinRange(int startYear, int endYear){
        Set<LocalDate> holidays = new HashSet<>();
        for(int year = startYear; year <= endYear; year++){
            holidays.add(LocalDate.of(year, Month.SEPTEMBER, 1)
                    .datesUntil(LocalDate.of(year, Month.SEPTEMBER, 8))
                    .filter(d -> d.getDayOfWeek() == DayOfWeek.MONDAY).findFirst()
                    .orElseThrow());

            LocalDate independenceDay = LocalDate.of(year, Month.JULY, 4);
            if (independenceDay.getDayOfWeek() == DayOfWeek.SATURDAY) {
                independenceDay = independenceDay.minusDays(1);
            } else if (independenceDay.getDayOfWeek() == DayOfWeek.SUNDAY) {
                independenceDay = independenceDay.plusDays(1);
            }
            holidays.add(independenceDay);
        }
        return holidays;
    }

    public static long getNumberOfWeekdaysBetweenRange(LocalDate startDate, LocalDate endDate) {
        return startDate.datesUntil(endDate)
                .filter(date -> DayOfWeek.SATURDAY != date.getDayOfWeek() &&
                        DayOfWeek.SUNDAY != date.getDayOfWeek())
                .count();
    }

    public static long getNumberOfWeekendsBetweenRange(LocalDate startDate, LocalDate endDate) {
        return startDate.datesUntil(endDate)
                .filter(date -> DayOfWeek.SATURDAY == date.getDayOfWeek() ||
                        DayOfWeek.SUNDAY == date.getDayOfWeek())
                .count();
    }
}
