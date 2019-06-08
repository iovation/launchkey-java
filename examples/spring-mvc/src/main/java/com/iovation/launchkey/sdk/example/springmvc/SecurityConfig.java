package com.iovation.launchkey.sdk.example.springmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private LogoutSuccessHandler logoutSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/favicon.ico", AuthManager.SERVICE_WEBHOOK, AuthManager.DIRECTORY_WEBHOOK, "/authorized",
                        "/link", "/linked/**", "/images/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login").permitAll()
                .and().logout().permitAll().logoutSuccessHandler(logoutSuccessHandler)
                .and().csrf().ignoringAntMatchers(AuthManager.SERVICE_WEBHOOK, AuthManager.DIRECTORY_WEBHOOK,
                        "/authorized", "/link", "/linked/**");
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, AuthManager authManager,
                                LogoutSuccessHandler logoutSuccessHandler) throws Exception {
        this.logoutSuccessHandler = logoutSuccessHandler;
        auth.authenticationProvider(new LaunchKeyAuthenticationProvider(authManager));
    }
}
