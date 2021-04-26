package cn.rypacker.productkeymanager.config;

public class StaticInformation {
    /**
     * version number of this application
     */
    public static final String VERSION_NUMBER = "0.0.4";

    public static final String EMPTY_DB_PATH = "resources/db/records_empty.db";
    public static final String USER_DB_DIR = "data";
    public static final String USER_DB_PATH = USER_DB_DIR + "/records.db";
    public static final String USER_DB_BACKUP_DIR = USER_DB_DIR+ "/bak";
    public static final String MANDATORY_FIELDS_FILE_PATH = USER_DB_DIR + "/mandatoryFields";

    public static final String APPLICATION_TITLE = "序列号管理系统";

}
