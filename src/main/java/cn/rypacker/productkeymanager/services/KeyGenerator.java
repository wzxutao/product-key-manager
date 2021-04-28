package cn.rypacker.productkeymanager.services;

public interface KeyGenerator {

    int getKeyLength();

    /**
     * @throws IllegalArgumentException if key length is too short
     */
    void setKeyLength(int length);

    /**
     * the datetime info will be the time this method is called
     * @return
     */
    String generateKey();

    /**
     * returns a key containing readable datetime info
     * @param epochMilli
     * @return
     */
    String generateKey(long epochMilli);

    /**
     * keeps the dateString intact
     * @param dateString
     * @return
     * @throws IllegalArgumentException if the dateString has a wrong length
     */
    String generateKey(String dateString);
}
