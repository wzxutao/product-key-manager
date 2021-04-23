package cn.rypacker.productkeymanager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/new-key")
@Controller
public class NewKeyController {

    @GetMapping
    public String get(){
        return "new-key";
    }
}
