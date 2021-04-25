package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.config.StaticInformation;
import cn.rypacker.productkeymanager.services.FileSystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@RequestMapping("/admin")
@Controller
public class ManagerController {

    private static Logger logger = LoggerFactory.getLogger(ManagerController.class);

    @GetMapping(path = "")
    public String get(Model model){

        model.addAttribute("versionNumber", StaticInformation.VERSION_NUMBER);
        return "admin";
    }

    @PostMapping(path = "/backup")
    public ResponseEntity<?> requestBackup(@RequestParam(value = "fileName") String fileName) throws IOException {
        var basePath = StaticInformation.USER_DB_BACKUP_DIR + File.separator;
        if(!FileSystemUtil.isValidFilePath(basePath + fileName)){
            // 422
            return new ResponseEntity<>("Invalid filename.", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        FileSystemUtil.mkdirIfNotExists(StaticInformation.USER_DB_BACKUP_DIR);
        Files.copy(Path.of(StaticInformation.USER_DB_PATH),
                Path.of(StaticInformation.USER_DB_BACKUP_DIR, fileName),
                StandardCopyOption.REPLACE_EXISTING);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "/restore")
    public String getRestorePage(Model model){
        var backUpFileName = FileSystemUtil.getBackupFileNames();
        model.addAttribute("backupFiles", backUpFileName);
        return "restoreDb";
    }

    @PostMapping(path = "/restore")
    public ResponseEntity<?> requestRestore(@RequestParam(value = "fileName") String fileName){
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
