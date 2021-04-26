package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.models.RequestBodies;
import cn.rypacker.productkeymanager.services.AdminAuth;
import cn.rypacker.productkeymanager.services.MandatoryFieldsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin/keygenFields")
@Controller
public class AdminSetMandatoryFieldsController {
    private static final Logger logger = LoggerFactory.getLogger(AdminSetMandatoryFieldsController.class);

    @Autowired
    MandatoryFieldsManager mandatoryFieldsManager;
    @Autowired
    AdminAuth adminAuth;

    private boolean isAuthorized(String authToken){
        return authToken != null && adminAuth.isValidToken(authToken);
    }

    @GetMapping(path = "")
    public String get(Model model){
        model.addAttribute("requiredFields", mandatoryFieldsManager.getFieldNames());
        return "admin/keygenFieldsSetting";
    }

    @PostMapping(path = "/modify")
    public ResponseEntity<?> modify(@RequestBody RequestBodies.MandatoryFieldNames reqBody,
                                    @CookieValue(value = "auth", required = false) String authToken){

        if(!isAuthorized(authToken)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        var listOfNames = reqBody.mandatoryFieldNames;
        mandatoryFieldsManager.replaceWith(listOfNames);
        logger.info("mandatory fields changed");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
