package cn.rypacker.productkeymanager.services.auth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final AdminAuth adminAuth;
    private final NormalAccountAuth normalAccountAuth;

    public UsernamePasswordAuthenticationProvider(AdminAuth adminAuth, NormalAccountAuth normalAccountAuth) {
        this.adminAuth = adminAuth;
        this.normalAccountAuth = normalAccountAuth;
    }



    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var usernamePasswordAuth = (UsernamePasswordAuthenticationToken) authentication;

        String username = (String) usernamePasswordAuth.getPrincipal();
        String password = (String) usernamePasswordAuth.getCredentials();

        if(adminAuth.isAdmin(username, password)) {
            return new UsernamePasswordAuthenticationToken(username, password, AdminAuth.getAuthorities());
        }

        if(normalAccountAuth.isNormalAccount(username, password)) {
            return new UsernamePasswordAuthenticationToken(username, password, NormalAccountAuth.getAuthorities());
        }

        throw new BadCredentialsException("invalid password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
