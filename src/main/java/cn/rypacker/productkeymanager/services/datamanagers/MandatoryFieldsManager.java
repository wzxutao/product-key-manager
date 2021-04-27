package cn.rypacker.productkeymanager.services.datamanagers;

import java.util.Collection;
import java.util.Set;

public interface MandatoryFieldsManager {

    Set<String> getFieldNames();

    void removeField(String fieldName);

    void addField(String fieldName);
    void replaceWith(Collection<String> collection);

    void clear();


}
