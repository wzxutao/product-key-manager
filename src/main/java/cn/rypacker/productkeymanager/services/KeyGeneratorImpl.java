package cn.rypacker.productkeymanager.services;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;

@Service
public class KeyGeneratorImpl implements KeyGenerator {
    private int keyLength = 15;
    private static final int KEY_MIN_LENGTH = 8;
    private static final int DATE_LENGTH = 6;

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
        return String.format("%02d%02d%02d",
                zdt.getYear() % 100, zdt.getMonth().getValue(), zdt.getDayOfMonth());
    }

    @Override
    public int getKeyLength() {
        return keyLength;
    }

    @Override
    public void setKeyLength(int length) {
        if(length < KEY_MIN_LENGTH){
            throw new IllegalArgumentException("key length is too short");
        }
        this.keyLength = length;
    }

    @Override
    public String generateKey() {
        return generateKey(System.currentTimeMillis());
    }

    @Override
    public String generateKey(long epochMilli) {
        var dateTimeString = generateDatetimeString(epochMilli);
        var randStrLen = keyLength - dateTimeString.length();
        assert dateTimeString.length() == DATE_LENGTH : dateTimeString;

        var randomStr = generateRandomString(keyLength - dateTimeString.length());
        return dateTimeString + randomStr;
    }

    @Override
    public String generateKey(String dateString) {
        var randomStr = generateRandomString(keyLength - dateString.length());
        if(dateString.length() != DATE_LENGTH){
            throw new IllegalArgumentException("data string length: " + dateString.length() +
                    " expecting: " + DATE_LENGTH);
        }

        return dateString + randomStr;
    }
}
