package cn.rypacker.productkeymanager.services;

import org.json.JSONObject;

import java.util.Map;

public class JSONUtil {

    /**
     * generates json string from map
     * @param map
     * @return
     */
    public static String toStringFrom(Map<String, String> map){
        var json = new JSONObject();
        map.forEach(json::append);
        return json.toString();
    }
}
