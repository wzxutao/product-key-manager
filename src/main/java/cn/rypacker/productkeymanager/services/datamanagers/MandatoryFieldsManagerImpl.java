package cn.rypacker.productkeymanager.services.datamanagers;

import cn.rypacker.productkeymanager.config.StaticInformation;
import cn.rypacker.productkeymanager.services.FileSystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

@Service
public class MandatoryFieldsManagerImpl implements MandatoryFieldsManager {

    private final Logger logger = LoggerFactory.getLogger(MandatoryFieldsManagerImpl.class);

    // retain insertion order
    private final Set<String> fieldNamesCache = new LinkedHashSet<>();


    // read from file into cache
    private synchronized void readFromFile() throws FileNotFoundException {
        fieldNamesCache.clear();
        try(var scanner = new Scanner(
                new BufferedInputStream(
                new FileInputStream(StaticInformation.MANDATORY_FIELDS_FILE_PATH)))){
            while(scanner.hasNextLine()){
                fieldNamesCache.add(scanner.nextLine());
            }
        }
    }

    // write from cache to file
    private synchronized void writeToFile(){
        FileSystemUtil.mkEnclosingDirsIfNotExist(StaticInformation.MANDATORY_FIELDS_FILE_PATH);

        try(var pw = new PrintWriter(
                new BufferedOutputStream(
                new FileOutputStream(StaticInformation.MANDATORY_FIELDS_FILE_PATH)))){
            for (var e :
                    fieldNamesCache) {
                pw.println(e);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized Set<String> getFieldNames() {
        try{
            FileSystemUtil.ifFileModified(StaticInformation.MANDATORY_FIELDS_FILE_PATH,
                    ()->{
                        try {
                            readFromFile();
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    });
            return fieldNamesCache;
        } catch (FileNotFoundException e) {
            logger.warn("mandatory fields file not found");
            fieldNamesCache.clear();
            return fieldNamesCache;
        }
    }

    @Override
    public synchronized void removeField(String fieldName) {
        fieldNamesCache.remove(fieldName);
        writeToFile();
    }

    @Override
    public synchronized void addField(String fieldName) {
        if(fieldName == null || fieldName.isBlank()) return;
        fieldNamesCache.add(fieldName);
        writeToFile();
    }

    @Override
    public synchronized void replaceWith(Collection<String> collection) {
        fieldNamesCache.clear();
        if(collection == null) return;

        collection.forEach(e -> {
            if(e != null && !e.isBlank()){
                fieldNamesCache.add(e);
            }
        });
        writeToFile();
    }

    @Override
    public synchronized void clear() {
        fieldNamesCache.clear();
        writeToFile();

    }
}
