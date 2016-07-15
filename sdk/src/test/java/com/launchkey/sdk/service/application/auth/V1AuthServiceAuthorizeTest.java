package com.launchkey.sdk.service.application.auth;

import com.launchkey.sdk.domain.auth.AuthPolicy;
import com.launchkey.sdk.transport.v1.domain.AuthsRequest;
import com.launchkey.sdk.transport.v1.domain.AuthsResponse;
import com.launchkey.sdk.transport.v1.domain.Policy.Factor;
import com.launchkey.sdk.transport.v1.domain.Policy.Policy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

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
@SuppressWarnings("Duplicates")
public class V1AuthServiceAuthorizeTest extends V1AuthServiceTestBase {

    protected final AuthsResponse authsResponse = new AuthsResponse("auth request");

    @Override @Before
    public void setUp() throws Exception {
        super.setUp();
        when(transport.auths(any(AuthsRequest.class))).thenReturn(authsResponse);
    }

    @Test
    public void testPassesAuthsRequestToTransportAuthsCall() throws Exception {
        service.authorize("username");
        verify(transport).auths(any(AuthsRequest.class));
    }

    @Test
    public void testPassesCorrectAppKeyInAuthsRequest() throws Exception {
        service.authorize("username");
        ArgumentCaptor<AuthsRequest> argumentCaptor = ArgumentCaptor.forClass(AuthsRequest.class);
        verify(transport).auths(argumentCaptor.capture());
        assertEquals(appKey, argumentCaptor.getValue().getAppKey());
    }

    @Test
    public void testPassesCorrectRsaEncryptedSecretKeyInAuthsRequest() throws Exception {
        service.authorize("username");
        ArgumentCaptor<AuthsRequest> argumentCaptor = ArgumentCaptor.forClass(AuthsRequest.class);
        verify(transport).auths(argumentCaptor.capture());
        assertEquals(base64.encodeAsString("RSA Encrypted Value".getBytes()), argumentCaptor.getValue().getSecretKey());
    }

    @Test
    public void testPassesCorrectSignatureInAuthsRequest() throws Exception {
        service.authorize("username");
        ArgumentCaptor<AuthsRequest> argumentCaptor = ArgumentCaptor.forClass(AuthsRequest.class);
        verify(transport).auths(argumentCaptor.capture());
        assertEquals(base64.encodeAsString("RSA Signature".getBytes()), argumentCaptor.getValue().getSignature());
    }

    @Test
    public void testPassesZeroAsSessionInAuthsRequest() throws Exception {
        service.authorize("username");
        ArgumentCaptor<AuthsRequest> argumentCaptor = ArgumentCaptor.forClass(AuthsRequest.class);
        verify(transport).auths(argumentCaptor.capture());
        assertEquals(0, argumentCaptor.getValue().getSession());
    }

    @Test
    public void testPassesOneAsUserPushIdInAuthsRequest() throws Exception {
        service.authorize("username");
        ArgumentCaptor<AuthsRequest> argumentCaptor = ArgumentCaptor.forClass(AuthsRequest.class);
        verify(transport).auths(argumentCaptor.capture());
        assertEquals(1, argumentCaptor.getValue().getUserPushID());
    }

    @Test
    public void testPassesContextInAuthsRequest() throws Exception {
        String expected = "Expected Context";
        service.authorize("username", "Expected Context");
        ArgumentCaptor<AuthsRequest> argumentCaptor = ArgumentCaptor.forClass(AuthsRequest.class);
        verify(transport).auths(argumentCaptor.capture());
        assertEquals(expected, argumentCaptor.getValue().getContext());
    }

    @Test
    public void testPassesCorrectAuthPolicyInAuthsRequestForFactorsCountBasedPolicy() throws Exception {
        List<Policy.MinimumRequirement> minimumRequirements = new ArrayList<Policy.MinimumRequirement>();
        minimumRequirements.add(new Policy.MinimumRequirement(Policy.MinimumRequirement.Type.AUTHENTICATED, 99, 0, 0, 0));
        List<Factor> factors = new ArrayList<Factor>();
        List<Factor.Location> factorLocations = new ArrayList<Factor.Location>();
        factorLocations.add(new Factor.Location(1.1, 2.2, 3.3));
        factors.add(new Factor(
                Factor.Type.GEOFENCE,
                true,
                Factor.Requirement.FORCED,
                1,
                new Factor.Attributes(factorLocations)
        ));
        Policy expected = new Policy(minimumRequirements, factors);
        List<AuthPolicy.Location> authPolicyLocations = new ArrayList<AuthPolicy.Location>();
        authPolicyLocations.add(new AuthPolicy.Location(1.1, 2.2, 3.3));
        service.authorize("username", null, new AuthPolicy(99, authPolicyLocations));
        ArgumentCaptor<AuthsRequest> argumentCaptor = ArgumentCaptor.forClass(AuthsRequest.class);
        verify(transport).auths(argumentCaptor.capture());
        assertEquals(expected, argumentCaptor.getValue().getPolicy());
    }

    @Test
    public void testPassesCorrectAuthPolicyInAuthsRequestForRequiredFactorBasedPolicy() throws Exception {
        List<Policy.MinimumRequirement> minimumRequirements = new ArrayList<Policy.MinimumRequirement>();
        minimumRequirements.add(new Policy.MinimumRequirement(Policy.MinimumRequirement.Type.AUTHENTICATED, 0, 1, 1, 1));
        List<Factor.Location> factorLocations = new ArrayList<Factor.Location>();
        factorLocations.add(new Factor.Location(1.1, 2.2, 3.3));
        List<Factor> factors = new ArrayList<Factor>();
        factors.add(new Factor(
                Factor.Type.GEOFENCE,
                true,
                Factor.Requirement.FORCED,
                1,
                new Factor.Attributes(factorLocations)
        ));
        Policy expected = new Policy(minimumRequirements, factors);
        List<AuthPolicy.Location> authPolicyLocations = new ArrayList<AuthPolicy.Location>();
        authPolicyLocations.add(new AuthPolicy.Location(1.1, 2.2, 3.3));
        service.authorize("username", null, new AuthPolicy(true, true, true, authPolicyLocations));
        ArgumentCaptor<AuthsRequest> argumentCaptor = ArgumentCaptor.forClass(AuthsRequest.class);
        verify(transport).auths(argumentCaptor.capture());
        assertEquals(expected, argumentCaptor.getValue().getPolicy());
    }

    @Test
    public void testPassesContextWithNoExceptionWhenContextIs400Chars() throws Exception {
        String expected = (new String(new char[400])).replace("\0", "x");
        service.authorize("username", expected);
        ArgumentCaptor<AuthsRequest> argumentCaptor = ArgumentCaptor.forClass(AuthsRequest.class);
        verify(transport).auths(argumentCaptor.capture());
        assertEquals(expected, argumentCaptor.getValue().getContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRaisesIllegalArgumentExceptionWhenContextGreaterThan400Chars() throws Exception {
        String expected = (new String(new char[401])).replace("\0", "x");
        service.authorize("username", expected);
    }
}
