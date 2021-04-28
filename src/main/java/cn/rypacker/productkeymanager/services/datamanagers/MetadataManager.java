package cn.rypacker.productkeymanager.services.datamanagers;

public interface MetadataManager {
    public static class MetaData{
        public static final String DB_VERSION = "database.version";
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
}
