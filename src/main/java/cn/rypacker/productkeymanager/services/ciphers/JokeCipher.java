package cn.rypacker.productkeymanager.services.ciphers;

public interface JokeCipher {
    String insecureEncrypt(String src) throws Exception;
    String insecureDecrypt(String cipherText) throws Exception;
}
