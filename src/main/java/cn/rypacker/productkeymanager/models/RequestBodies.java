package cn.rypacker.productkeymanager.models;

import java.util.List;

public class RequestBodies {
    public static class Key{
        public String key;
    }

    public static class LoginForm {
        public String account;
        public String password;
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
