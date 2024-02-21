package cn.rypacker.productkeymanager.services.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

import static cn.rypacker.productkeymanager.common.Constants.COOKIE_KEY_ADMIN_AUTH;
import static cn.rypacker.productkeymanager.common.Constants.COOKIE_KEY_NORMAL_AUTH;


public class TokenAuthenticationFilter implements Filter {

    private final AuthenticationManager authenticationManager;

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!HttpServletRequest.class.isAssignableFrom(request.getClass())) {
            chain.doFilter(request, response);
            return;
        }

        var cookies = ((HttpServletRequest) request).getCookies();
        if (cookies == null) {
            chain.doFilter(request, response);
            return;
        }

        Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(COOKIE_KEY_ADMIN_AUTH)
                        || cookie.getName().equals(COOKIE_KEY_NORMAL_AUTH))
                .min((c1, c2) -> c2.getName().compareTo(c1.getName())) // admin first
                .map(cookie -> {
                    var token = authenticationManager.authenticate(new CustomAuthenticationToken(cookie.getValue()));
                    SecurityContextHolder.getContext().setAuthentication(token);
                    return null;
                });

        chain.doFilter(request, response);
    }
}
