package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.models.RecordStatus;
import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import cn.rypacker.productkeymanager.services.DatetimeUtil;
import cn.rypacker.productkeymanager.services.auth.NormalAccountAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/today-records")
@Controller
public class TodayRecordsListController {

    @Autowired
    JsonRecordRepository jsonRecordRepository;
    @Autowired
    NormalAccountAuth normalAccountAuth;

    private boolean isAuthorized(String authToken){
        return authToken != null && normalAccountAuth.isTokenValid(authToken);
    }

    private String returnTemplateIfAuthSucceed(String original, String authToken){
        return isAuthorized(authToken) ? original : "new-key-auth";
    }

    @GetMapping(path = "")
    public String get(Model model,
                      @CookieValue(value = "normalAuth", required = false) String authToken){

        var fromS = DatetimeUtil.getTodayEpochSeconds(true);
        var toS = DatetimeUtil.getTodayEpochSeconds(false);

        var records = jsonRecordRepository.
                findByMilliCreatedBetweenAndStatusEquals(
                        fromS * 1000, (toS * 1000) + 999,
                        RecordStatus.NORMAL);
        model.addAttribute("records", records);

        return returnTemplateIfAuthSucceed("today-records-list", authToken);
    }


    private ResponseEntity<?> changeRecordStatus(String key, int toStatus){
        var rl = jsonRecordRepository.findByProductKey(key);
        if(rl.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        var record = rl.get(0);
        record.setStatus(toStatus);
        jsonRecordRepository.save(record);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/delete")
    public ResponseEntity<?> markDelete(@RequestParam(value = "key") String key,
                                        @CookieValue(value = "normalAuth", required = false)
                                                String authToken){
        if(!isAuthorized(authToken)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return changeRecordStatus(key, RecordStatus.MARKED_DELETE);
    }

    @PostMapping(path = "/undo-delete")
    public ResponseEntity<?> undoDelete(@RequestParam(value = "key") String key,
                                        @CookieValue(value = "normalAuth", required = false)
                                                String authToken){
        if(!isAuthorized(authToken)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return changeRecordStatus(key, RecordStatus.NORMAL);

    }
}
