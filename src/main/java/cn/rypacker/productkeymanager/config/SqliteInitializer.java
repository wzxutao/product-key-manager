package cn.rypacker.productkeymanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.File;

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
    public DataSource dataSourceProd() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        var pwd = System.getProperty("user.dir");
        dataSource.setUrl(String.format("jdbc:sqlite:%s%sdata%srecords.db",
                pwd, File.separator, File.separator));
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }
}
