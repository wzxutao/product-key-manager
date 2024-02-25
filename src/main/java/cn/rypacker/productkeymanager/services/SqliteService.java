package cn.rypacker.productkeymanager.services;

import cn.rypacker.productkeymanager.services.configstore.UserConfigStore;
import com.almworks.sqlite4java.SQLiteBackup;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static cn.rypacker.productkeymanager.common.Sqlite3DBVersionUtil.*;
import static cn.rypacker.productkeymanager.config.StaticInformation.USER_DB_BACKUP_DIR;

@Service
@Slf4j
public class SqliteService {

    @Autowired
    private UserConfigStore userConfigStore;

    private static final String AUTO_BACKUP_PREFIX = "auto-";

    public synchronized void backup(File backupFile) throws IOException {
        String dbPathCurr = getCurrentDbPath();
        if(dbPathCurr == null) {
            throw new IOException("No database found.");
        }


        SQLiteConnection sourceConnection = new SQLiteConnection(new File(dbPathCurr));
        SQLiteBackup backup = null;

        try{
            sourceConnection.openReadonly();

            backup = sourceConnection.initializeBackup(backupFile);
            do {
                backup.backupStep(-1);
            } while(!backup.isFinished());

        } catch (SQLiteException e) {
            throw new RuntimeException(e);
        }finally {
            if(backup != null){
                backup.dispose();
            }
            sourceConnection.dispose();
        }
    }

    @Scheduled(fixedDelay = 1000 * 60)
    public synchronized void autoBackup() {
        var today = LocalDateTime.now();
        var dateStr = String.format("%04d-%02d-%02d", today.getYear(), today.getMonthValue(), today.getDayOfMonth());
        var timeStr = String.format("%02d-%02d-%02d", today.getHour(), today.getMinute(), today.getSecond());

        var scheduledTime = userConfigStore.getData().getBackup();
        if(today.getHour() < scheduledTime.getHour() || today.getMinute() < scheduledTime.getMinute()) return;

        try (var stream = Files.list(Paths.get(USER_DB_BACKUP_DIR))) {
            if(stream.anyMatch(path -> path.getFileName().toString().startsWith(AUTO_BACKUP_PREFIX + dateStr))) {
                return;
            }
            var backupFile = new File(USER_DB_BACKUP_DIR + File.separator + AUTO_BACKUP_PREFIX + dateStr + "-" + timeStr);
            backup(backupFile);
            log.info("Auto backup created: " + backupFile.getAbsolutePath());
        } catch (IOException e) {
            throw new UncheckedIOException("Error reading directory", e);
        }

    }
}
