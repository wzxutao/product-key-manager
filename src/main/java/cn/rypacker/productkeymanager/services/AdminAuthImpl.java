package cn.rypacker.productkeymanager.services;

import cn.rypacker.productkeymanager.services.ciphers.JokeCipher;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthImpl implements AdminAuth {
    @Autowired
    JokeCipher jokeCipher;

    private static final String KEY_EXPIRATION = "expiration";


    //todo
    @Override
    public boolean isAdmin(String account, String password) {
//        System.out.println("a: " + account + ", p: " + password);
        if(account == null || password == null) return false;
        return account.equals("admin") && password.equals("admin");
    }

    @Override
    public String signNewToken(int expirationSecondsFromNow) throws Exception {
        var jsonObject = new JSONObject();
        var expirationMilli = System.currentTimeMillis() + expirationSecondsFromNow * 1000L;
        jsonObject.put(KEY_EXPIRATION, expirationMilli);
        return jokeCipher.insecureEncrypt(jsonObject.toString());
    }

    @Override
    public boolean isValidToken(String cipherToken) {
        try{
            var token = jokeCipher.insecureDecrypt(cipherToken);
            var expirationMilli = JSONUtil.getValue(token, KEY_EXPIRATION);
            if(expirationMilli == null) return false;
            return System.currentTimeMillis() < Long.parseLong(expirationMilli);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}
