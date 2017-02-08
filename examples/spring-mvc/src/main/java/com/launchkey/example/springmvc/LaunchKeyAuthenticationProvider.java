package com.launchkey.example.springmvc;

import com.launchkey.sdk.error.BaseException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;

public class LaunchKeyAuthenticationProvider implements AuthenticationProvider {

    private final AuthManager authManager;

    private int authRequestCounter = 0;

    public LaunchKeyAuthenticationProvider(AuthManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();

        try {
            synchronized (this) {
                authRequestCounter++;
                this.authManager.login(username, String.format("Login Request #%s", authRequestCounter));
            }
            Boolean authorized = null;
            while (authorized == null) {
                Thread.sleep(100L);
                authorized = this.authManager.isAuthorized();
            }
            if (authorized == null) {
                throw new InsufficientAuthenticationException("The login request was not responded to in sufficient time");
            } else if (!authorized) {
                throw new InsufficientAuthenticationException("The login request was denied");
            }
        } catch (InterruptedException e) {
            throw new AuthenticationServiceException("Sleep error");
        } catch (AuthManager.AuthException e) {
            if (e.getCause() instanceof BaseException) {
                throw new BadCredentialsException("Login failure", e.getCause());
            }
        }

        return new UsernamePasswordAuthenticationToken(username, authentication.getCredentials(), new ArrayList<GrantedAuthority>());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
