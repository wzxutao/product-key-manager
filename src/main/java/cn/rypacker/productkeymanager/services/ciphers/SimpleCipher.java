package cn.rypacker.productkeymanager.services.ciphers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
public class SimpleCipher implements JokeCipher {

    private static final Logger logger = LoggerFactory.getLogger(SimpleCipher.class);
    private static final String DEFAULT_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String DEFAULT_PASSWORD = "password!";

    public static SecretKey generateKey(int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(keySize);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    public static SecretKey generateKeyFromPassword(String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return generateKeyFromPassword(password, password + "Salt");
    }

    public static SecretKey generateKeyFromPassword(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), "AES");
        return secret;
    }

    public static IvParameterSpec generateIvSpec() {
        return new IvParameterSpec(generateIv());
    }

    public static byte[] generateIv(){
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return iv;
    }



    private static SecretKey getSecretKeyFromSystemProperty() throws InvalidKeySpecException, NoSuchAlgorithmException {
        var sk = System.getProperty("secretKey");
        if(sk == null || sk.isEmpty()){
            logger.error("secretKey system property not set");
            return null;
        }
        return generateKeyFromPassword(sk);
    }

    public static String bytesToHex(byte[] bytes) {
        final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }

    /* s must be an even-length string. */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }


    public static String encrypt(String input, IvParameterSpec iv) throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        return encrypt(input, getSecretKeyFromSystemProperty(), iv);
    }

    public static String encrypt(String input, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        return encrypt(DEFAULT_ALGORITHM, input, key, iv);
    }

    public static String encrypt(String algorithm, String input, SecretKey key,
                                 IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    public static String decrypt(String input, IvParameterSpec iv) throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        return decrypt(input, getSecretKeyFromSystemProperty(), iv);
    }

    public static String decrypt(String cipherText, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        return decrypt(DEFAULT_ALGORITHM, cipherText, key, iv);
    }

    public static String decrypt(String algorithm, String cipherText, SecretKey key,
                                 IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(cipherText));
        return new String(plainText);
    }


    @Override
    public String insecureEncrypt(String src) throws Exception {
        var iv = generateIv();
        var cipherText = encrypt(src, generateKeyFromPassword(DEFAULT_PASSWORD), new IvParameterSpec(iv));
        return bytesToHex(iv) + cipherText;
    }

    @Override
    public String insecureDecrypt(String ivAndCipherText) throws Exception {
        if(ivAndCipherText == null || ivAndCipherText.length() <= 32){
            throw new IllegalArgumentException();
        }

        var iv = hexStringToByteArray(ivAndCipherText.substring(0, 32));
        var cipherText = ivAndCipherText.substring(32);
        return decrypt(cipherText, generateKeyFromPassword(DEFAULT_PASSWORD), new IvParameterSpec(iv));
    }
}
