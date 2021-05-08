package cn.rypacker.productkeymanager.services.auth;

public interface NormalAccountAuth {

    String signNewToken() throws Exception;
    boolean isTokenValid(String cipherToken);
}
