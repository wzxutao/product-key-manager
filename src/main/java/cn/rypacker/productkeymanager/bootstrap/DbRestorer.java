package cn.rypacker.productkeymanager.bootstrap;

import cn.rypacker.productkeymanager.config.StaticInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class DbRestorer {

    private static final Logger logger = LoggerFactory.getLogger(DbRestorer.class);

    public static void restoreDbIfRequested(){
        var dbToRestore = new File(StaticInformation.DB_PENDING_RESTORE_PATH);
        if(dbToRestore.exists()){
            logger.info("find db pending restore. restoring...");
            try {
                Files.copy(dbToRestore.toPath(),
                        Path.of(StaticInformation.USER_DB_PATH), StandardCopyOption.REPLACE_EXISTING);
                logger.info("db restored.");
                if(!dbToRestore.delete()){
                    throw new RuntimeException("db restored, but the original file cannot be deleted");
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

}
