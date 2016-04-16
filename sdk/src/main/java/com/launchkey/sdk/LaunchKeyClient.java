/**
 * Copyright 2016 LaunchKey, Inc.  All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk;

import com.launchkey.sdk.service.auth.AuthService;
import com.launchkey.sdk.service.whitelabel.WhiteLabelService;

import java.security.Provider;

/**
 * @deprecated Use a current implementation of {@link Client}
 */
@Deprecated
public class LaunchKeyClient implements Client {
    private final Client client;

    /**
     * @deprecated Use a current implementation of {@link Client}
     */
    public LaunchKeyClient(AuthService auth, WhiteLabelService whiteLabel) {
        this.client = new BasicClient(auth, whiteLabel);
    }

    /**
     * @deprecated Use a current implementation of {@link Client}
     */
    public LaunchKeyClient(Client client) {
        this.client = client;
    }

    /**
     * @deprecated Use a current implementation of {@link Client}
     */
    @Override public AuthService auth() {
        return this.client.auth();
    }

    /**
     * @deprecated Use a current implementation of {@link Client}
     */
    @Override public WhiteLabelService whiteLabel() {
        return this.client.whiteLabel();
    }

    /**
     * @deprecated Use a current implementation of {@link Client}
     */
    public static LaunchKeyClient factory(Config config) {
        Client client = BasicClient.factory(config);
        return new LaunchKeyClient(client);
    }

    /**
     * @deprecated Use a current implementation of {@link Client}
     */
    public static LaunchKeyClient factory(long appKey, String secretKey, String privateKeyPEM, Provider provider) {
        Client client = BasicClient.factory(appKey, secretKey, privateKeyPEM, provider);
        return new LaunchKeyClient(client);
    }
}
