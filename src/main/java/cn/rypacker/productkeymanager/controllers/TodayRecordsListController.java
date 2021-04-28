package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import cn.rypacker.productkeymanager.services.DatetimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
                findByMilliCreatedBetween(fromS * 1000, (toS * 1000) + 999);
        model.addAttribute("records", records);

        return "today-records-list";
    }
}
