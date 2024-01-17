package cn.rypacker.productkeymanager.config;

import cn.rypacker.productkeymanager.services.auth.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/**").hasRole("NORMAL")
                .antMatchers("/auth/**", "/new-key/login").permitAll()
                .antMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
        return http.build();
    }


}
