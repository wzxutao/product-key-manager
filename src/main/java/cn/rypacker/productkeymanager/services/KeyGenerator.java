package cn.rypacker.productkeymanager.services;

public interface KeyGenerator {

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
     */
    String generateKey(String dateString);
}
