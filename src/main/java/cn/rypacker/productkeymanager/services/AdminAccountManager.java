package cn.rypacker.productkeymanager.services;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class AdminAccountManager {

    private static final String ACCOUNT_INFO_FILE = "data/admin";

    private static String hashPassword(String password){
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            return new String(messageDigest.digest());
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }

    public static boolean adminAccountExists(){
        return new File(ACCOUNT_INFO_FILE).exists();
    }

    public static boolean isAdminAccount(String username, String password){
        try(var scanner = new Scanner(new FileInputStream(ACCOUNT_INFO_FILE))){
            var userExpected = scanner.nextLine();
            var passwordExpected = scanner.nextLine();
//            var hashExpected = scanner.nextLine();

//            System.out.println("ue: " + userExpected);
//            System.out.println("he: " + hashExpected);
//            var hp = hashPassword(password);
//            System.out.println("ah: " + hp);
//            System.out.println("equals: " + (hashExpected.equals(hp)));


//            return userExpected.equals(username) &&
//                    hashPassword(password).equals(hashExpected);
            return userExpected.equals(username) && password.equals(passwordExpected);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void createAdminAccount(String username, String password, boolean hashPassword){
        var accountFile = new File(ACCOUNT_INFO_FILE);
        FileSystemUtil.mkdirIfNotExists(accountFile.getParentFile().toString());

        try(var pw = new PrintWriter(new FileOutputStream(ACCOUNT_INFO_FILE))){
            pw.println(username);
            if(hashPassword){
                pw.println(hashPassword(password));
            }else {
                pw.println(password);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
