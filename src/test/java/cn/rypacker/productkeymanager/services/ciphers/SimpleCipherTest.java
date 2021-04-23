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
import java.util.Arrays;
import java.util.Random;

import static cn.rypacker.productkeymanager.services.ciphers.SimpleCipher.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleCipherTest {

    @Test
    void givenString_whenEncrypt_thenSuccess()
            throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException,
            BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException, InvalidKeySpecException {

        String input = "{id:3, name:'boris johnson', date: '2020/12/20 12:12:12'}";
        SecretKey key = generateKeyFromPassword("boris johnson");
        IvParameterSpec ivParameterSpec = generateIvSpec();
        String cipherText = encrypt(input, key, ivParameterSpec);
        String plainText = decrypt(cipherText, key, ivParameterSpec);
        assertEquals(input, plainText);
    }



    @Test
    void bytesHexConversion() {
        var byteArr = new byte[] {1, 2 ,3 ,4 ,5 ,6 ,7 ,8 ,9 ,10, 11, 12, 13, 14, 15, 16};
        var hex = bytesToHex(byteArr);
        assertEquals(hex.length(), 32);
        assertArrayEquals(byteArr, hexStringToByteArray(hex));

        Arrays.fill(byteArr, (byte) 0);
        hex = bytesToHex(byteArr);
        assertEquals(hex.length(), 32);
        assertArrayEquals(byteArr, hexStringToByteArray(hex));

        Arrays.fill(byteArr, (byte) 127);
        hex = bytesToHex(byteArr);
        assertEquals(hex.length(), 32);
        assertArrayEquals(byteArr, hexStringToByteArray(hex));

        Arrays.fill(byteArr, (byte) -128);
        hex = bytesToHex(byteArr);
        assertEquals(hex.length(), 32);
        assertArrayEquals(byteArr, hexStringToByteArray(hex));

        var random = new Random();
        for(int i=0; i<500; i++){
            random.nextBytes(byteArr);
            hex = bytesToHex(byteArr);
            assertEquals(hex.length(), 32);
            assertArrayEquals(byteArr, hexStringToByteArray(hex));
        }
    }

    @Test
    void insecureEncryptAndDecrypt() throws Exception {
        var cipher = new SimpleCipher();
        var plainText = "";
        var encrypted = cipher.insecureEncrypt(plainText);
        assertEquals(cipher.insecureDecrypt(encrypted) , plainText);

        plainText = "afasasdf? { asdf}dfasda f";
        encrypted = cipher.insecureEncrypt(plainText);
        assertEquals(cipher.insecureDecrypt(encrypted) , plainText);

        plainText = "          \nafa\tsa\\sd\"f? { ajiseyo, .#$%as*&df}dfasda f";
        encrypted = cipher.insecureEncrypt(plainText);
        assertEquals(cipher.insecureDecrypt(encrypted) , plainText);

        plainText = "而设撒打发时间的法律是否撒地方卢卡斯 劳动法阿萨阿斯顿";
        encrypted = cipher.insecureEncrypt(plainText);
        assertEquals(cipher.insecureDecrypt(encrypted) , plainText);

    }


}