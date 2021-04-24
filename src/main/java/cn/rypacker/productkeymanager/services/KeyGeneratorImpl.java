package cn.rypacker.productkeymanager.services;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;

@Service
public class KeyGeneratorImpl implements KeyGenerator {
    private static final int KEY_LENGTH = 15;
    private static final int ID_MIN_DIGITS = 5;

    String generateRandomString(int length){
        char[] array = new char[length]; // length is bounded by 7
        Random r = new Random();

        for(int i=0; i<length; i++){
            array[i] = (char) (r.nextInt('z' - 'a' + 1) + 'A');
        }
        return new String(array);
    }

    String generateDatetimeString(long epochMilli){
        ZonedDateTime zdt = ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
        return String.format("%02d/%02d%02d",
                zdt.getYear(), zdt.getMonth().getValue(), zdt.getDayOfMonth());
    }

    @Override
    public String generateKey() {
        return generateKey(System.currentTimeMillis());
    }

    @Override
    public String generateKey(long epochMilli) {
        var dateTimeString = generateDatetimeString(epochMilli);
        var randStrLen = KEY_LENGTH - dateTimeString.length();
        if(randStrLen < ID_MIN_DIGITS){
            throw new RuntimeException(String.format(
                    "key length %d not enough: " +
                            "datetime occupied %d leave %d for id which was supposed to be more than %d,",
                    KEY_LENGTH, dateTimeString.length(), randStrLen, ID_MIN_DIGITS)
            );
        }
        var randomStr = generateRandomString(KEY_LENGTH - dateTimeString.length());
        return dateTimeString + randomStr;
    }

    @Override
    public String generateKey(String dateString) {
        var randStrLen = KEY_LENGTH - dateString.length();
        if(randStrLen < ID_MIN_DIGITS){
            throw new RuntimeException(String.format(
                    "key length %d not enough: " +
                            "datetime occupied %d leave %d for id which was supposed to be more than %d,",
                    KEY_LENGTH, dateString.length(), randStrLen, ID_MIN_DIGITS)
            );
        }
        var randomStr = generateRandomString(KEY_LENGTH - dateString.length());
        return dateString + randomStr;
    }
}
