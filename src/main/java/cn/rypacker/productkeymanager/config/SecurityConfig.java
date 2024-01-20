package cn.rypacker.productkeymanager.config;

import cn.rypacker.productkeymanager.services.auth.CustomAuthRequestFilter;
import cn.rypacker.productkeymanager.services.auth.CustomAuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomAuthRequestFilter customAuthRequestFilter;

    @Autowired
    private CustomAuthenticationManager customAuthenticationManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .addFilter(new UsernamePasswordAuthenticationFilter(customAuthenticationManager))
                .authorizeRequests()
//                .antMatchers("/auth/**", "/new-key/login").permitAll()
//                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/**").permitAll()
                .and()
                .csrf().disable();

        return http.build();
    }
}
