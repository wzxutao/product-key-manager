package cn.rypacker.productkeymanager.services;

import cn.rypacker.productkeymanager.services.configstore.legacy.PropertyManagerImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyGeneratorImplTest {

    KeyGenerator keyGenerator = new KeyGeneratorImpl(new PropertyManagerImpl());

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

    @Test
    void nextSibling() {
        keyGenerator.setKeyLength(8);
        var key = "200101AA";
        assertEquals(keyGenerator.nextSibling(key), "200101AB");

        key = "200101AZ";
        assertEquals(keyGenerator.nextSibling(key), "200101BA");

        key = "200101ZZ";
        assertEquals(keyGenerator.nextSibling(key), "200101AA");

        key = "200101GZZ";
        assertEquals(keyGenerator.nextSibling(key), "200101HAA");

        key = "200101ZZZ";
        assertEquals(keyGenerator.nextSibling(key), "200101AAA");

    }
}