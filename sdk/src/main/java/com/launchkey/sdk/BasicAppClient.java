/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk;

import com.launchkey.sdk.service.application.auth.AuthService;

/**
 * Basic client for interacting with the Platform API.
 */
public class BasicAppClient implements AppClient {

    private final AuthService auth;

    /**
     * Basic client with all services
     *
     * @param auth auth service
     */
    public BasicAppClient(AuthService auth) {
        this.auth = auth;
    }

    @Override public AuthService auth() {
        return auth;
    }
}
