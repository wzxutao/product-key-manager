package cn.rypacker.productkeymanager.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

import static cn.rypacker.productkeymanager.common.UserRole.ROLE_ADMIN;
import static cn.rypacker.productkeymanager.common.UserRole.ROLE_NORMAL;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminAuth adminAuth;

    @Autowired
    private NormalAccountAuth normalAccountAuth;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.equals("admin"))
            return new User(username, "admin", List.of(
                    new SimpleGrantedAuthority(ROLE_ADMIN),
                    new SimpleGrantedAuthority(ROLE_NORMAL)));
        else
            return new User(username, "normal", List.of(new SimpleGrantedAuthority(ROLE_NORMAL)));
    }
}
