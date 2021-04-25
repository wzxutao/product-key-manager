package cn.rypacker.productkeymanager.controllers;


import cn.rypacker.productkeymanager.desktopui.AdminConfirm;
import cn.rypacker.productkeymanager.models.RequestBodies;
import cn.rypacker.productkeymanager.services.AdminAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/auth")
@Controller
public class AdminAuthController {

    @Autowired
    AdminAuth adminAuth;
    @Autowired
    AdminConfirm adminConfirm;

    @GetMapping("")
    public String getLogInPage(){

//        System.out.println("get admin auth");
        return "adminAuth";
    }

    @PostMapping("/login")
    public ResponseEntity<?> getAuthToken(@RequestBody RequestBodies.LoginForm loginForm){
        if(loginForm == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        var account = loginForm.account;
        var password = loginForm.password;
        if(!adminAuth.isAdmin(account, password)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        // todo change to client ip
        var token = adminConfirm.getTokenIfApproved("某人");
        System.out.println(token);
        if(token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

}
