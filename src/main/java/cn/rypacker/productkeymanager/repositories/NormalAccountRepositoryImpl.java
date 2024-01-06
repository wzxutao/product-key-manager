package cn.rypacker.productkeymanager.repositories;

import cn.rypacker.productkeymanager.services.configstore.UserConfig;
import cn.rypacker.productkeymanager.services.configstore.UserConfigStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class NormalAccountRepositoryImpl
        implements NormalAccountRepository {

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private UserConfigStore userConfigStore;


    @Override
    public void add(String username, String password) {
        update(username, password);
    }

    @Override
    public void update(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        var hash = encoder.encode(password);
        userConfigStore.update(c -> c.setAccounts(
                Stream.concat(c.getAccounts()
                                .stream()
                                .filter(account -> !account.getUsername().equals(username))
                        , Stream.of(new UserConfig.Account(username, hash))
                ).collect(Collectors.toList())));
    }

    @Override
    public void remove(String username) {
        userConfigStore.update(c -> c.setAccounts(
                c.getAccounts()
                        .stream()
                        .filter(account -> !account.getUsername().equals(username))
                        .collect(Collectors.toList()))
        );
    }

    @Override
    public boolean exists(String username) {
        return get(Objects.requireNonNull(username)) != null;
    }

    @Override
    public boolean matches(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        var hash = get(username);
        if (hash == null) return false;

        return encoder.matches(password, hash);
    }

    private String get(String username) {
        return userConfigStore.getData().getAccounts()
                .stream()
                .filter(account -> account.getUsername().equals(username))
                .findFirst()
                .map(UserConfig.Account::getHash)
                .orElse(null);
    }

    @Override
    public Set<String> findAllExistingUserNames() {
        return userConfigStore.getData().getAccounts()
                .stream()
                .map(UserConfig.Account::getUsername)
                .collect(Collectors.toSet());
    }

}
