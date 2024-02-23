package cn.rypacker.productkeymanager.common;


import cn.rypacker.productkeymanager.services.AdminAccountManager;

public class Constants {

    public static final String COOKIE_KEY_ADMIN_AUTH = "auth";

    public static final String COOKIE_KEY_NORMAL_AUTH = "normalAuth";

    public static final String COOKIE_KEY_USERNAME = "username";

    public static final String COOKIE_KEY_AUTH_EXPIRATION = "authExpiration";

    public static final String RECORD_KEY_USERNAME = "__username";
    public static final String RECORD_KEY_COMMENT = "__comment";
    public static final String RECORD_KEY_DATE = "日期";

    public static final String USERNAME_RECORD_VALUE_ADMIN;

    static {
        USERNAME_RECORD_VALUE_ADMIN = AdminAccountManager.getAdminUsername();
    }
}
