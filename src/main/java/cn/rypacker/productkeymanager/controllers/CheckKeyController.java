package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.models.RequestBodies;
import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import cn.rypacker.productkeymanager.services.ciphers.JokeCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/check-key")
public class CheckKeyController {

    @Autowired
    JokeCipher jokeCipher;
    @Autowired
    JsonRecordRepository jsonRecordRepository;

    @GetMapping(path="")
    public String get(){
        return "check-key";
    }

    @PostMapping(path="/info", consumes = "application/json")
    public ResponseEntity<?> retrieveInfo(@RequestBody RequestBodies.Key key){
        if(key == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        System.out.println(key.key);
        long id;
        try{
            var idStr = jokeCipher.insecureDecrypt(key.key);
            id = Long.parseLong(idStr);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        var record = jsonRecordRepository.findById(id);
        if(record.isEmpty()){
            return new ResponseEntity<>("无效序列号", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(record.get().getJsonString(), HttpStatus.OK);
    }
}
