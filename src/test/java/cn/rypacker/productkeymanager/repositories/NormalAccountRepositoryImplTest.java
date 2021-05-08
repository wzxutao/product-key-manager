package cn.rypacker.productkeymanager.repositories;

import cn.rypacker.productkeymanager.config.StaticInformation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NormalAccountRepositoryImplTest extends NormalAccountRepositoryImpl{
    NormalAccountRepositoryImpl r;

    private static final String path = StaticInformation.TEST_TEMP_DIR + "/accounts";

    static {
        var f = new File(path);
        if(f.exists()){
            assertTrue(f.delete());
        }
    }

    @Override
    protected String getFilePath() {
        return path;
    }


    @BeforeAll
    void beforeAll() {
        r = new NormalAccountRepositoryImplTest();
    }

    @Test
    void add_match_update(){
        var username = "user1";
        assertFalse(r.exists(username));
        var password = "password1";
        assertFalse(r.matches(username, password));

        r.add(username, password);
        assertTrue(r.matches(username, password));

        var user2 = "user2";
        var pass2 = "password2";
        assertFalse(r.exists(user2));
        assertFalse(r.matches(user2, pass2));

        r.add(user2, pass2);
        assertTrue(r.matches(user2, pass2));
        assertTrue(r.matches(username, password));
        assertFalse(r.matches(username, pass2));
        assertFalse(r.matches(user2, password));

        // update
        var newPass = "newPass";
        r.update(username, newPass);
        assertTrue(r.matches(username, newPass));
        assertFalse(r.matches(username, password));
    }

    @Test
    void remove(){
        var username = "user3";
        var password = "pass3";
        r.add(username, password);
        var user4 = "user4";
        var pass4 = "pass4";
        r.add(user4, pass4);
        assertTrue(r.exists(username));
        assertTrue(r.exists(user4));

        r.remove(username);
        assertFalse(r.exists(username));
        assertTrue(r.exists(user4));

    }
}