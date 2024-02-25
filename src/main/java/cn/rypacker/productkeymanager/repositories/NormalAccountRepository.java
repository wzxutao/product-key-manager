package cn.rypacker.productkeymanager.repositories;


import java.util.Set;

public interface NormalAccountRepository {

    void upsert(String username, String password);
    void remove(String username);
    boolean exists(String username);
    boolean matches(String username, String password);

    Set<String> findAllExistingUserNames();

}
