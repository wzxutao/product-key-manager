package cn.rypacker.productkeymanager.services.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static cn.rypacker.productkeymanager.common.UserRole.ROLE_ADMIN;
import static cn.rypacker.productkeymanager.common.UserRole.ROLE_NORMAL;

public interface AdminAuth {

    boolean isAdmin(String account, String password);
    boolean isAdmin(String account);

    String signNewToken(int expirationSecondsFromNow) throws Exception;
    boolean isValidToken(String token);

    static List<GrantedAuthority> getAuthorities() {
        return  List.of(
                new SimpleGrantedAuthority(ROLE_ADMIN),
                new SimpleGrantedAuthority(ROLE_NORMAL)
        );
    }
}
