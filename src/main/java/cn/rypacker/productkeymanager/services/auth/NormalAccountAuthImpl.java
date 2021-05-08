package cn.rypacker.productkeymanager.services.auth;

import cn.rypacker.productkeymanager.services.DatetimeUtil;
import cn.rypacker.productkeymanager.services.JSONUtil;
import cn.rypacker.productkeymanager.services.ciphers.JokeCipher;
import cn.rypacker.productkeymanager.services.datamanagers.PropertyManager;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NormalAccountAuthImpl implements NormalAccountAuth {

    @Autowired
    JokeCipher jokeCipher;
    @Autowired
    PropertyManager propertyManager;

    private static final String KEY_CREATED = "created";


    @Override
    public String signNewToken() throws Exception {
        var jsonObject = new JSONObject();
        jsonObject.put(KEY_CREATED, System.currentTimeMillis());
        return jokeCipher.insecureEncrypt(jsonObject.toString());
    }

    @Override
    public boolean isTokenValid(String cipherToken) {
        try{
            var token = jokeCipher.insecureDecrypt(cipherToken);
            var created = Long.parseLong(JSONUtil.getValue(token, KEY_CREATED));
            var validDaysStr = propertyManager.getOrDefault(
                    PropertyManager.Properties.NORMAL_AUTH_VALID_DAYS,
                    "30");
            int validDays;
            try{
                validDays = Integer.parseInt(validDaysStr);
            }catch (NullPointerException e){
                validDays = 30;
            }

            var validUntil = DatetimeUtil.roundToMidNight(
                    created + (long) validDays * 24 * 60 * 60 * 1000);

            return validUntil > System.currentTimeMillis();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
