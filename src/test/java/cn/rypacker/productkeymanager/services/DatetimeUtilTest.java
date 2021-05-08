package cn.rypacker.productkeymanager.services;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;

import static cn.rypacker.productkeymanager.services.DatetimeUtil.roundToMidNight;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DatetimeUtilTest {

    @Test
    void milliToString_thenConvertBack(){
        var now = Instant.now().toEpochMilli() / 1000L;
        var fd = DatetimeUtil.epochSecondsToFinalDate(now);
        assertEquals(DatetimeUtil.finalDateToEpochSeconds(fd), now);

        var random = new Random();

        for(int i=0; i< 10000; i++){
            var s = random.nextInt() % now;
            fd = DatetimeUtil.epochSecondsToFinalDate(s);
            assertEquals(DatetimeUtil.finalDateToEpochSeconds(fd), s);
        }

        var cts = System.currentTimeMillis() / 1000L;
        fd = DatetimeUtil.epochSecondsToFinalDate(cts);
        assertEquals(DatetimeUtil.finalDateToEpochSeconds(fd), cts);
    }

    @Test
    void test_roundToMidNight() {
        var now = System.currentTimeMillis();
        var ldtNow = LocalDateTime.now();
        var ldtMn = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(roundToMidNight(now)),
                ZoneId.systemDefault());
        assertEquals(ldtMn.getYear(), ldtNow.getYear());
        assertEquals(ldtMn.getMonth(), ldtNow.getMonth());
        assertEquals(ldtMn.getDayOfMonth(), ldtNow.getDayOfMonth());
        assertEquals(ldtMn.getHour(), 23);
        assertEquals(ldtMn.getMinute(), 59);
        assertEquals(ldtMn.getSecond(), 59);

    }
}