package cn.rypacker.productkeymanager.bootstrap;

import cn.rypacker.productkeymanager.config.StaticInformation;
import cn.rypacker.productkeymanager.models.RecordStatus;
import cn.rypacker.productkeymanager.services.datamanagers.PropertyManager;
import cn.rypacker.productkeymanager.services.datamanagers.PropertyManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUpdater {

    private static PropertyManager propertyManager = new PropertyManagerImpl();
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUpdater.class);

    protected static String dbUrl;

    static {
        setDbPath(StaticInformation.USER_DB_PATH);
    }

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
            return DriverManager.getConnection(dbUrl);
        }catch (SQLException e){
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

    protected static void setDbPath(String dbPath){
        dbUrl = String.format(
                "jdbc:sqlite:%s", dbPath);
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
