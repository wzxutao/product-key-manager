package cn.rypacker.productkeymanager.services.datamanagers;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MetadataManagerImplTest {

    MetadataManager metadataManager = new MetadataManagerImpl();

    @Test
    void write_read(){
        // read
        var random = new Random();
        List<String> randomKeys = new ArrayList<>();
        for(int i=0; i<100; i++){
            randomKeys.add(Integer.toString(random.nextInt()));
        }
        randomKeys.add("boris johnson");
        randomKeys.add("尼玛");
        randomKeys.add("ある");
        for (var randomKey: randomKeys
        ) {
            assertDoesNotThrow(()->metadataManager.get(randomKey));
        }

        // write then read
        var map = new HashMap<String, String>();
        map.put("test_key_1", "test_value_1");
        map.put("test_key_2", "test_value_2");
        map.put("test_key_3", "test_value_3");
        map.put("test_key_4", "test_value_4");
        map.put("test_key_5", "test_value_5");

        map.forEach((k, v) -> {
            metadataManager.put(k, v);
        });

        map.forEach((k, v) -> {
            assertEquals(metadataManager.get(k), v);
        });

        // update then read
        var key = "test_key_2";
        var val = "test_value_2_updated";
        var valPrev = metadataManager.get(key);
        metadataManager.put(key, val);
        assertEquals(metadataManager.get(key), val);
        assertNotEquals(metadataManager.get(key), valPrev);




    }

}