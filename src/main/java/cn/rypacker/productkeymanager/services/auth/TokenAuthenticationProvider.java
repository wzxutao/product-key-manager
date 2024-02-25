package cn.rypacker.productkeymanager.services.auth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class TokenAuthenticationProvider implements AuthenticationProvider {

    private final AdminAuth adminAuth;
    private final NormalAccountAuth normalAccountAuth;

    public TokenAuthenticationProvider(AdminAuth adminAuth, NormalAccountAuth normalAccountAuth) {
        this.adminAuth = adminAuth;
        this.normalAccountAuth = normalAccountAuth;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var tokenAuth = (CustomAuthenticationToken) authentication;

        if (adminAuth.isValidToken((String) authentication.getCredentials())) {
            return new CustomAuthenticationToken(tokenAuth.getCredentials(), AdminAuth.getAuthorities());
        } else if (normalAccountAuth.isTokenValid((String) authentication.getCredentials())) {
            return new CustomAuthenticationToken(tokenAuth.getCredentials(), NormalAccountAuth.getAuthorities());
        } else {
            throw new BadCredentialsException("invalid token");
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
