package cn.rypacker.productkeymanager.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;

import static cn.rypacker.productkeymanager.common.Sqlite3DBVersionUtil.getCurrentDbPath;

@Configuration
@Slf4j
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
        var dbName = getCurrentDbPath();
        log.info("current db name: {}", dbName);
        dataSource.setUrl(String.format("jdbc:sqlite:%s%s%s",
                pwd, File.separator, dbName));
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }
}
