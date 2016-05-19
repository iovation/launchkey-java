package com.launchkey.example.springmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 *
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private LogoutSuccessHandler logoutSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/favicon.ico", "/callback", "/authorized", "/images/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login").permitAll()
                .and().logout().permitAll().logoutSuccessHandler(logoutSuccessHandler)
                .and().csrf().ignoringAntMatchers("/callback", "/authorized");
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, AuthManager authManager, LogoutSuccessHandler logoutSuccessHandler) throws Exception {
        this.logoutSuccessHandler = logoutSuccessHandler;
        auth.authenticationProvider(new MultiFactorAuthenticationProvider(authManager));
    }
}
