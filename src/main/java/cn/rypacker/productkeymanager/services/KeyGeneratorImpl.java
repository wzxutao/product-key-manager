package cn.rypacker.productkeymanager.services;

import cn.rypacker.productkeymanager.dto.KeyGenStats;
import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import cn.rypacker.productkeymanager.services.configstore.UserConfigStore;
import cn.rypacker.productkeymanager.specification.JsonRecordSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KeyGeneratorImpl implements KeyGenerator {

    @Autowired
    private UserConfigStore userConfigStore;

    private int keyLength;

    @Autowired
    private JsonRecordRepository recordRepository;

    private NavigableSet<String> blackList;

    private final List<String> candidates = new ArrayList<>();

    private static final Random random = new Random();

    @PostConstruct
    private void init() {
        keyLength = userConfigStore.getData().getKey().getLength();
        blackList = new TreeSet<>(Comparator.comparing(String::length).thenComparing(String::compareTo));
        blackList.addAll(userConfigStore.getData().getKey().getBlacklist());
        refreshCandidates();
    }


    @Override
    public void refreshCandidates() {
        var fromS = DatetimeUtil.getTodayEpochSeconds(true) * 1000;

        var used = recordRepository.findAll(
                JsonRecordSpecs.createdMilliAfter(fromS)
        );

        var candidateLength = keyLength - DATE_LENGTH;

        var usedKeys = used.stream()
                .map(r -> r.getProductKey().substring(DATE_LENGTH))
                .filter(s -> s.length() == candidateLength)
                .collect(Collectors.toSet());

        var candidateSet = new TreeSet<String>();
        int radix = 'Z' - 'A' + 1;
        var totalCountForLength = (long) Math.pow('Z' - 'A' + 1, candidateLength);
        for(int i = 0; i < totalCountForLength; i++) {
            char[] array = new char[candidateLength];
            for(int j = 0; j < candidateLength; j++){
                array[candidateLength - j - 1] = (char) ('A' + (i / (long) Math.pow(radix, j)) % radix);
            }
            var str = new String(array);
            if(usedKeys.contains(str)) continue;

            candidateSet.add(str);
        }

        candidateSet.removeAll(blackList);
        candidates.clear();
        candidates.addAll(candidateSet);
    }




    @Override
    public KeyGenStats getStats(){
        var candidateLength = keyLength - DATE_LENGTH;
        long totalCount = (long) Math.pow('Z' - 'A' + 1, candidateLength);
        long blackListedCount = blackList.stream().filter(s -> s.length() == candidateLength).count();
        long remainingCount = candidates.size();

        return KeyGenStats
                .builder()
                .keyLength(keyLength)
                .totalKeyCount(totalCount)
                .blackListedKeyCount(blackListedCount)
                .usedKeyCount(totalCount - blackListedCount - remainingCount)
                .remainingKeyCount(remainingCount)
                .build();
    }

    @Override
    public List<String> getBlackList() {
        return new ArrayList<>(blackList);
    }

    @Override
    public void setBlackList(List<String> blackList) {
        var blackListAllUpper = blackList.stream().map(String::toUpperCase).collect(Collectors.toList());
        userConfigStore.update(c -> c.getKey().setBlacklist(blackListAllUpper));
        this.blackList.clear();
        this.blackList.addAll(blackListAllUpper);
        refreshCandidates();
    }

    @Override
    public void setKeyLength(int length) {
        if(length < MIN_LENGTH){
            throw new IllegalArgumentException("key length is too short");
        }
        if(length > MAX_LENGTH){
            throw new IllegalArgumentException("key length is too long");
        }
        userConfigStore.update(c -> c.getKey().setLength(length));
        this.keyLength = length;
        refreshCandidates();
    }

    @Override
    public String generateKey(String dateString) {
        if(dateString.length() != DATE_LENGTH){
            throw new IllegalArgumentException("data string length: " + dateString.length() +
                    " expecting: " + DATE_LENGTH);
        }

        synchronized (this) {
            if(candidates.isEmpty()){
                expand();
            }
            var idx = random.nextInt(candidates.size());
            var candidate = candidates.get(idx);
            candidates.remove(idx);
            return dateString + candidate;
        }
    }

    @Override
    public int getKeyLength() {
        return keyLength;
    }


    @Override
    public void returnCandidate(String key) {

        synchronized (this) {
            int l = 0;
            int r = candidates.size() - 1;

            while(l < r) {
                int mid = (l + r) / 2;
                if(candidates.get(mid).compareTo(key.substring(DATE_LENGTH)) < 0) {
                    l = mid + 1;
                } else {
                    r = mid;
                }
            }

            candidates.add(l, key.substring(DATE_LENGTH));
        }
    }
}
