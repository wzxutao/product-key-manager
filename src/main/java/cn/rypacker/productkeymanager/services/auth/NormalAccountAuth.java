package cn.rypacker.productkeymanager.services.auth;

public interface NormalAccountAuth {

    String signNewToken(String username) throws Exception;
    boolean isTokenValid(String cipherToken);
    String getUsername(String cipherToken);
}
