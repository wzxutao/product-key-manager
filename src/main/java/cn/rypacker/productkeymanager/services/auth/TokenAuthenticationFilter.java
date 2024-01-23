package cn.rypacker.productkeymanager.services.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.ForwardAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static cn.rypacker.productkeymanager.common.Constants.COOKIE_KEY_ADMIN_AUTH;


public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        super("/**", authenticationManager);
        setAuthenticationSuccessHandler(new NullAuthenticationSuccessHandler());
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        var cookies = request.getCookies();
        if (cookies == null) {
            throw new InvalidCookieException("no cookies");
        }

        var token = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(COOKIE_KEY_ADMIN_AUTH)).findFirst().orElseThrow(() ->
                new InvalidCookieException("no token")).getValue();

//        var username = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(COOKIE_KEY_USERNAME)).findFirst().orElseThrow(() ->
//                new InvalidCookieException("no username")).getValue();

        return getAuthenticationManager().authenticate(new CustomAuthenticationToken(token));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
