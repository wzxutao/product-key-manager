package cn.rypacker.productkeymanager.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

import static cn.rypacker.productkeymanager.common.UserRole.ROLE_ADMIN;
import static cn.rypacker.productkeymanager.common.UserRole.ROLE_NORMAL;


public class CustomProviderManager extends ProviderManager {

    public CustomProviderManager(AdminAuth adminAuth) {
        super(List.of(
                new TokenAuthenticationProvider(adminAuth),
                new UsernamePasswordAuthenticationProvider(adminAuth)
                ));
    }
}
