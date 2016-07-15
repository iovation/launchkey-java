/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.service.organization.whitelabel;

import com.launchkey.sdk.transport.v3.Transport;

import java.security.interfaces.RSAPrivateKey;

public class V3WhiteLabelServiceFactory implements WhiteLabelServiceFactory {
    private final RSAPrivateKey privateKey;
    private Transport transport;
    private long orgKey;

    public V3WhiteLabelServiceFactory(
            Transport transport, RSAPrivateKey privateKey, long orgKey
    ) {
        this.transport = transport;
        this.privateKey = privateKey;
        this.orgKey = orgKey;
    }

    @Override public WhiteLabelService getService(String sdkKey) {
        return new V3WhiteLabelService(transport, privateKey, orgKey, sdkKey);
    }
}
