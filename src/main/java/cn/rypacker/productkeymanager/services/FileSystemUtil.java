package cn.rypacker.productkeymanager.services;

import cn.rypacker.productkeymanager.config.StaticInformation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileSystemUtil {

    public static void mkdirIfNotExists(String path){
        var dir = new File(path);
//        System.out.println(path + " exists: " + dir.exists());
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

    /**
     * get all the filenames in the directory excluding subdirectories in it
     * @param dirPath
     * @return
     */
    public static List<String> getFileNamesInDir(String dirPath){
        File folder = new File(dirPath);
        if(!folder.exists()) return new ArrayList<>();
        File[] listOfFiles = Objects.requireNonNull(folder.listFiles());

        List<String> fileNames = new ArrayList<>();
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                fileNames.add(listOfFile.getName());
            }
        }
        return fileNames;
    }


    public static List<String> getBackupFileNames(){
        List<String> backups = new ArrayList<>();
        var backupDir = new File(StaticInformation.USER_DB_BACKUP_DIR);
        if(!backupDir.exists()) return backups;

        return getFileNamesInDir(backupDir.getPath());
    }
}
