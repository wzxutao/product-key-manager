package cn.rypacker.productkeymanager.services;

import java.io.File;
import java.io.IOException;

public class FileSystemUtil {

    public static void mkdirIfNotExists(String path){
        var dir = new File(path);
        if(!dir.exists()){
            dir.mkdirs();
        }
    }

    public static boolean isValidFilePath(String path) {
        File f = new File(path);
        try {
            f.getCanonicalPath();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }
}
