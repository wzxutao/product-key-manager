package cn.rypacker.productkeymanager.services;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatetimeUtilTest {

    @Test
    void milliToString_thenConvertBack(){
        var now = (int) Instant.now().toEpochMilli() / 1000;
        var random = new Random();

        for(int i=0; i< 10000; i++){
            var s = random.nextInt() % now;
            var fd = DatetimeUtil.epochSecondsToFinalDate(s);
            assertEquals(DatetimeUtil.finalDateToEpochSeconds(fd), s);
        }

    }
}