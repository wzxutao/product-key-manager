package cn.rypacker.productkeymanager.config;

import cn.rypacker.productkeymanager.services.auth.AdminAuth;
import cn.rypacker.productkeymanager.services.auth.CustomProviderManager;
import cn.rypacker.productkeymanager.services.auth.LoginAuthenticationSuccessHandler;
import cn.rypacker.productkeymanager.services.auth.TokenAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

import static cn.rypacker.productkeymanager.common.UserRole.ROLE_ADMIN;
import static cn.rypacker.productkeymanager.common.UserRole.ROLE_NORMAL;

@Configuration
@Slf4j
public class SecurityConfig {

    @Autowired
    private AdminAuth adminAuth;
    @Autowired
    private LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler;


    @Bean
    public CustomProviderManager customProviderManager() {
        return new CustomProviderManager(adminAuth);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            var cors = new org.springframework.web.cors.CorsConfiguration();
            cors.addAllowedOrigin("http://localhost:3000");
            List.of("GET", "POST", "PUT", "DELETE", "OPTIONS").forEach(cors::addAllowedMethod);
            cors.setAllowCredentials(true);
            cors.addAllowedHeader(HttpHeaders.CONTENT_TYPE);
            return cors;
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .addFilter(usernamePasswordAuthenticationFilter())
                .addFilterAfter(new TokenAuthenticationFilter(customProviderManager()), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/admin/**").hasAuthority(ROLE_ADMIN)
                .antMatchers(
                        "/check-key/**",
                        "/new-key/**",
                        "/today-records/**",
                        "/keygen/v2/**",
                        "/util/**").hasAuthority(ROLE_NORMAL)
                .antMatchers("/**").permitAll()
                .and()
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }


    @Bean
    public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() {
        var filter = new UsernamePasswordAuthenticationFilter(customProviderManager());
        filter.setFilterProcessesUrl("/auth/login");
        filter.setUsernameParameter("account");
        filter.setAuthenticationSuccessHandler(loginAuthenticationSuccessHandler);
        return filter;
    }
}
