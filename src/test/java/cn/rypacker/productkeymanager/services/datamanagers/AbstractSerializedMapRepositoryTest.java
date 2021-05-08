package cn.rypacker.productkeymanager.services.datamanagers;

import cn.rypacker.productkeymanager.config.StaticInformation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AbstractSerializedMapRepositoryTest<K, V>
        extends AbstractSerializedMapRepository<K, V>{

    File file = new File(getFilePath());
    AbstractSerializedMapRepositoryTest<String, String> t;

    @BeforeAll
    void beforeAll() {
        t = new AbstractSerializedMapRepositoryTest<>();
    }

    @Override
    protected String getFilePath() {
        return StaticInformation.TEST_TEMP_DIR + "/abstractMapRepo";
    }

    @BeforeEach
    void deleteFile(){
        if(file.exists()){
            assertTrue(file.delete());
        }
    }

    @Test
    void getFromNoneExistingFile(){
        var k = "key1";
        assertNull(t.get(k));
    }

    @Test
    void put_then_get(){
        var k = "key1";
        var v = "val1";
        assertDoesNotThrow(() -> t.put(k, v));
        assertEquals(t.get(k), v);
    }

    @Test
    void replace(){
        var k = "key1";
        var v = "val1";
        assertDoesNotThrow(() -> t.put(k, "val1"));
        assertEquals(t.get(k), v);
        v = "val2";
        assertDoesNotThrow(() -> t.put(k, "val2"));
        assertEquals(t.get(k), v);
    }

}