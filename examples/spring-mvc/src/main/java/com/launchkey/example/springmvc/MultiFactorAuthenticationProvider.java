package com.launchkey.example.springmvc;

import com.launchkey.sdk.service.error.ApiException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;

/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
 *
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class MultiFactorAuthenticationProvider implements AuthenticationProvider {

    private AuthManager authManager;

    private int authRequestCounter = 0;

    public MultiFactorAuthenticationProvider(AuthManager authManager) {
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
            if (e.getCause() instanceof ApiException) {
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
