package cn.rypacker.productkeymanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Collectors;

@Configuration
public class SqliteInitializer {

    @Profile("dev")
    @Bean
    public DataSource dataSourceDev() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        var pwd = System.getProperty("user.dir");
        dataSource.setUrl(String.format("jdbc:sqlite:%s%sdata%stest.db",
                pwd, File.separator, File.separator));
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Profile("prod")
    @Bean
    public DataSource dataSourceProd() throws IOException {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        var pwd = System.getProperty("user.dir");
        String dbName = "records.db-00000000";
        try(var stream = Files.list(Paths.get(pwd, "data"))) {
            var optionalDbName = stream
                    .filter(f -> !Files.isDirectory(f))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(n -> n.startsWith("records.db")).max(Comparator.naturalOrder());
            if(optionalDbName.isPresent()){
                dbName = optionalDbName.get();
            }
        }
        dataSource.setUrl(String.format("jdbc:sqlite:%s%sdata%s%s",
                pwd, File.separator, File.separator, dbName));
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }
}
