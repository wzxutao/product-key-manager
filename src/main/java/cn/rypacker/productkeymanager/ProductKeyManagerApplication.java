package cn.rypacker.productkeymanager;

import cn.rypacker.productkeymanager.bootstrap.DatabaseUpdater;
import cn.rypacker.productkeymanager.bootstrap.DbRestorer;
import cn.rypacker.productkeymanager.bootstrap.FirstTimeInitializer;
import cn.rypacker.productkeymanager.bootstrap.MetadataMigrator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class ProductKeyManagerApplication {
    private static ConfigurableApplicationContext context;
    private static String[] args;

    private static final ThreadPoolExecutor mainThreadPool = new ThreadPoolExecutor(
            1, 1,
            0L, java.util.concurrent.TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(3)
    );

    public static void main(String[] args) {
        ProductKeyManagerApplication.args = args;
        mainThreadPool.submit(() -> ProductKeyManagerApplication.doMain(args));
    }

    public static void doMain(String[] args) {
        try {
            FirstTimeInitializer.initIfNecessary();
            DbRestorer.restoreDbIfRequested();
            DatabaseUpdater.updateIfNeeded();
            MetadataMigrator.migrateOrInitialize();
            var app =
                    new SpringApplicationBuilder(ProductKeyManagerApplication.class) {
                    }
                            .headless(true);
            context = app.run(args);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            System.exit(1);
        }

    }

    public static void restart(Callable<?> actionAfterClose) {
        mainThreadPool.submit(() -> {
            log.info("closing application context");
            context.close();
            log.info("application context closed");
        });
        mainThreadPool.submit(() -> {
            try {
                log.info("executing action after close");
                actionAfterClose.call();
                log.info("action after close executed");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        mainThreadPool.submit(() -> {
            log.info("restarting application");
            doMain(ProductKeyManagerApplication.args);
        });
    }

}
