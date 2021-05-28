package cn.rypacker.productkeymanager.controllers;


import cn.rypacker.productkeymanager.desktopui.AdminConfirm;
import cn.rypacker.productkeymanager.models.RequestBodies;
import cn.rypacker.productkeymanager.services.auth.AdminAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/auth")
@Controller
public class AdminAuthController {

    @Autowired
    AdminAuth adminAuth;
    @Autowired
    AdminConfirm adminConfirm;


    @GetMapping("")
    public String getLogInPage(Model model){

//        System.out.println("get admin auth");
        model.addAttribute("validMinutes", adminConfirm.getTokenValidMinutes());
        System.out.println(adminConfirm.getTokenValidMinutes());
        return "admin/adminAuth";
    }

    @PostMapping("/login")
    public ResponseEntity<?> getAuthToken(@RequestBody RequestBodies.LoginForm loginForm,
                                          HttpServletRequest request){
        if(loginForm == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        var account = loginForm.account;
        var password = loginForm.password;
        if(!adminAuth.isAdmin(account, password)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        var token = adminConfirm.getTokenIfApproved(request.getRemoteAddr());
//        System.out.println(token);
        if(token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

}
