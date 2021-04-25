package cn.rypacker.productkeymanager.bootstrap;

import cn.rypacker.productkeymanager.config.StaticInformation;
import cn.rypacker.productkeymanager.services.FileSystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FirstTimeInitialization {

    private static Logger logger = LoggerFactory.getLogger(FirstTimeInitialization.class);

    public static void initIfNecessary() throws IOException {
        initDb();
    }

    private static void initDb() throws IOException {
        var db = new File(StaticInformation.USER_DB_PATH);
        if(!db.exists()){
            logger.info("db not found. creating");
            FileSystemUtil.mkdirIfNotExists(StaticInformation.USER_DB_DIR);
            var emptyDb = new File(StaticInformation.EMPTY_DB_PATH);
            Files.copy(emptyDb.toPath(), db.toPath(), StandardCopyOption.REPLACE_EXISTING);
            logger.info("empty db created");
        }else{
            logger.info("db found");
        }

    }
}
