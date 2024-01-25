package cn.rypacker.productkeymanager.controllers;

import cn.rypacker.productkeymanager.ProductKeyManagerApplication;
import cn.rypacker.productkeymanager.common.Sqlite3DBVersionUtil;
import cn.rypacker.productkeymanager.config.StaticInformation;
import cn.rypacker.productkeymanager.models.RequestBodies;
import cn.rypacker.productkeymanager.repositories.JsonRecordRepository;
import cn.rypacker.productkeymanager.repositories.NormalAccountRepository;
import cn.rypacker.productkeymanager.services.DatetimeUtil;
import cn.rypacker.productkeymanager.services.FileSystemUtil;
import cn.rypacker.productkeymanager.services.KeyGenerator;
import cn.rypacker.productkeymanager.services.auth.AdminAuth;
import cn.rypacker.productkeymanager.services.configstore.UserConfigStore;
import cn.rypacker.productkeymanager.services.update.UpdateFailedException;
import cn.rypacker.productkeymanager.services.update.Updater;
import cn.rypacker.productkeymanager.specification.JsonRecordSpecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/admin")
@Controller
public class AdminController {

    private static Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminAuth adminAuth;
    @Autowired
    private JsonRecordRepository jsonRecordRepository;
    @Autowired
    private KeyGenerator keyGenerator;
    @Autowired
    private Updater updater;
    @Autowired
    private NormalAccountRepository normalAccountRepository;
    @Autowired
    private UserConfigStore userConfigStore;
    @Autowired
    private AdminAuthController adminAuthController;

    @Autowired
    private DataSource dataSource;

    private volatile boolean checkingUpdate = false;


    /**
     * returns the original template name otherwise auth page
     *
     * @param original  template name
     * @param authToken
     * @return
     */
    private String returnTemplateIfAuthSucceed(Model model, String original, String authToken) {
        return isAuthorized(authToken) ? original : adminAuthController.getLogInPage(model);
    }

    private boolean isAuthorized(String authToken) {
        return authToken != null && adminAuth.isValidToken(authToken);
    }

    @GetMapping(path = "")
    public String get(Model model, @CookieValue(value = "auth", required = false) String authToken) {
        model.addAttribute("versionNumber", StaticInformation.VERSION_NUMBER);
        model.addAttribute("keyLength", keyGenerator.getKeyLength());
        model.addAttribute("adminAuthMinutes",
                userConfigStore.getData().getAuth().getAdmin().getValidMinutes());
        return returnTemplateIfAuthSucceed(model, "admin/admin", authToken);
    }

    @PostMapping(path = "/adminAuthMinutes")
    public ResponseEntity<?> setAdminAuthMinutes(@RequestParam(value = "minutes") String minutes) {
        try {
            var m = Integer.parseInt(minutes);
            if (m <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            userConfigStore.update(
                    c -> c.getAuth().getAdmin().setValidMinutes(m)
            );
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

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

    @GetMapping(path = "/restore")
    public String getRestorePage(Model model, @CookieValue(value = "auth", required = false) String authToken) {
        var backUpFileName = FileSystemUtil.getBackupFileNames();
        model.addAttribute("backupFiles", backUpFileName);
        return returnTemplateIfAuthSucceed(model, "admin/restoreDb", authToken);
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

        logger.info("restarting the server to restore db: " + fileName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/records")
    public String getAllRecords(Model model) {
        model.addAttribute("records", jsonRecordRepository.findAll());
        return "admin/recordsView";
    }

    @PostMapping(path = "/records", consumes = "application/json")
    public String getRecords(Model model,
                             @RequestBody RequestBodies.RecordsQuery recordsQuery) {
//        System.out.println(recordsQuery.fromTime + ", " + recordsQuery.toTime);

        if (recordsQuery.fromTime == null || recordsQuery.fromTime.isBlank()) {
            recordsQuery.fromTime = DatetimeUtil.epochSecondsToFinalDate(0);
        }

        if (recordsQuery.toTime == null || recordsQuery.toTime.isBlank()) {
            recordsQuery.toTime = DatetimeUtil.epochSecondsToFinalDate(Instant.now().plus(
                    Duration.ofDays(1)).toEpochMilli() / 1000L);
        }

        model.addAttribute("fromTime", recordsQuery.fromTime);
        model.addAttribute("toTime", recordsQuery.toTime);
        try {
            var fromS = DatetimeUtil.finalDateToEpochSeconds(recordsQuery.fromTime);
            var toS = DatetimeUtil.finalDateToEpochSeconds(recordsQuery.toTime);
            var records = jsonRecordRepository.findAll(JsonRecordSpecs.createdMilliBetween(fromS * 1000, (toS * 1000) + 999));
//            System.out.println(fromS * 1000 + " -> " + ((toS * 1000) + 999));
            model.addAttribute("records", records);

        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "illegal format");
        }


        return "admin/recordsView";
    }

    @PostMapping(path = "/key-length")
    public ResponseEntity<?> changeKeyLength(@RequestParam(value = "length") String length) {
        try {
            int len = Integer.parseInt(length);
            keyGenerator.setKeyLength(len);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/update")
    public ResponseEntity<?> checkUpdate() {
        if (checkingUpdate) return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);

        checkingUpdate = true;
        try {
            var hasUpdate = !updater.isLatestVersion();
            if (hasUpdate) {
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        updater.update();
                    } catch (UpdateFailedException e) {
                        e.printStackTrace();
                    }
                }).start();
                checkingUpdate = false;
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                checkingUpdate = false;
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            checkingUpdate = false;
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/accounts")
    public String getAccountsManagementPage(
            Model model) {

        model.addAttribute("accounts",
                normalAccountRepository.findAllExistingUserNames());
        return "admin/normalAccountsManager";
    }

    @PostMapping(path = "/accounts-add")
    public ResponseEntity<?> addAccounts(
            @RequestBody Map<String, String> reqBody
    ) {
        reqBody.forEach((k, v) -> {
            normalAccountRepository.add(k, v);
        });
        logger.info("accounts created: " + reqBody);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/accounts-remove", consumes = "application/json")
    public ResponseEntity<?> removeAccounts(
            @RequestBody RequestBodies.OneList<String> reqBody) {
        var list = reqBody.list;

        if (list == null) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }

        for (var key :
                list) {
            normalAccountRepository.remove(key);
        }

        logger.info("accounts removed: " + list);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
