package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.models.RequestBodies;
import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import cn.rypacker.productkeymanager.services.auth.AdminAuth;
import cn.rypacker.productkeymanager.services.ciphers.JokeCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/check-key")
public class CheckKeyController {

    @Autowired
    AdminAuth adminAuth;

    @Autowired
    JokeCipher jokeCipher;
    @Autowired
    JsonRecordRepository jsonRecordRepository;
    @Autowired
    AdminAuthController adminAuthController;

    private boolean isAuthorized(String authToken){
        return authToken != null && adminAuth.isValidToken(authToken);
    }

    private String returnTemplateIfAuthSucceed(Model model, String original, String authToken){
        return isAuthorized(authToken) ? original : adminAuthController.getLogInPage(model);
    }

    @GetMapping(path="")
    public String get(Model model,
                      @CookieValue(value = "auth", required = false) String authToken){
        return returnTemplateIfAuthSucceed(model,"check-key", authToken);
    }

    @PostMapping(path="/info", consumes = "application/json")
    public ResponseEntity<?> retrieveInfo(@RequestBody RequestBodies.Key key,
                                          @CookieValue(value = "auth", required = false) String authToken){
        if(key == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(!isAuthorized(authToken)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        var record = jsonRecordRepository.findByProductKey(key.key);
        if(record.isEmpty()){
            return new ResponseEntity<>("无效序列号", HttpStatus.NOT_FOUND);
        }else if(record.size() != 1){
            throw new RuntimeException("key duplication");
        }

        return new ResponseEntity<>(record.get(0).getJsonString(), HttpStatus.OK);
    }
}
