package cn.rypacker.productkeymanager.services;

import cn.rypacker.productkeymanager.dto.KeyGenStats;

import java.util.List;

public interface KeyGenerator {


    int MIN_LENGTH = 8;
    int MAX_LENGTH = 11;
    int DATE_LENGTH = 6;


    /**
     * @throws IllegalArgumentException if key length is too short
     */
    void setKeyLength(int length);

    /**
     * keeps the dateString intact
     * @param dateString
     * @return
     * @throws IllegalArgumentException if the dateString has a wrong length
     */
    String generateKey(String dateString);

    int getKeyLength();

    default void expand(){
        setKeyLength(getKeyLength() + 1);
    }

    KeyGenStats getStats();

    List<String> getBlackList();
    void setBlackList(List<String> blackList);

    void returnCandidate(String key);

    void refreshCandidates();
}
