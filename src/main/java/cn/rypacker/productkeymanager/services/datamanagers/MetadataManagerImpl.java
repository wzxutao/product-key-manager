package cn.rypacker.productkeymanager.services.datamanagers;

import cn.rypacker.productkeymanager.config.StaticInformation;
import cn.rypacker.productkeymanager.services.FileSystemUtil;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MetadataManagerImpl implements MetadataManager {

    Map<String, String> metadataCache = new ConcurrentHashMap<>();

    public MetadataManagerImpl() {
        readFromFile();
    }

    private synchronized void readFromFile(){
        var path = StaticInformation.METADATA_FILE_PATH;
        var f = new File(path);

        if(!f.exists()){
            return;
        }

        try(var oi = new ObjectInputStream(
                new BufferedInputStream((
                        new FileInputStream(f))))
        ){
            metadataCache = (Map<String, String>) oi.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private synchronized void writeToFile(){
        var path = StaticInformation.METADATA_FILE_PATH;
        FileSystemUtil.mkEnclosingDirsIfNotExist(path);

        try(var oo = new ObjectOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(path)
                )
        )){
            oo.writeObject(metadataCache);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void put(String key, String value) {
        metadataCache.put(key, value);
        writeToFile();
    }

    @Override
    public String get(String key) {
        FileSystemUtil.ifFileModifiedUnchecked(StaticInformation.MANDATORY_FIELDS_FILE_PATH,
                this::readFromFile);
        return metadataCache.get(key);

    }
}
