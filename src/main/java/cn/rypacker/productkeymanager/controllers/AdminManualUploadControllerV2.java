package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.common.RecordStatus;
import cn.rypacker.productkeymanager.dto.adminmanualupload.UploadKeyRequest;
import cn.rypacker.productkeymanager.entity.JsonRecord;
import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import cn.rypacker.productkeymanager.services.JSONUtil;
import cn.rypacker.productkeymanager.services.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

import static cn.rypacker.productkeymanager.common.Constants.RECORD_KEY_USERNAME;
import static cn.rypacker.productkeymanager.common.Constants.USERNAME_RECORD_VALUE_ADMIN;

@RestController
@RequestMapping("/admin/manual-upload-record/v2")
public class AdminManualUploadControllerV2 {

    @Autowired
    private JsonRecordRepository jsonRecordRepository;

    @Autowired
    private KeyGenerator keyGenerator;

    @PostMapping("/upload")
    @Transactional
    public ResponseEntity<?> upload(@RequestBody UploadKeyRequest reqBody) {
        var entity = jsonRecordRepository.findByProductKey(reqBody.getProductKey());
        if(entity != null) {
            return ResponseEntity.status(409).build();
        }
        entity = new JsonRecord();

        String username = reqBody.getUsername();
        if(username == null || username.isEmpty()) {
            username = USERNAME_RECORD_VALUE_ADMIN;
        }

        Map<String, String> kvPairs = new HashMap<>(reqBody.getData());
        kvPairs.put(RECORD_KEY_USERNAME, username);

        entity.setProductKey(reqBody.getProductKey());
        entity.setStatus(RecordStatus.NORMAL);
        entity.setCreatedMilli(System.currentTimeMillis());
        entity.setJsonString(JSONUtil.toStringFrom(kvPairs));

        jsonRecordRepository.save(entity);
        keyGenerator.refreshCandidates();

        return ResponseEntity.ok().build();
    }
}
