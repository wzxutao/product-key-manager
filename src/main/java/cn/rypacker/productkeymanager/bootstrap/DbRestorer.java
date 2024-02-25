package cn.rypacker.productkeymanager.bootstrap;

import cn.rypacker.productkeymanager.config.StaticInformation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static cn.rypacker.productkeymanager.config.StaticInformation.USER_DB_DIR;

import static cn.rypacker.productkeymanager.common.Sqlite3DBVersionUtil.*;

@Slf4j
public class DbRestorer {

    public static void restoreDbIfRequested() throws InterruptedException, IOException {
        var dbToRestore = new File(StaticInformation.DB_PENDING_RESTORE_PATH);
        if(dbToRestore.exists()){
            log.info("find db pending restore. restoring...");
            int retryCount = 5;
            var nextDbName = getNextDbPath();
            log.info("nextDbName: {}", nextDbName);

            while(retryCount-- > 0){
                try {
                    Files.copy(dbToRestore.toPath(),
                            Path.of(USER_DB_DIR, nextDbName), StandardCopyOption.REPLACE_EXISTING);
                    log.info("db restored.");
                    break;
                } catch (IOException e) {
                    if(retryCount == 0){
                        throw new UncheckedIOException(e);
                    }
                    Thread.sleep(2000);
                }
            }
            retryCount = 5;
            while(retryCount-- > 0) {
                try {
                    Files.delete(dbToRestore.toPath());
                    log.info("db pending restore deleted.");
                    break;
                } catch (IOException e) {
                    if(retryCount == 0){
                        throw new UncheckedIOException(e);
                    }
                    Thread.sleep(2000);
                }
            }
            deletePreviousDbs(nextDbName);
        }
    }

}
