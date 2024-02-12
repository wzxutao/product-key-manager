package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.dto.RequestBodies;
import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/check-key")
public class CheckKeyController {

    JsonRecordRepository jsonRecordRepository;
    @GetMapping(path="")
    public String get(Model model){
        return "check-key";
    }

    @PostMapping(path="/info", consumes = "application/json")
    public ResponseEntity<?> retrieveInfo(@RequestBody RequestBodies.Key key){
        if(key == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        var record = jsonRecordRepository.findByProductKey(key.key);
        if(record == null){
            return new ResponseEntity<>("无效序列号", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(record.getJsonString(), HttpStatus.OK);
    }
}
