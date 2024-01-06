package cn.rypacker.productkeymanager.services;

import cn.rypacker.productkeymanager.config.StaticInformation;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

    public static void ifFileModifiedUnchecked(String filePath, Runnable thenExecute){
        try{
            ifFileModified(filePath, thenExecute);
        } catch (FileNotFoundException e) {
            throw new UncheckedIOException(e);
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

    public static void copyRecursive(File source, File destination) throws IOException {
        Objects.requireNonNull(source);
        Objects.requireNonNull(destination);

        if(source.isDirectory()){
            destination.mkdirs();
            for(var child: source.list()){
                copyRecursive(Path.of(source.toString(), child).toFile(),
                        Path.of(destination.toString(), child).toFile());
            }
        }else{
            Files.copy(source.toPath(), destination.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static void download(String href, Path saveTo) throws IOException {
        try(var in = new BufferedInputStream(new URL(href).openStream())){
            FileSystemUtil.mkEnclosingDirsIfNotExist(saveTo.toString());
            Files.copy(in, saveTo,
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    public static void unzip(Path zipPath, Path destDir) throws IOException {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath.toFile()));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDir.toFile(), zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
    }


    public static void copyFolder(Path src, Path dest) throws IOException {
        try (Stream<Path> stream = Files.walk(src)) {
            stream.forEach(source -> copyFile(source, dest.resolve(src.relativize(source))));
        }
    }

    private static void copyFile(Path source, Path dest) {
        try {
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void moveFileIfExists(String oldPath, String newPath){
        var oldFile = new File(oldPath);
        if(oldFile.exists()){
            var newFile = new File(newPath);
            if(newFile.exists()){
                newFile.delete();
            }
            oldFile.renameTo(newFile);
        }
    }
    
}
