package cn.rypacker.productkeymanager.services.auth;

public interface AdminAuth {

    boolean isAdmin(String account, String password);

    String signNewToken(int expirationSecondsFromNow) throws Exception;
    boolean isValidToken(String token);
}
