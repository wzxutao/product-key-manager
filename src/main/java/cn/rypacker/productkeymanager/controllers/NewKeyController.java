package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.models.JsonRecord;
import cn.rypacker.productkeymanager.models.RequestBodies;
import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import cn.rypacker.productkeymanager.repositories.NormalAccountRepository;
import cn.rypacker.productkeymanager.services.JSONUtil;
import cn.rypacker.productkeymanager.services.KeyGenerator;
import cn.rypacker.productkeymanager.services.auth.NormalAccountAuth;
import cn.rypacker.productkeymanager.services.ciphers.JokeCipher;
import cn.rypacker.productkeymanager.services.configstore.UserConfigStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/new-key")
@Controller
@Slf4j
public class NewKeyController {

    @Autowired
    private JokeCipher jokeCipher;
    @Autowired
    private JsonRecordRepository jsonRecordRepository;
    @Autowired
    private KeyGenerator keyGenerator;
    @Autowired
    private UserConfigStore userConfigStore;
    @Autowired
    private NormalAccountAuth normalAccountAuth;
    private NormalAccountRepository normalAccountRepository;

    @GetMapping("")
    public String get(Model model){
        model.addAttribute("requiredFields", userConfigStore.getData().getRecord().getMandatoryFields());
        return "new-key";
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
            @RequestBody Map<String, String> reqBody
            ){
        if(reqBody == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        var authToken = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
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
        log.info("new record saved: " + record.toString());
        return new ResponseEntity<>(key, HttpStatus.CREATED);
    }
}
