package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.ProductKeyManagerApplication;
import cn.rypacker.productkeymanager.common.Sqlite3DBVersionUtil;
import cn.rypacker.productkeymanager.config.StaticInformation;
import cn.rypacker.productkeymanager.services.FileSystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/man/v2")
@Slf4j
public class AdminControllerV2 {


    @PostMapping(path = "/backup")
    public ResponseEntity<?> requestBackup(@RequestParam(value = "fileName") String fileName)
            throws IOException {

        var basePath = StaticInformation.USER_DB_BACKUP_DIR + File.separator;
        if (!FileSystemUtil.isValidFilePath(basePath + fileName)) {
            return new ResponseEntity<>("Invalid filename.", HttpStatus.BAD_REQUEST);
        }

        FileSystemUtil.mkdirsIfNotExist(StaticInformation.USER_DB_BACKUP_DIR);
        Files.copy(Path.of(Sqlite3DBVersionUtil.getCurrentDbPath()),
                Path.of(StaticInformation.USER_DB_BACKUP_DIR, fileName),
                StandardCopyOption.REPLACE_EXISTING);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(path = "/backup-files")
    public ResponseEntity<List<String>> getBackupFiles() {
        return ResponseEntity.ok(FileSystemUtil.getBackupFileNames().stream().sorted().collect(Collectors.toList()));
    }

    @PostMapping(path = "/restore")
    public ResponseEntity<?> requestRestore(@RequestParam(value = "fileName") String fileName) {

        var restoreSrc = Path.of(StaticInformation.USER_DB_BACKUP_DIR, fileName);
        if (!restoreSrc.toFile().exists()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        ProductKeyManagerApplication.restart(() -> Files.copy(restoreSrc,
                Path.of(StaticInformation.DB_PENDING_RESTORE_PATH),
                StandardCopyOption.REPLACE_EXISTING));

        log.info("restarting the server to restore db: " + fileName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
