package cn.rypacker.productkeymanager.services.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

import static cn.rypacker.productkeymanager.common.UserRole.ROLE_ADMIN;
import static cn.rypacker.productkeymanager.common.UserRole.ROLE_NORMAL;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        if ("admin".equals(username) && "admin".equals(password)) {
            return new UsernamePasswordAuthenticationToken(username, password, List.of(
                    new SimpleGrantedAuthority(ROLE_ADMIN),
                    new SimpleGrantedAuthority(ROLE_NORMAL)
            ));
        } else {
            throw new BadCredentialsException("Invalid username/password");
        }
    }
}
