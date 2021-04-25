package cn.rypacker.productkeymanager.bootstrap;

import cn.rypacker.productkeymanager.config.StaticInformation;
import cn.rypacker.productkeymanager.desktopui.AdminAccountCreationFrame;
import cn.rypacker.productkeymanager.services.AdminAccountManager;
import cn.rypacker.productkeymanager.services.FileSystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Component
public class FirstTimeInitializer implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(FirstTimeInitializer.class);

    public static void initIfNecessary() throws IOException, InterruptedException {
        initDb();
        initAdminAccount();
    }

    private static void initAdminAccount() {
        if(AdminAccountManager.adminAccountExists()) return;
        var frame = new AdminAccountCreationFrame();
        while(frame.isRunning()){};
    }


    private static void initDb() throws IOException {
        var db = new File(StaticInformation.USER_DB_PATH);
        if(!db.exists()){
            logger.info("db not found. creating");
            FileSystemUtil.mkdirIfNotExists(StaticInformation.USER_DB_DIR);
            var emptyDb = new File(StaticInformation.EMPTY_DB_PATH);
            Files.copy(emptyDb.toPath(), db.toPath(), StandardCopyOption.REPLACE_EXISTING);
            logger.info("empty db created");
        }else{
            logger.info("db found");
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initIfNecessary();
    }
}
