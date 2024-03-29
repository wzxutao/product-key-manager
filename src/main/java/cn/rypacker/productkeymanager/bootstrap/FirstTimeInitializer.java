package cn.rypacker.productkeymanager.bootstrap;

import cn.rypacker.productkeymanager.config.StaticInformation;
import cn.rypacker.productkeymanager.services.AdminAccountManager;
import cn.rypacker.productkeymanager.services.FileSystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static cn.rypacker.productkeymanager.common.Sqlite3DBVersionUtil.FIRST_DB_NAME;
import static cn.rypacker.productkeymanager.common.Sqlite3DBVersionUtil.getCurrentDbPath;
import static cn.rypacker.productkeymanager.config.StaticInformation.USER_DB_DIR;

public class FirstTimeInitializer {

    private static final Logger logger = LoggerFactory.getLogger(FirstTimeInitializer.class);

    public static void initIfNecessary() throws IOException, InterruptedException {
        createDataDir();
        initDb();
        initAdminAccount();
    }

    private static void createDataDir(){
        FileSystemUtil.mkdirsIfNotExist(USER_DB_DIR);
    }

    private static void initAdminAccount() {
        if(AdminAccountManager.adminAccountExists()) return;
        throw new RuntimeException("admin account not found");
    }


    private static void initDb() throws IOException {
        var dbName = getCurrentDbPath();
        if(dbName == null){
            logger.info("db not found. creating");
            FileSystemUtil.mkdirsIfNotExist(USER_DB_DIR);
            var emptyDb = new File(StaticInformation.EMPTY_DB_PATH);
            var db = new File(USER_DB_DIR + File.separator + FIRST_DB_NAME);
            Files.copy(emptyDb.toPath(), db.toPath(), StandardCopyOption.REPLACE_EXISTING);
            logger.info("empty db created");
        }else{
//            logger.info("db found");
        }

    }

}
