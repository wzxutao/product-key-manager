package cn.rypacker.productkeymanager.services;

import cn.rypacker.productkeymanager.config.StaticInformation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class FileSystemUtil {

    private static final Map<String, Long> fileLastModifiedMap = new HashMap<>();

    /**
     * execute the runnable only if the file has been modified since last call of this method
     * or if the method has never been called on the file.
     * @param filePath
     * @param thenExecute
     */
    public static void ifFileModified(String filePath, Runnable thenExecute) throws FileNotFoundException {
        var file = new File(filePath);
        if(!file.exists()){
            throw new FileNotFoundException();
        }
        var lastModifiedPrev = fileLastModifiedMap.get(filePath);
        var lastModifiedCurr = file.lastModified();
        if(lastModifiedPrev == null || lastModifiedPrev != lastModifiedCurr){
            fileLastModifiedMap.put(filePath, lastModifiedCurr);
            thenExecute.run();
        }
    }

    public static void mkEnclosingDirsIfNotExist(String filePath){
        var f = Path.of(filePath).getParent().toFile();
        mkdirsIfNotExist(f.toString());
    }

    public static void mkdirsIfNotExist(String dirPath){
        var dir = new File(dirPath);
//        if(!dir.isDirectory()){
//            dir = dir.getParentFile();
//        }
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
