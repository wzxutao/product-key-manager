package cn.rypacker.productkeymanager.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyGeneratorImplTest {

    KeyGenerator keyGenerator = new KeyGeneratorImpl();

    @Test
    void keyLength(){
        assertThrows(IllegalArgumentException.class,
                ()->keyGenerator.setKeyLength(7));


        var length = 8;
        assertDoesNotThrow(()->keyGenerator.setKeyLength(8));
        assertEquals(keyGenerator.getKeyLength(), length);

        var key = keyGenerator.generateKey();
        assertEquals(key.length(), length);
        key = keyGenerator.generateKey("200101");
        assertEquals(key.length(), length);
        assertThrows(IllegalArgumentException.class,
                ()->keyGenerator.generateKey("20200101"));

        length = 15;
        keyGenerator.setKeyLength(length);
        assertEquals(keyGenerator.getKeyLength(), length);
        key = keyGenerator.generateKey("200101");
        assertEquals(key.length(), length);
        assertThrows(IllegalArgumentException.class,
                ()->keyGenerator.generateKey("20200101"));

    }

}