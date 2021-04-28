package cn.rypacker.productkeymanager.services;

import cn.rypacker.productkeymanager.config.StaticInformation;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileSystemUtilTest {

    private static void makeFile(String path, String contents) {
        FileSystemUtil.mkEnclosingDirsIfNotExist(path);
        try(var oo = new ObjectOutputStream(
                new FileOutputStream(path))
        ){
            oo.writeObject(contents);
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    private static <T> T readFile(String path, Class<T> returnType){
        try(var oi = new ObjectInputStream(
                new FileInputStream(path))
        ){
            return (T) oi.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void copyRecursive() {
        var basePath = StaticInformation.TEST_TEMP_DIR + File.separator + "copyFrom";
        FileSystemUtil.mkdirsIfNotExist(basePath);

        // some text files
        Map<String, String> fileContents = new HashMap<>();
        fileContents.put(File.separator + "text1", "some nonsense");
        fileContents.put(File.separator + "briefcase" + File.separator + "text2", "another nonsense");
        fileContents.put(File.separator + "briefcase2" + File.separator + "text3", "what the hell");
        fileContents.put(File.separator + "briefcase2" + File.separator + "abracadabra" + File.separator + "secret"
                        + File.separator + "password" + File.separator + "password",
                "password:\nvery secure password\ndo you understand?\n"
                );
        fileContents.forEach((k, v) -> {
            makeFile(basePath + k, v);
        });

        // copy
        var copyTo = StaticInformation.TEST_TEMP_DIR + File.separator + "copyTo";
        assertDoesNotThrow(() -> FileSystemUtil.copyRecursive(new File(basePath), new File(copyTo)));
        // check copy
        fileContents.forEach((k, v)->{
            assertEquals(readFile(copyTo + k, String.class), v);
        });

    }
}