package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class NewKeyControllerTest {
    private static final Logger log = LoggerFactory.getLogger(NewKeyControllerTest.class);

    @Autowired
    JsonRecordRepository jsonRecordRepository;
    @Autowired
    NewKeyController newKeyController;

    @Test
    public void insert10ThousandRecordsWithoutDuplication(){
        Map<String, String> dummyReqbody = new HashMap<>();
        var random = new Random();
        final long recordCount = 10_000;
        for(long i=0; i<recordCount; i++){
            dummyReqbody.put("日期", Integer.toString(random.nextInt(900000) + 100000));
            var rv = newKeyController.postNewKey(dummyReqbody);
            assertEquals(rv.getStatusCode(), HttpStatus.CREATED);
            log.info("Inserting record " + i);
        }
        var allRecords = jsonRecordRepository.findAll();
        int count = 0;
        for (var record :
                allRecords) {
            log.info("checking record " + count);
            assertEquals(jsonRecordRepository.findByProductKey(record.getProductKey()).size(), 1);
            count++;
        }

        assertTrue(count > recordCount);
    }
}