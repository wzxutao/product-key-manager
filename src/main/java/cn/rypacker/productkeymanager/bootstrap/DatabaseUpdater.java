package cn.rypacker.productkeymanager.bootstrap;

import cn.rypacker.productkeymanager.common.Sqlite3DBVersionUtil;
import cn.rypacker.productkeymanager.common.RecordStatus;
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
        updateTo0_0_6();
        updateTo1_0_0();

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
        try{
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
    }

    /**
     * adding unique constraint to product_key
     */
    protected static void updateTo1_0_0(){
        try{
            var conn = getDbConnection();
            final var sql = "CREATE UNIQUE INDEX idx_product_key ON json_record (product_key)";
            try(var stmt = conn.createStatement()){
                stmt.execute(sql);
                logger.info("updated db to 1.0.0");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            closeDbConnection(conn);
        }catch (RuntimeException e){
            var cause = e.getCause();
            if(cause.getClass() == SQLiteException.class){
                if(!cause.getMessage()
                        .contains("index idx_product_key already exists")){
                    throw e;
                }
            }else {
                throw e;
            }
        }

    }
}
