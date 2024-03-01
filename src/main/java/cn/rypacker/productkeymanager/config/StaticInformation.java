package cn.rypacker.productkeymanager.config;

public class StaticInformation {
    /**
     * version number of this application
     */
    // todo change before release
    public static final String VERSION_NUMBER = "1.0.1";

    public static final String EMPTY_DB_PATH = "resources/db/records_empty.db";
    public static final String USER_DB_DIR = "data";
    public static final String DB_PENDING_RESTORE_PATH = USER_DB_DIR + "/records_restore.db";
    public static final String USER_DB_BACKUP_DIR = USER_DB_DIR + "/bak";

    public static final String LEGACY_FILE_DIR = USER_DB_DIR + "/legacy";

    public static final String BACKGROUND_IMAGE_PATH = USER_DB_DIR + "/background.jpg";

    public static final String METADATA_FILE_PATH_V1 = USER_DB_DIR + "/metadata_v1.json";

    public static final String USER_DOC_DIR =
            System.getProperty("user.home") != null ?
                    System.getProperty("user.home") + "/Documents/product-key-manager" :
                    USER_DB_DIR + "/home";
    public static final String TEMP_DIR = USER_DOC_DIR + "/temp";

    public static final String TEST_TEMP_DIR = "testResources";
    public static final String OLDER_VERSION_EXAMPLE_DB_DIR = "resources/db/olderVersionExamples";

}
