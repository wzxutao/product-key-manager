package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.dto.keygen.GenKeyRequest;
import cn.rypacker.productkeymanager.entity.JsonRecord;
import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import cn.rypacker.productkeymanager.services.JSONUtil;
import cn.rypacker.productkeymanager.services.KeyGenerator;
import cn.rypacker.productkeymanager.services.auth.AdminAuth;
import cn.rypacker.productkeymanager.services.auth.NormalAccountAuth;
import cn.rypacker.productkeymanager.services.configstore.UserConfigStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.rypacker.productkeymanager.common.Constants.RECORD_KEY_USERNAME;

@RestController
@RequestMapping("/normal/keygen/v2")
public class KeyGenControllerV2 {

    @Autowired
    private UserConfigStore userConfigStore;

    @Autowired
    private NormalAccountAuth normalAccountAuth;

    @Autowired
    private AdminAuth adminAuth;

    @Autowired
    private KeyGenerator keyGenerator;

    @Autowired
    private JsonRecordRepository jsonRecordRepository;

    @GetMapping("/mandatory-fields")
    public List<String> getMandatoryFields() {
        return userConfigStore.getData().getRecord().getMandatoryFields();
    }

    @PostMapping("/new-key")
    public ResponseEntity<?> generateKey(@Valid @RequestBody GenKeyRequest reqBody) {
        int keyCount = reqBody.getCount() == null ? 1 : reqBody.getCount();

        var authToken = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        String username = normalAccountAuth.getUsername(authToken);
        if (username == null) {
            if (adminAuth.isValidToken(authToken)) {
                username = "admin";
            } else {
                return ResponseEntity.badRequest().body("usename not found");
            }
        }

        Map<String, String> kvPairs = new HashMap<>(reqBody.getData());
        kvPairs.put(RECORD_KEY_USERNAME, username);

        var date = kvPairs.get("日期");
        if (date == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<JsonRecord> records = new ArrayList<>(keyCount);

        for (int i = 0; i < keyCount; i++) {
            var record = generateKey(kvPairs, date);
            records.add(record);
        }

        jsonRecordRepository.saveAll(records);
        return ResponseEntity.ok(records.stream().map(JsonRecord::getProductKey).collect(Collectors.toList()));
    }

    private JsonRecord generateKey(Map<String, String> contents, String date) {
        String jsonString = JSONUtil.toStringFrom(contents);

        String key;
        // avoid duplication
        var combinations = keyGenerator.getCombinationCount();
        var count = 0;

        key = keyGenerator.generateKey(date);
        while (jsonRecordRepository.findByProductKey(key).size() > 0) {
            key = keyGenerator.nextSibling(key);
            count++;
            // expand on half full
            if (count >= combinations / 2) {
                keyGenerator.expand();
                combinations = keyGenerator.getCombinationCount();
                count = 0;
                key = keyGenerator.generateKey(date);
            }
        }

        return new JsonRecord(jsonString, key);
    }
}
