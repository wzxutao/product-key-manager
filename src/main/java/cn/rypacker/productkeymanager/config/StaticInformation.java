package cn.rypacker.productkeymanager.config;

public class StaticInformation {
    /**
     * version number of this application
     */
    // todo change before release
    public static final String VERSION_NUMBER = "0.0.15";

    public static final String EMPTY_DB_PATH = "resources/db/records_empty.db";
    public static final String USER_DB_DIR = "data";
    public static final String USER_DB_PATH = USER_DB_DIR + "/records.db";
    public static final String DB_PENDING_RESTORE_PATH = USER_DB_DIR + "/records_restore.db";
    public static final String USER_DB_BACKUP_DIR = USER_DB_DIR+ "/bak";
    public static final String MANDATORY_FIELDS_FILE_PATH = USER_DB_DIR + "/mandatoryFields";
    public static final String METADATA_FILE_PATH = USER_DB_DIR + "/metadata";
    public static final String ACCOUNTS_FILE_PATH = USER_DB_DIR + "/accounts";

    public static final String USER_DOC_DIR =
            System.getProperty("user.home") != null ?
                    System.getProperty("user.home") + "/Documents/product-key-manager" :
                    USER_DB_DIR + "/home";
    public static final String TEMP_DIR = USER_DOC_DIR + "/temp";

    public static final String TEST_TEMP_DIR = "testResources";
    public static final String OLDER_VERSION_EXAMPLE_DB_DIR = "resources/db/olderVersionExamples";

    public static final String APPLICATION_TITLE = "序列号管理系统";

}
