package cn.rypacker.productkeymanager.common;

public class RecordStatus {
    public static final int NORMAL = 0;
    public static final int MARKED_DELETE = 1;

    public static String toString(int statusCode){
        switch (statusCode){
            case NORMAL:
                return "正常";
            case MARKED_DELETE:
                return "删除";
            default:
                return Integer.toString(statusCode);
        }
    }
}
