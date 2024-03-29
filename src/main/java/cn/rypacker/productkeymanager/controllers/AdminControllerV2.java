package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.ProductKeyManagerApplication;
import cn.rypacker.productkeymanager.common.Sqlite3DBVersionUtil;
import cn.rypacker.productkeymanager.config.StaticInformation;
import cn.rypacker.productkeymanager.dto.AutoBackupConfigDto;
import cn.rypacker.productkeymanager.dto.KeyGenStats;
import cn.rypacker.productkeymanager.dto.UserCredentials;
import cn.rypacker.productkeymanager.entity.UserConfig;
import cn.rypacker.productkeymanager.repositories.NormalAccountRepository;
import cn.rypacker.productkeymanager.services.FileSystemUtil;
import cn.rypacker.productkeymanager.services.KeyGenerator;
import cn.rypacker.productkeymanager.services.SqliteService;
import cn.rypacker.productkeymanager.services.configstore.UserConfigStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/man/v2")
@Slf4j
public class AdminControllerV2 {

    @Autowired
    private UserConfigStore userConfigStore;

    @Autowired
    private KeyGenerator keyGenerator;

    @Autowired
    private NormalAccountRepository normalAccountRepository;

    @Autowired
    private SqliteService sqliteService;


    @PostMapping(path = "/backup")
    public ResponseEntity<?> requestBackup(@RequestParam(value = "fileName") String fileName)
            throws IOException {

        var backupFile = new File(StaticInformation.USER_DB_BACKUP_DIR
                + File.separator
                + fileName);
        if (!FileSystemUtil.isValidFilePath(backupFile.getAbsolutePath())) {
            return new ResponseEntity<>("Invalid filename.", HttpStatus.BAD_REQUEST);
        }

        FileSystemUtil.mkdirsIfNotExist(StaticInformation.USER_DB_BACKUP_DIR);
        sqliteService.backup(backupFile);

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

    @PutMapping(path = "/mandatory-fields/update")
    public ResponseEntity<?> modify(@RequestBody List<String> mandatoryFieldNames){
        log.info("updating mandatory fields: " + mandatoryFieldNames);
        userConfigStore.update(
                userConfig -> userConfig.getRecord().setMandatoryFields(mandatoryFieldNames)
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping(path = "/key-length/update")
    public ResponseEntity<?> updateKeyLength(@RequestParam("length") Integer length) {
        if(length == null || length < KeyGenerator.MIN_LENGTH)
            return ResponseEntity.badRequest().body("长度最少 " + KeyGenerator.MIN_LENGTH + " 位");

        keyGenerator.setKeyLength(length);
        return ResponseEntity.ok().build();
    }


    @GetMapping(path = "/admin-expiry/get")
    public int getAdminExpiryMinutes() {
        return userConfigStore.getData().getAuth().getAdmin().getValidMinutes();
    }

    @PutMapping(path = "/admin-expiry/update")
    public ResponseEntity<?> updateAdminExpiry(@RequestParam("length") Integer length) {
        if(length == null || length < 1)
            return ResponseEntity.badRequest().body("Illegal expiry minutes: " + length);

        userConfigStore.update(c -> c.getAuth().getAdmin().setValidMinutes(length));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/key-gen-stats/get")
    public KeyGenStats getKeyGenStats() {
        return keyGenerator.getStats();
    }

    @GetMapping("/key-gen-blacklist/get")
    public List<String> getKeyGenBlacklist(){
        return keyGenerator.getBlackList();
    }

    @PostMapping("/key-gen-blacklist/update")
    public ResponseEntity<?> updateKeyGenBlacklist(@RequestBody List<String> blacklist) {
        keyGenerator.setBlackList(blacklist);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/normal-accounts/get")
    public List<String> getNormalAccounts(){
        return new ArrayList<>(normalAccountRepository.findAllExistingUserNames());
    }

    @PostMapping("/normal-accounts/upsert")
    public ResponseEntity<?> upsertNormalAccount(@Valid @RequestBody UserCredentials credentials){
        normalAccountRepository.upsert(credentials.username, credentials.getPassword());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/normal-accounts/delete")
    public ResponseEntity<?> deleteNormalAccount(@RequestParam String username){
        normalAccountRepository.remove(username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/normal-accounts/verify")
    public ResponseEntity<Boolean> verifyNormalAccount(@Valid @RequestBody UserCredentials credentials){
        return ResponseEntity.ok(normalAccountRepository.matches(credentials.username, credentials.getPassword()));
    }

    @GetMapping("/auto-backup/time/get")
    public AutoBackupConfigDto getAutoBackupTime(){
        var backupConfig = userConfigStore.getData().getBackup();
        return new AutoBackupConfigDto(backupConfig.getHour(), backupConfig.getMinute());
    }

    @PutMapping("/auto-backup/time/update")
    public ResponseEntity<?> updateAutoBackupTime(@RequestBody AutoBackupConfigDto config){
        userConfigStore.update(c -> {
            c.getBackup().setHour(config.getHour());
            c.getBackup().setMinute(config.getMinute());
        });
        return ResponseEntity.ok().build();
    }

}
