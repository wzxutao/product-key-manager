package cn.rypacker.productkeymanager.common;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static cn.rypacker.productkeymanager.config.StaticInformation.USER_DB_DIR;

@Slf4j
public class Sqlite3DBVersionUtil {

    public static final String FIRST_DB_NAME = "records.db-00000000";

    public static String getCurrentDbPath() throws IOException {
        String dbName = null;
        try(var stream = Files.list(Paths.get(System.getProperty("user.dir"), "data"))) {
            var optionalDbName = stream
                    .filter(f -> !Files.isDirectory(f))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(n -> n.startsWith("records.db")).max(Comparator.naturalOrder());
            if(optionalDbName.isPresent()){
                dbName = optionalDbName.get();
            }
        }
        return dbName == null ? null : USER_DB_DIR + File.separator + dbName;
    }

    public static String getNextDbPath() throws IOException {
        try(var stream = Files.list(Path.of(USER_DB_DIR))) {
            var optionalDbName = stream
                    .filter(f -> !Files.isDirectory(f))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(n -> n.startsWith("records.db")).max(Comparator.naturalOrder());
            if(optionalDbName.isEmpty() || optionalDbName.get().equals("records.db")){
                return FIRST_DB_NAME;
            }else{
                var dbName = optionalDbName.get();
                var index = Integer.parseInt(dbName.substring("records.db-".length()));
                return String.format("records.db-%08d", index + 1);
            }
        }
    }

    public static void deletePreviousDbs(String currentDbName) {
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
                            Files.delete(Path.of(USER_DB_DIR, n));
                        } catch (IOException e) {
                            log.warn(e.getMessage(), e);
                        }
                    });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
