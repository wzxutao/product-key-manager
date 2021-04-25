package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.config.StaticInformation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class ManagerController {

    @GetMapping(path = "")
    public String get(Model model){

        model.addAttribute("versionNumber", StaticInformation.VERSION_NUMBER);
        return "admin";
    }

}
