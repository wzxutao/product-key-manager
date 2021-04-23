package cn.rypacker.productkeymanager.services.ciphers;

import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static cn.rypacker.productkeymanager.services.ciphers.TotalCipher.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


class TotalCipherTest {

    @Test
    void givenString_whenEncrypt_thenSuccess()
            throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException,
            BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException, InvalidKeySpecException {

        String input = "{id:3, name:'boris johnson', date: '2020/12/20 12:12:12'}";
        SecretKey key = generateKeyFromPassword("boris johnson");
        IvParameterSpec ivParameterSpec = generateIv();
        String cipherText = encrypt(input, key, ivParameterSpec);
        String plainText = decrypt(cipherText, key, ivParameterSpec);
        assertEquals(input, plainText);
    }

}