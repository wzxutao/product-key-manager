package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.config.StaticInformation;
import cn.rypacker.productkeymanager.services.AdminAuth;
import cn.rypacker.productkeymanager.services.FileSystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@RequestMapping("/admin")
@Controller
public class AdminController {

    private static Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    AdminAuth adminAuth;

    /**
     * returns the original template name otherwise auth page
     * @param original template name
     * @param authToken
     * @return
     */
    private String returnTemplateIfAuthSucceed(String original, String authToken){
        return isAuthorized(authToken) ? original : "admin/adminAuth";
    }

    private boolean isAuthorized(String authToken){
        return authToken != null && adminAuth.isValidToken(authToken);
    }

    @GetMapping(path = "")
    public String get(Model model, @CookieValue(value = "auth", required = false) String authToken){
        model.addAttribute("versionNumber", StaticInformation.VERSION_NUMBER);
//        System.out.println("auth: " + isAuthorized(authToken) + ", token: " + authToken);
        return returnTemplateIfAuthSucceed("admin/admin", authToken);
    }

    @PostMapping(path = "/backup")
    public ResponseEntity<?> requestBackup(@RequestParam(value = "fileName") String fileName,
                                           @CookieValue(value = "auth", required = false) String authToken)
            throws IOException {
        if(!isAuthorized(authToken)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        var basePath = StaticInformation.USER_DB_BACKUP_DIR + File.separator;
        if(!FileSystemUtil.isValidFilePath(basePath + fileName)){
            // 422
            return new ResponseEntity<>("Invalid filename.", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        FileSystemUtil.mkdirsIfNotExist(StaticInformation.USER_DB_BACKUP_DIR);
        Files.copy(Path.of(StaticInformation.USER_DB_PATH),
                Path.of(StaticInformation.USER_DB_BACKUP_DIR, fileName),
                StandardCopyOption.REPLACE_EXISTING);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "/restore")
    public String getRestorePage(Model model, @CookieValue(value = "auth", required = false) String authToken){
        var backUpFileName = FileSystemUtil.getBackupFileNames();
        model.addAttribute("backupFiles", backUpFileName);
        return returnTemplateIfAuthSucceed("admin/restoreDb", authToken);
    }

    @PostMapping(path = "/restore")
    public ResponseEntity<?> requestRestore(@RequestParam(value = "fileName") String fileName,
                                            @CookieValue(value = "auth", required = false) String authToken){

        if(!isAuthorized(authToken)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        var restoreSrc = Path.of(StaticInformation.USER_DB_BACKUP_DIR, fileName);
        if(!restoreSrc.toFile().exists()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Files.copy(restoreSrc, Path.of(StaticInformation.USER_DB_PATH), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("db restored: " + fileName);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
