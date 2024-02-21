package cn.rypacker.productkeymanager.services.auth;

import cn.rypacker.productkeymanager.services.configstore.UserConfigStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static cn.rypacker.productkeymanager.common.Constants.COOKIE_KEY_ADMIN_AUTH;
import static cn.rypacker.productkeymanager.common.Constants.COOKIE_KEY_NORMAL_AUTH;
import static cn.rypacker.productkeymanager.common.UserRole.ROLE_ADMIN;
import static cn.rypacker.productkeymanager.common.UserRole.ROLE_NORMAL;

@Component
@Slf4j
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private AdminAuth adminAuth;

    @Autowired
    private NormalAccountAuth normalAccountAuth;

    @Autowired
    private UserConfigStore userConfigStore;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {
        if(!authentication.isAuthenticated()) {
            return;
        }

        try {
            // admin
            if(authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals(ROLE_ADMIN))
            ) {
                var cookieAge = getTokenValidMinutes() * 60;
                var token = adminAuth.signNewToken(cookieAge);
                // cookie: auth token
                var cookie = new Cookie(COOKIE_KEY_ADMIN_AUTH, token);
                cookie.setMaxAge(cookieAge);
                cookie.setPath("/");
                response.addCookie(cookie);
                // cookie: username
                cookie = new Cookie("username", authentication.getName());
                cookie.setPath("/");
                cookie.setMaxAge(cookieAge);
                response.addCookie(cookie);
                // clear normal auth cookie
                cookie = new Cookie(COOKIE_KEY_NORMAL_AUTH, "");
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
                return;
            }

            if(authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals(ROLE_NORMAL))) {
                var cookieAge = 60 * 60 * 24 * 30;
//                var cookieAge = 60 * 2;
                var token = normalAccountAuth.signNewToken(authentication.getName());
                // cookie: auth token
                var cookie = new Cookie(COOKIE_KEY_NORMAL_AUTH, token);
                cookie.setMaxAge(cookieAge);
                cookie.setPath("/");
                response.addCookie(cookie);
                // cookie: username
                cookie = new Cookie("username", authentication.getName());
                cookie.setPath("/");
                cookie.setMaxAge(cookieAge);
                response.addCookie(cookie);
                // clear admin auth cookie
                cookie = new Cookie(COOKIE_KEY_ADMIN_AUTH, "");
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
                return;
            }


        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public int getTokenValidMinutes() {
        try {
            return userConfigStore.getData().getAuth().getAdmin().getValidMinutes();
        } catch (NumberFormatException e) {
            return 30;
        }

    }
}
