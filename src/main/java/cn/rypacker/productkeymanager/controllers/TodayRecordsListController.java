package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.models.RecordStatus;
import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import cn.rypacker.productkeymanager.services.DatetimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/today-records")
@Controller
public class TodayRecordsListController {

    @Autowired
    JsonRecordRepository jsonRecordRepository;

    @GetMapping(path = "")
    public String get(Model model){

        var fromS = DatetimeUtil.getTodayEpochSeconds(true);
        var toS = DatetimeUtil.getTodayEpochSeconds(false);

        var records = jsonRecordRepository.
                findByMilliCreatedBetweenAndStatusEquals(
                        fromS * 1000, (toS * 1000) + 999,
                        RecordStatus.NORMAL);
        model.addAttribute("records", records);

        return "today-records-list";
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
    public ResponseEntity<?> markDelete(@RequestParam(value = "key") String key){
        return changeRecordStatus(key, RecordStatus.MARKED_DELETE);
    }

    @PostMapping(path = "/undo-delete")
    public ResponseEntity<?> undoDelete(@RequestParam(value = "key") String key){
        return changeRecordStatus(key, RecordStatus.NORMAL);

    }
}
