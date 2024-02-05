package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.dto.RequestBodies;
import cn.rypacker.productkeymanager.services.configstore.UserConfigStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin/keygenFields")
@Controller
public class AdminSetMandatoryFieldsController {
    private static final Logger logger = LoggerFactory.getLogger(AdminSetMandatoryFieldsController.class);

    @Autowired
    private UserConfigStore userConfigStore;

    @GetMapping(path = "")
    public String get(Model model){
        model.addAttribute("requiredFields", userConfigStore.getData().getRecord().getMandatoryFields());
        return "admin/keygenFieldsSetting";
    }

    @PostMapping(path = "/modify")
    public ResponseEntity<?> modify(@RequestBody RequestBodies.MandatoryFieldNames reqBody){

        var listOfNames = reqBody.mandatoryFieldNames;
        userConfigStore.update(
                userConfig -> userConfig.getRecord().setMandatoryFields(listOfNames)
        );
        logger.info("mandatory fields changed");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
