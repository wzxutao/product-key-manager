package cn.rypacker.productkeymanager.repositories;

import cn.rypacker.productkeymanager.config.StaticInformation;
import cn.rypacker.productkeymanager.services.datamanagers.AbstractSerializedMapRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Set;

@Repository
public class NormalAccountRepositoryImpl
        extends AbstractSerializedMapRepository<String, String>
        implements NormalAccountRepository {

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void add(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        var hash = encoder.encode(password);
        put(username, hash);
    }

    @Override
    public void update(String username, String password) {
        add(username, password);
    }

    @Override
    public void remove(String username) {
        super.remove(username);
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
        if(hash == null) return false;

        return encoder.matches(password, hash);
    }

    @Override
    public Set<String> findAllExistingUserNames() {
        return getKeySet();
    }

    @Override
    protected String getFilePath() {
        return StaticInformation.ACCOUNTS_FILE_PATH;
    }
}
