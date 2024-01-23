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

@Component
@Slf4j
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private AdminAuth adminAuth;

    @Autowired
    private UserConfigStore userConfigStore;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
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
