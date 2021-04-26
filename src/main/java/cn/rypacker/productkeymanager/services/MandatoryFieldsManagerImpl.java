package cn.rypacker.productkeymanager.services;

import cn.rypacker.productkeymanager.config.StaticInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

@Service
public class MandatoryFieldsManagerImpl implements MandatoryFieldsManager {

    private final Logger logger = LoggerFactory.getLogger(MandatoryFieldsManagerImpl.class);

    private final Set<String> fieldNamesCache = new HashSet<>();


    // read from file into cache
    private void readFromFile() throws FileNotFoundException {
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
    private void writeToFile(){
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
    public Set<String> getFieldNames() {
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
    public void removeField(String fieldName) {
        fieldNamesCache.remove(fieldName);
        writeToFile();
    }

    @Override
    public void addField(String fieldName) {
        if(fieldName == null || fieldName.isBlank()) return;
        fieldNamesCache.add(fieldName);
        writeToFile();
    }

    @Override
    public void clear() {
        fieldNamesCache.clear();
        writeToFile();

    }
}
