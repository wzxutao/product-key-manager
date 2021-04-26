package cn.rypacker.productkeymanager.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

}
