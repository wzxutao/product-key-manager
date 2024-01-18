package cn.rypacker.productkeymanager.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static cn.rypacker.productkeymanager.common.Constants.ADMIN_AUTH_COOKIE_KEY;
import static cn.rypacker.productkeymanager.common.Constants.NORMAL_AUTH_COOKIE_KEY;

@Component
public class CustomAuthRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        var cookies = httpServletRequest.getCookies();
        if(cookies == null) {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        for(var cookie: cookies) {
            if(cookie.getName().equals(ADMIN_AUTH_COOKIE_KEY) || cookie.getName().equals(NORMAL_AUTH_COOKIE_KEY)) {
                var token = cookie.getValue();
                var userDetails = userDetailsService.loadUserByUsername(token);
                if(userDetails != null) {
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                    return;
                }
            }
        }
    }
}
