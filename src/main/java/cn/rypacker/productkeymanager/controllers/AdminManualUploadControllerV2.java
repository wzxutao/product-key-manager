package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.common.CommonUtil;
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
import org.sqlite.SQLiteException;

import javax.transaction.Transactional;
import java.sql.SQLException;
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
    public ResponseEntity<?> upload(@RequestBody UploadKeyRequest reqBody) {
        var entity = new JsonRecord();

        Map<String, String> kvPairs = new HashMap<>(reqBody.getData());
        kvPairs.put(RECORD_KEY_USERNAME, USERNAME_RECORD_VALUE_ADMIN);

        entity.setProductKey(reqBody.getProductKey());
        entity.setJsonString(JSONUtil.toStringFrom(kvPairs));

        try{
            jsonRecordRepository.save(entity);
        }catch (Exception e) {
            var rootCause = CommonUtil.findRootCause(e);
            if (rootCause instanceof SQLiteException) {
                if(((SQLiteException) rootCause).getErrorCode() == 19) {
                    return ResponseEntity.status(409).build();
                }
            }
            throw e;
        }
        keyGenerator.refreshCandidates();

        return ResponseEntity.ok().build();
    }
}
