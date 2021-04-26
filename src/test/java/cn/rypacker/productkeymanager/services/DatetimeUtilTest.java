package cn.rypacker.productkeymanager.services;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Random;

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
}