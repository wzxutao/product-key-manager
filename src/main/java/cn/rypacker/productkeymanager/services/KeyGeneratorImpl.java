package cn.rypacker.productkeymanager.services;

import cn.rypacker.productkeymanager.services.datamanagers.PropertyManager;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;

@Service
public class KeyGeneratorImpl implements KeyGenerator {
    PropertyManager propertyManager;

    private int keyLength;
    private static final int KEY_MIN_LENGTH = 8;
    public static final int DATE_LENGTH = 6;

    public KeyGeneratorImpl(PropertyManager propertyManager) {
        this.propertyManager = propertyManager;
        retrieveKeyLength();
    }

    private void retrieveKeyLength(){
        try{
            keyLength = Integer.parseInt(
                    propertyManager.getOrDefault(
                            PropertyManager.Properties.KEY_LENGTH, "15"));

        }catch (NumberFormatException e){
            keyLength = 15;
        }
    }

    String generateRandomString(int length){
        char[] array = new char[length]; // length is bounded by 7
        Random r = new Random();

        for(int i=0; i<length; i++){
            array[i] = (char) (r.nextInt('z' - 'a' + 1) + 'A');
        }
        return new String(array);
    }

    @Override
    public String nextSibling(String prev) {
        var date = prev.substring(0, DATE_LENGTH);
        var suffix = prev.substring(DATE_LENGTH);

        var chars = suffix.toCharArray();
        for(int i=chars.length-1; i>=0; i--){
            var prevChar = chars[i];
            if(prevChar != 'Z'){
                chars[i] = (char)(((int)prevChar) + 1);
                break;
            }else {
                chars[i] = 'A';
            }
        }
        return date + new String(chars);
    }

    @Override
    public long getCombinationCount() {
        return (long) Math.pow('Z' - 'A' + 1, keyLength - DATE_LENGTH);
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
        propertyManager.put(PropertyManager.Properties.KEY_LENGTH,
                Integer.toString(length));
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
