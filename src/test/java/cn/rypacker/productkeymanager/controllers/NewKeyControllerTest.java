package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import cn.rypacker.productkeymanager.services.KeyGenerator;
import cn.rypacker.productkeymanager.services.auth.NormalAccountAuth;
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
    @Autowired
    KeyGenerator keyGenerator;

    @Autowired
    NormalAccountAuth normalAccountAuth;

    private void insertRecordsWithoutDuplication(long recordCount) throws Exception {
        Map<String, String> dummyReqbody = new HashMap<>();
        var random = new Random();

        var authToken = normalAccountAuth.signNewToken("__unit_test");
        for(long i=0; i<recordCount; i++){
            dummyReqbody.put("日期", "700101");
            var rv = newKeyController.postNewKey(dummyReqbody, authToken);
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

        assertTrue(count >= recordCount);
    }

    @Test
    void expand() throws Exception {
        keyGenerator.setKeyLength(8);
        var combinations = keyGenerator.getCombinationCount();

        // cram it full
        assertEquals(keyGenerator.getKeyLength(), 8);
        insertRecordsWithoutDuplication(2 * combinations);
        assertTrue(keyGenerator.getKeyLength() > 8);


    }
}