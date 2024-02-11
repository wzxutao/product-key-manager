package cn.rypacker.productkeymanager.dto;

import java.util.List;

public class RequestBodies {
    public static class Key{
        public String key;
    }


    public static class MandatoryFieldNames {
        public List<String> mandatoryFieldNames;
    }

    public static class RecordsQuery {
        public String fromTime;
        public String toTime;
    }

    public static class OneList<T> {
        public List<T> list;
    }
}
