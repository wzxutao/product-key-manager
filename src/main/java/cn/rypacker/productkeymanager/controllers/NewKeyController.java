package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.models.JsonRecord;
import cn.rypacker.productkeymanager.models.RequestBodies;
import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import cn.rypacker.productkeymanager.repositories.NormalAccountRepository;
import cn.rypacker.productkeymanager.services.JSONUtil;
import cn.rypacker.productkeymanager.services.KeyGenerator;
import cn.rypacker.productkeymanager.services.auth.NormalAccountAuth;
import cn.rypacker.productkeymanager.services.ciphers.JokeCipher;
import cn.rypacker.productkeymanager.services.datamanagers.MandatoryFieldsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    NormalAccountAuth normalAccountAuth;
    @Autowired
    NormalAccountRepository normalAccountRepository;


    private boolean isAuthorized(String authToken){
        return authToken != null && normalAccountAuth.isTokenValid(authToken);
    }

    private String returnTemplateIfAuthSucceed(String original, String authToken){
        return isAuthorized(authToken) ? original : "new-key-auth";
    }

    @GetMapping("")
    public String get(Model model,
                      @CookieValue(value = "normalAuth", required = false) String authToken){
        model.addAttribute("requiredFields", mandatoryFieldsManager.getFieldNames());
        return returnTemplateIfAuthSucceed("new-key", authToken);
    }

    @GetMapping("/auth")
    public String getLoginForm(){
        return "new-key-auth";
    }

    @PostMapping("/login")
    public ResponseEntity<?> requestLogin(
            @RequestBody RequestBodies.LoginForm loginForm){
        if(loginForm == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        var account = loginForm.account;
        var password = loginForm.password;
        if(!normalAccountRepository.matches(account, password)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            var token = normalAccountAuth.signNewToken(account);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // deliberately omit auth for user experience
    @PostMapping(path = "/submit", consumes = "application/json")
    public ResponseEntity<?> postNewKey(
            @RequestBody Map<String, String> reqBody,
            @CookieValue(value = "normalAuth") String authToken
            ){
        if(reqBody == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        String username = normalAccountAuth.getUsername(authToken);

        reqBody.put("__username", username);
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
