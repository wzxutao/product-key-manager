package cn.rypacker.productkeymanager.services.datamanagers;

import cn.rypacker.productkeymanager.services.FileSystemUtil;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractSerializedMapRepository<K, V> {

    Map<K, V> cache = new ConcurrentHashMap<>();

    /**
     *
     * @return where this map should be stored
     */
    protected abstract String getFilePath();

    public AbstractSerializedMapRepository() {
        readFromFile();
    }

    public void put(K key, V value){
        cache.put(key, value);
        writeToFile();
    }

    public V get(K key) {
        try{
            FileSystemUtil.ifFileModified(getFilePath(),
                    this::readFromFile);
        }catch (FileNotFoundException e){
            return null;
        }
        return cache.get(key);
    }

    public void remove(K key){
        cache.remove(key);
        writeToFile();
    }

    private synchronized void readFromFile(){
        var path = getFilePath();
        var f = new File(path);

        if(!f.exists()){
            return;
        }

        try(var oi = new ObjectInputStream(
                new BufferedInputStream((
                        new FileInputStream(f))))
        ){
            cache = (Map<K, V>) oi.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private synchronized void writeToFile(){
        var path = getFilePath();
        FileSystemUtil.mkEnclosingDirsIfNotExist(path);

        try(var oo = new ObjectOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(path)
                )
        )){
            oo.writeObject(cache);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
