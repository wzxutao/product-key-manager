package cn.rypacker.productkeymanager.services;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DatetimeUtil {

    private static final String FINAL_DATE_FORMAT = "%04d/%02d/%02d %02d:%02d:%02d";

    /**
     * converts epochSeconds to YYYY/MM/DD hh:mm:ss
     * @param epochSeconds seconds since epoch
     * @return YYYY/MM/DD hh:mm:ss
     */
    public static String epochSecondsToFinalDate(long epochSeconds){
        var zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochSeconds * 1000L),
                ZoneId.systemDefault());
        return String.format(FINAL_DATE_FORMAT,
                zdt.getYear(), zdt.getMonth().getValue(), zdt.getDayOfMonth(),
                zdt.getHour(), zdt.getMinute(), zdt.getSecond());
    }

    public static long finalDateToEpochSeconds(String finalDate){
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        var ldt = LocalDateTime.parse(finalDate, formatter);
        var zdt = ldt.atZone(ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli() / 1000L;
    }


    public static long getTodayEpochSeconds(boolean fromStart){
        var ldt = LocalDateTime.of(
                LocalDate.now(),
                fromStart ? LocalTime.of(0, 0, 0) :
                        LocalTime.of(23, 59, 59)
        );
        var zdt = ldt.atZone(ZoneId.systemDefault());
        return zdt.toEpochSecond();
    }

    public static String getTodayDateTime(boolean fromStart){
        return epochSecondsToFinalDate(getTodayEpochSeconds(fromStart));
    }

    /**
     * returns epoch milli of 23:59:59.999 at the given day
     */
    public static long roundToMidNight(long original){
        var ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(original),
                ZoneId.systemDefault());
        var newLdt = LocalDateTime.of(
                ldt.getYear(), ldt.getMonth(), ldt.getDayOfMonth(),
                23, 59, 59);
        return newLdt.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000 + 999;
    }
}
