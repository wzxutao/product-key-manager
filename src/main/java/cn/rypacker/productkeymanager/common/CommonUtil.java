package cn.rypacker.productkeymanager.common;

public class CommonUtil {

    public static Throwable findRootCause(Throwable e) {
        Throwable curr = e;
        while(curr.getCause() != null && curr.getCause() != curr){
            curr = curr.getCause();
        }
        return curr;
    }
}
