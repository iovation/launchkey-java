/**
 * Copyright 2017 iovation, Inc.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iovation.launchkey.sdk.integration.managers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.iovation.launchkey.sdk.client.ServiceClient;
import com.iovation.launchkey.sdk.domain.service.AuthPolicy;
import com.iovation.launchkey.sdk.domain.service.AuthorizationResponse;
import com.iovation.launchkey.sdk.domain.service.DenialReason;

import java.util.List;

@Singleton
public class DirectoryServiceAuthsManager {

    private DirectoryServiceManager directoryServiceManager;

    private String currentAuthRequestId;
    private AuthorizationResponse currentAuthResponse;
    private AuthPolicy currentAuthPolicy;

    @Inject
    public DirectoryServiceAuthsManager(DirectoryServiceManager directoryServiceManager) {
        this.directoryServiceManager = directoryServiceManager;
    }

    public String createAuthorizationRequest(String userIdentifier) throws Throwable {
        currentAuthResponse = null;
        currentAuthPolicy = null;
        currentAuthRequestId = getServiceClient().createAuthorizationRequest(userIdentifier).getId();
        return currentAuthRequestId;
    }

    public String createAuthorizationRequest(String userIdentifier, String context) throws Throwable {
        currentAuthResponse = null;
        currentAuthPolicy = null;
        currentAuthRequestId = getServiceClient().createAuthorizationRequest(userIdentifier, context).getId();
        return currentAuthRequestId;
    }

    public String createAuthorizationRequest(String userIdentifier, String context, AuthPolicy policy) throws Throwable {
        currentAuthResponse = null;
        currentAuthPolicy = policy;
        currentAuthRequestId = getServiceClient().createAuthorizationRequest(userIdentifier, context, policy).getId();
        return currentAuthRequestId;
    }

    public String createAuthorizationRequest(String userIdentifier, String context, AuthPolicy policy, String title, Integer ttl) throws Throwable {
        currentAuthResponse = null;
        currentAuthPolicy = policy;
        currentAuthRequestId = getServiceClient().createAuthorizationRequest(userIdentifier, context, policy, title, ttl).getId();
        return currentAuthRequestId;
    }

    public String createAuthorizationRequest(String userIdentifier, String context, AuthPolicy policy,
                                                    String title, Integer ttl, String pushTitle, String pushBody,
                                                    List<DenialReason> denialReasons) throws Throwable {
        currentAuthResponse = null;
        currentAuthPolicy = policy;
        currentAuthRequestId = getServiceClient().createAuthorizationRequest(userIdentifier, context, policy, title, ttl, pushTitle, pushBody, denialReasons).getId();
        return currentAuthRequestId;
    }

    public AuthPolicy getCurrentAuthPolicy() {
        return currentAuthPolicy;
    }

    public AuthorizationResponse getAuthorizationResponse() throws Throwable {
        currentAuthResponse = getServiceClient().getAuthorizationResponse(currentAuthRequestId);
        return currentAuthResponse;
    }

    public AuthorizationResponse getCurrentAuthResponse() {
        return currentAuthResponse;
    }

    private ServiceClient getServiceClient() throws Throwable {
        return directoryServiceManager.getServiceClient();
    }
}
