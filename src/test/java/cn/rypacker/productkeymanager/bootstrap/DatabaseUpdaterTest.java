package cn.rypacker.productkeymanager.bootstrap;

import cn.rypacker.productkeymanager.config.StaticInformation;
import cn.rypacker.productkeymanager.services.FileSystemUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DatabaseUpdaterTest extends DatabaseUpdater{

    private String copiedExampleDbDir;
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUpdaterTest.class);

    @BeforeAll
    void copyExamples(){
        var copyFrom = StaticInformation.OLDER_VERSION_EXAMPLE_DB_DIR;
        var copyTo = StaticInformation.TEST_TEMP_DIR + File.separator + "dbUpgrade";
        copiedExampleDbDir = copyTo;
        try {
            FileSystemUtil.copyRecursive(new File(copyFrom), new File(copyTo));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUpdateTo0_0_6() {
//        logger.info(()->"testing upgrade db to 0.0.6");
        DatabaseUpdaterTest.setDbPath(Path.of(copiedExampleDbDir, "0.0.5", "records.db").toString());
        assertDoesNotThrow(DatabaseUpdaterTest::updateTo0_0_6);
//        logger.info(()->"upgrade db to 0.0.6 succeeded");
    }
}