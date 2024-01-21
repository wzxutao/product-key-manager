package cn.rypacker.productkeymanager.config;

import cn.rypacker.productkeymanager.services.auth.CustomAuthenticationManager;
import cn.rypacker.productkeymanager.services.auth.CustomAuthenticationSuccessHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Slf4j
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationManager customAuthenticationManager;
    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .addFilter(usernamePasswordAuthenticationFilter())
                .authorizeRequests()
//                .antMatchers("/auth/**", "/new-key/login").permitAll()
//                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/**").permitAll()
                .and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }


    @Bean
    public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() {
        var filter = new UsernamePasswordAuthenticationFilter(customAuthenticationManager);
        filter.setFilterProcessesUrl("/auth/login");
        filter.setUsernameParameter("account");
        filter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        return filter;
    }
}
