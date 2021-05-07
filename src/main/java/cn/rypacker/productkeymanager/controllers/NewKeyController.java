package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.models.JsonRecord;
import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import cn.rypacker.productkeymanager.services.JSONUtil;
import cn.rypacker.productkeymanager.services.KeyGenerator;
import cn.rypacker.productkeymanager.services.ciphers.JokeCipher;
import cn.rypacker.productkeymanager.services.datamanagers.MandatoryFieldsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/new-key")
@Controller
public class NewKeyController {

    private final Logger logger = LoggerFactory.getLogger(NewKeyController.class);
    @Autowired
    JokeCipher jokeCipher;
    @Autowired
    JsonRecordRepository jsonRecordRepository;
    @Autowired
    KeyGenerator keyGenerator;
    @Autowired
    MandatoryFieldsManager mandatoryFieldsManager;

    @GetMapping("")
    public String get(Model model){
        model.addAttribute("requiredFields", mandatoryFieldsManager.getFieldNames());
        return "new-key";
    }

    @PostMapping(path = "/submit", consumes = "application/json")
    public ResponseEntity<?> postNewKey(@RequestBody Map<String, String> reqBody){
        if(reqBody == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        var contents = JSONUtil.toStringFrom(reqBody);
        var date = reqBody.get("日期");
        if(date == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        String key;
        // avoid duplication
        var combinations = keyGenerator.getCombinationCount();
        var count = 0;

        key = keyGenerator.generateKey(date);
        while (jsonRecordRepository.findByProductKey(key).size() > 0){
            key = keyGenerator.nextSibling(key);
            count++;
            // expand on half full
            if(count >= combinations / 2){
                keyGenerator.expand();
                combinations = keyGenerator.getCombinationCount();
                count = 0;
                key = keyGenerator.generateKey(date);
            }
        }

        var record = new JsonRecord(contents, key);
        jsonRecordRepository.save(record);
        logger.info("new record saved: " + record.toString());
        return new ResponseEntity<>(key, HttpStatus.CREATED);
    }
}
