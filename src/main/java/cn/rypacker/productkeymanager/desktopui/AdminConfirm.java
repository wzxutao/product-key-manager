package cn.rypacker.productkeymanager.desktopui;

import cn.rypacker.productkeymanager.services.auth.AdminAuth;
import cn.rypacker.productkeymanager.services.configstore.UserConfigStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminConfirm {

    @Autowired
    AdminAuth adminAuth;
    @Autowired
    UserConfigStore userConfigStore;

    public int getTokenValidMinutes(){
        try{
            return userConfigStore.getData().getAuth().getAdmin().getValidMinutes();
        }catch (NumberFormatException e){
            return 30;
        }

    }


    /**
     * shows a dialog in the server and ask for confirmation.
     *
     * @return an auth token. null if user rejected the request or errors occurred
     */
    public String getTokenIfApproved(String clientIp){
//        var userOption = JOptionPane.showConfirmDialog(null,
//                String.format("%s 请求进入序列号管理系统管理界面，批准？", clientIp),
//                StaticInformation.APPLICATION_TITLE,
//                JOptionPane.YES_NO_OPTION);
//        if(userOption != JOptionPane.YES_OPTION) return null;

        try{
            return adminAuth.signNewToken(getTokenValidMinutes() * 60);
        }catch (Exception e){
            return null;
        }
    }

}
