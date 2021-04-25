package cn.rypacker.productkeymanager.services;

public interface AdminAuth {

    boolean isAdmin(String account, String password);

    String signNewToken(int expirationSecondsFromNow) throws Exception;
    boolean isValidToken(String token);
}
