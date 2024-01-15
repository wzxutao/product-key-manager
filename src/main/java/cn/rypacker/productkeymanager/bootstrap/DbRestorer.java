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
import java.util.Comparator;

import static cn.rypacker.productkeymanager.config.StaticInformation.USER_DB_DIR;

@Slf4j
public class DbRestorer {

    private static String getNextDbPath() throws IOException {
        try(var stream = Files.list(Path.of(USER_DB_DIR))) {
            var optionalDbName = stream
                    .filter(f -> !Files.isDirectory(f))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(n -> n.startsWith("records.db")).max(Comparator.naturalOrder());
            if(optionalDbName.isEmpty() || optionalDbName.get().equals("records.db")){
                return "records.db-00000000";
            }else{
                var dbName = optionalDbName.get();
                var index = Integer.parseInt(dbName.substring("records.db-".length()));
                return String.format("records.db-%08d", index + 1);
            }
        }
    }

    private static void deletePreviousDbs(String currentDbName) {
        try(var stream = Files.list(Path.of(USER_DB_DIR))) {
            stream
                    .filter(f -> !Files.isDirectory(f))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(n -> n.startsWith("records.db"))
                    .filter(n -> !n.equals(currentDbName))
                    .forEach(n -> {
                        try {
                            log.info("deleting previous db: {}", n);
                            Files.delete(Path.of(USER_DB_DIR, "data", n));
                        } catch (IOException e) {
                            log.warn(e.getMessage(), e);
                        }
                    });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

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
