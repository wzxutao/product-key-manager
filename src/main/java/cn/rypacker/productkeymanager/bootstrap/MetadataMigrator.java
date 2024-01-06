package cn.rypacker.productkeymanager.bootstrap;

import cn.rypacker.productkeymanager.services.FileSystemUtil;
import cn.rypacker.productkeymanager.services.configstore.UserConfig;
import cn.rypacker.productkeymanager.services.configstore.UserConfigStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static cn.rypacker.productkeymanager.config.StaticInformation.LEGACY_FILE_DIR;
import static cn.rypacker.productkeymanager.config.StaticInformation.USER_DB_DIR;

@Slf4j
public class MetadataMigrator {

    class UserConfigKeys {
        public static final String KEY_LENGTH = "key.length";
        public static final String NORMAL_AUTH_VALID_DAYS = "auth.normal.validDays";
        public static final String ADMIN_AUTH_VALID_MINUTES = "auth.admin.validMinutes";
    }

    public static final String MANDATORY_FIELDS_FILE_PATH_V0 = USER_DB_DIR + "/mandatoryFields";
    public static final String METADATA_FILE_PATH_V0 = USER_DB_DIR + "/metadata";
    public static final String ACCOUNTS_FILE_PATH_V0 = USER_DB_DIR + "/accounts";


    public static void migrateOrInitialize() {
        var objectMapper = new ObjectMapper();
        // check if new json file exists
        var userConfigStore = new UserConfigStore();
        try{
            userConfigStore.getData();
            return;
        }catch (UncheckedIOException doMigration){
            // new json file not found
        }

        var config = new UserConfig();

        // metadata
        var metadata = readLegacyPropertyFile();
        if(metadata != null){

            config.getKey().setLength(Integer.parseInt(metadata.getOrDefault(UserConfigKeys.KEY_LENGTH,  config.getKey().getLength() + "")));
            config.getAuth().getNormal().setValidDays(Integer.parseInt(metadata.getOrDefault(UserConfigKeys.NORMAL_AUTH_VALID_DAYS, config.getAuth().getNormal().getValidDays() + "")));
            config.getAuth().getAdmin().setValidMinutes(Integer.parseInt(metadata.getOrDefault(UserConfigKeys.ADMIN_AUTH_VALID_MINUTES, config.getAuth().getAdmin().getValidMinutes() + "")));
        }

        // mandatory fields
        var mandatoryFields = readMandatoryFieldsFile();
        if(mandatoryFields != null){
            config.getRecord().setMandatoryFields(new ArrayList<>(mandatoryFields));
        }

        // accounts
        var accounts = readAccountsFile();
        if(accounts != null){
            config.setAccounts(accounts.entrySet().stream()
                    .map(e -> {
                        var account = new UserConfig.Account();
                        account.setUsername(e.getKey());
                        account.setHash(e.getValue());
                        return account;
                    })
                    .collect(Collectors.toList()));
        }

        // save
        userConfigStore.saveData(config);

        // move legacy files
        FileSystemUtil.mkdirsIfNotExist(LEGACY_FILE_DIR);
        FileSystemUtil.moveFileIfExists(MANDATORY_FIELDS_FILE_PATH_V0, LEGACY_FILE_DIR + "/mandatoryFields");
        FileSystemUtil.moveFileIfExists(METADATA_FILE_PATH_V0, LEGACY_FILE_DIR + "/metadata");
        FileSystemUtil.moveFileIfExists(ACCOUNTS_FILE_PATH_V0, LEGACY_FILE_DIR + "/accounts");
        log.info("migration of user config complete.");
    }

    private static ConcurrentHashMap<String, String> readLegacyPropertyFile(){
        var f = new File(METADATA_FILE_PATH_V0);

        if(!f.exists()) return null;

        try(var oi = new ObjectInputStream(
                new BufferedInputStream((
                        new FileInputStream(f))))
        ){
            return (ConcurrentHashMap<String, String>) oi.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private static LinkedHashSet<String> readMandatoryFieldsFile() {
        var rv = new LinkedHashSet<String>();
        try(var scanner = new Scanner(
                new BufferedInputStream(
                        new FileInputStream(MANDATORY_FIELDS_FILE_PATH_V0)))){
            while(scanner.hasNextLine()){
                rv.add(scanner.nextLine());
            }
            return rv;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private static ConcurrentHashMap<String, String> readAccountsFile(){
        var f = new File(ACCOUNTS_FILE_PATH_V0);

        if(!f.exists()) return null;

        try(var oi = new ObjectInputStream(
                new BufferedInputStream((
                        new FileInputStream(f))))
        ){
            return (ConcurrentHashMap<String, String>) oi.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
