package cn.rypacker.productkeymanager.services.auth;

import org.springframework.security.authentication.ProviderManager;

import java.util.List;


public class CustomProviderManager extends ProviderManager {

    public CustomProviderManager(AdminAuth adminAuth, NormalAccountAuth normalAccountAuth) {
        super(List.of(
                new TokenAuthenticationProvider(adminAuth, normalAccountAuth),
                new UsernamePasswordAuthenticationProvider(adminAuth, normalAccountAuth)
                ));
    }
}
