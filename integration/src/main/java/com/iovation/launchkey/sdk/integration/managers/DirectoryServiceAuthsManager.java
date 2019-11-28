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
import com.iovation.launchkey.sdk.domain.service.*;
import cucumber.api.java.After;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class DirectoryServiceAuthsManager {

    private DirectoryServiceManager directoryServiceManager;

    private final List<AuthPolicy.Location> locations = new ArrayList<>();
    private Integer factors;
    private boolean inherence;
    private boolean knowledge;
    private boolean possession;

    @SuppressWarnings("deprecation")
    private AuthorizationResponse currentAuthResponse;
    private AuthPolicy currentAuthPolicy;
    private AdvancedAuthorizationResponse currentAdvancedAuthResponse;
    private AuthorizationRequest currentAuthRequest;

    @Inject
    public DirectoryServiceAuthsManager(DirectoryServiceManager directoryServiceManager) {
        this.directoryServiceManager = directoryServiceManager;
    }

    @After
    public void cleanUp() {
        currentAuthResponse = null;
        currentAdvancedAuthResponse = null;
        currentAuthPolicy = null;
        factors = null;
        inherence = false;
        knowledge = false;
        possession = false;
        locations.clear();
    }

    public void setFactors(Integer factors) {
        this.factors = factors;
    }

    public void setKnowledge() {
        this.knowledge = true;
    }

    public void setInherence() {
        this.inherence = true;
    }

    public void setPossession() {
        this.possession = true;
    }

    public void addLocation(AuthPolicy.Location location) {
        locations.add(location);
    }

    public void createAuthorizationRequest(String userIdentifier) throws Throwable {
        createAuthorizationRequest(userIdentifier, null, null, null, null, null, null, null);
    }

    public void createAuthorizationRequest(String userIdentifier, String context) throws Throwable {
        createAuthorizationRequest(userIdentifier, context, null, null, null, null, null, null);
    }

    public void createAuthorizationRequest(String userIdentifier, String context, AuthPolicy policy) throws Throwable {
        createAuthorizationRequest(userIdentifier, context, policy, null, null, null, null, null);
    }

    public void createAuthorizationRequest(String userIdentifier, String context, AuthPolicy policy,
                                                    String title, Integer ttl, String pushTitle, String pushBody,
                                                    List<DenialReason> denialReasons) throws Throwable {
        currentAuthResponse = null;
        currentAuthPolicy = policy;
        currentAuthRequest = getServiceClient().createAuthorizationRequest(userIdentifier, context, policy, title, ttl, pushTitle, pushBody, denialReasons);
    }

    public AuthPolicy getCurrentAuthPolicy() {
        if (currentAuthPolicy == null) {
            if (factors != null) {
                currentAuthPolicy = new AuthPolicy(factors, locations);
            } else {
                currentAuthPolicy = new AuthPolicy(knowledge, inherence, possession, locations);
            }
        }
        return currentAuthPolicy;
    }

    @SuppressWarnings("deprecation")
    public AuthorizationResponse getAuthorizationResponse(String authRequestId) throws Throwable {
        currentAuthResponse = getServiceClient().getAuthorizationResponse(authRequestId);
        return currentAuthResponse;
    }

    public AdvancedAuthorizationResponse getAdvancedAuthorizationResponse() throws Throwable {
        currentAdvancedAuthResponse = getServiceClient().getAdvancedAuthorizationResponse(currentAuthRequest.getId());
        return currentAdvancedAuthResponse;
    }

    public AdvancedAuthorizationResponse getAdvancedAuthorizationResponse(String authRequestId) throws Throwable {
        currentAdvancedAuthResponse = getServiceClient().getAdvancedAuthorizationResponse(authRequestId);
        return currentAdvancedAuthResponse;
    }

    @SuppressWarnings("deprecation")
    public AuthorizationResponse getAuthorizationResponse() throws Throwable {
       return getAuthorizationResponse(currentAuthRequest.getId());
    }

    @SuppressWarnings("deprecation")
    public AuthorizationResponse getCurrentAuthResponse() {
        return currentAuthResponse;
    }

    private ServiceClient getServiceClient() throws Throwable {
        return directoryServiceManager.getServiceClient();
    }

    public AdvancedAuthorizationResponse getCurrentAdvancedAuthorizationResponse() {
        return currentAdvancedAuthResponse;
    }

    public AuthorizationRequest getAuthorizationRequest() {
        return currentAuthRequest;
    }
}
