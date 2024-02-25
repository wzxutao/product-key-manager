package cn.rypacker.productkeymanager.services.configstore;

import cn.rypacker.productkeymanager.services.FileSystemUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Consumer;

@Slf4j
public abstract class AbstractJsonFileStore<T> {

    private static final ObjectMapper mapper = new ObjectMapper();

    private T data;
    private Class<T> dataClass;

    static {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    protected abstract String getFilePath();

    protected AbstractJsonFileStore(Class<T> clazz) {
        dataClass = clazz;
        readFromFile();
    }

    public synchronized void saveData(T data) {
        try {
            mapper.writeValue(new File(getFilePath()), data);
            this.data = data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized T getData() {
        try {
            FileSystemUtil.ifFileModified(getFilePath(), this::readFromFile);
        } catch (FileNotFoundException e) {
            throw new UncheckedIOException(e);
        }

        if (data == null) {
            throw new UncheckedIOException(new FileNotFoundException(getFilePath() + " not found."));
        }
        return data;
    }

    public synchronized void update(Consumer<T> consumer) {
        consumer.accept(getData());
        saveData(data);
    }

    protected synchronized void readFromFile() {
        try {
            data = mapper.readValue(new File(getFilePath()), dataClass);
        } catch (FileNotFoundException e) {
            log.warn(getFilePath() + " not found.");
            data = null;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
