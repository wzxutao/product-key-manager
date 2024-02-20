package cn.rypacker.productkeymanager.services;

import com.almworks.sqlite4java.SQLiteBackup;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

import static cn.rypacker.productkeymanager.common.Sqlite3DBVersionUtil.*;

@Service
public class SqliteService {

    public void backup(File backupFile) throws IOException {
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
}
