package cn.rypacker.productkeymanager.services;

import java.io.File;

public class FileSystemUtil {

    public static void mkdirIfNotExists(String path){
        var dir = new File(path);
        if(!dir.exists()){
            dir.mkdirs();
        }
    }
}
