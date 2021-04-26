package cn.rypacker.productkeymanager.services;

import java.util.Set;

public interface MandatoryFieldsManager {

    Set<String> getFieldNames();

    void removeField(String fieldName);

    void addField(String fieldName);

    void clear();


}
