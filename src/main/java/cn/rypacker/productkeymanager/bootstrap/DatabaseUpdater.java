package cn.rypacker.productkeymanager.bootstrap;

import cn.rypacker.productkeymanager.common.Sqlite3DBVersionUtil;
import cn.rypacker.productkeymanager.config.StaticInformation;
import cn.rypacker.productkeymanager.models.RecordStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUpdater {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseUpdater.class);


    public static void updateIfNeeded() {
        try{
            updateTo0_0_6();
        }catch (RuntimeException e){
            var cause = e.getCause();
            if(cause.getClass() == SQLiteException.class){
                if(!cause.getMessage()
                        .contains("duplicate column name: status")){
                    throw e;
                }
            }else {
                throw e;
            }
        }

//        if(dbVersion == null){
//            updateTo0_0_6();
//            metadataManager.put(MetadataManager.MetaData.DB_VERSION, "0.0.6");
//        }
    }

    private static Connection getDbConnection(){
        try{
            var dbUrl = String.format(
                    "jdbc:sqlite:%s", Sqlite3DBVersionUtil.getCurrentDbPath());
            return DriverManager.getConnection(dbUrl);
        }catch (SQLException | IOException e){
            throw new RuntimeException(e);
        }
    }

    private static void closeDbConnection(Connection connection){
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * added a column to JSONRecord: status
     */
    protected static void updateTo0_0_6(){
        var conn = getDbConnection();
        final var sql = String.format(
                "ALTER TABLE json_record ADD COLUMN status INTEGER DEFAULT %d", RecordStatus.NORMAL);
        try(var stmt = conn.createStatement()){
            stmt.execute(sql);
            logger.info("updated db to 0.0.6");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        closeDbConnection(conn);
    }
}
