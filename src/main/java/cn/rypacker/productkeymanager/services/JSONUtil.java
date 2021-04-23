package cn.rypacker.productkeymanager.services;

import org.json.JSONException;
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

    /**
     *
     * @param jsonString
     * @param key
     * @return the value associated with the key in this json string. null if not found
     */
    public static String getValue(String jsonString, String key){
        var jsonObject = new JSONObject(jsonString);
        try{
            return jsonObject.get(key).toString();
        }catch (JSONException e){
            return null;
        }
    }
}
