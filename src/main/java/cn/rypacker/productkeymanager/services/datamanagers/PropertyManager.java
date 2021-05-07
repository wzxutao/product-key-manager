package cn.rypacker.productkeymanager.services.datamanagers;

public interface PropertyManager {
    class Properties {
        public static final String KEY_LENGTH = "key.length";
    }

    /**
     * saves a metadata, replace existing
     * @param key
     * @param value
     */
    void put(String key, String value);

    /**
     * returns the value of this metadata, null if not defined
     * @param key
     * @return
     */
    String get(String key);

    /**
     * returns value corresponding to the key if exists, defaultVal otherwise
     * @param key
     * @param defaultVal
     * @return
     */
    default String getOrDefault(String key, String defaultVal){
        var val = get(key);
        return val != null ? val : defaultVal;
    }
}
