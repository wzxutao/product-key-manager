package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.services.configstore.UserConfigStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/keygen/v2")
public class KeyGenControllerV2 {

    @Autowired
    private UserConfigStore userConfigStore;

    @GetMapping("/mandatory-fields")
    public List<String> getMandatoryFields(){
        return userConfigStore.getData().getRecord().getMandatoryFields();
    }
}
