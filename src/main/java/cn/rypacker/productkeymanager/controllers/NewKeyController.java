package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.models.JsonRecord;
import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import cn.rypacker.productkeymanager.services.JSONUtil;
import cn.rypacker.productkeymanager.services.ciphers.JokeCipher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.Objects;

@RequestMapping("/new-key")
@Controller
public class NewKeyController {

    private final Logger logger = LoggerFactory.getLogger(NewKeyController.class);
    @Autowired
    JokeCipher jokeCipher;
    @Autowired
    JsonRecordRepository jsonRecordRepository;

    @GetMapping("")
    public String get(){
        return "new-key";
    }

    @PostMapping(path = "/submit", consumes = "application/json")
    public ResponseEntity<?> postNewKey(@RequestBody Map<String, String> reqBody){
        if(reqBody == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        var contents = JSONUtil.toStringFrom(reqBody);
        var record = new JsonRecord(contents);
        jsonRecordRepository.save(record);

        String key;
        try{
            key = jokeCipher.insecureEncrypt(
                    Objects.requireNonNull(record.getId()).toString());
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        record.setKeyType(JsonRecord.ProductKeyContentType.ID);
        record.setProductKey(key);
        jsonRecordRepository.save(record);
        logger.info("new record saved: " + record.toString());
        return new ResponseEntity<>(key, HttpStatus.CREATED);
    }
}
