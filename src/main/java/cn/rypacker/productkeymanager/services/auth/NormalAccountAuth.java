package cn.rypacker.productkeymanager.services.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static cn.rypacker.productkeymanager.common.UserRole.ROLE_ADMIN;
import static cn.rypacker.productkeymanager.common.UserRole.ROLE_NORMAL;

public interface NormalAccountAuth {

    String signNewToken(String username) throws Exception;
    boolean isTokenValid(String cipherToken);
    String getUsername(String cipherToken);

    boolean isNormalAccount(String username, String password);

    static List<GrantedAuthority> getAuthorities() {
        return  List.of(
                new SimpleGrantedAuthority(ROLE_NORMAL)
        );
    }
}
