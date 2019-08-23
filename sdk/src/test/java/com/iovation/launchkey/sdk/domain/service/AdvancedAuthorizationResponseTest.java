package com.iovation.launchkey.sdk.domain.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

/**
 * Copyright 2017 iovation, Inc. All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class AdvancedAuthorizationResponseTest {

    private AdvancedAuthorizationResponse authorizationResponse;

    @Before
    public void setUp() throws Exception {
        authorizationResponse = new AdvancedAuthorizationResponse(
                "Auth Request ID",
                true,
                "User Hash",
                "Organization User ID",
                "User Push ID",
                "Device ID",
                Arrays.asList("pin1", "pin2"),
                AdvancedAuthorizationResponse.Type.AUTHORIZED,
                AdvancedAuthorizationResponse.Reason.AUTHENTICATION,
                "Denial Reason",
                true,
                new AuthorizationResponsePolicy(Requirement.OTHER, 0, null, false, false, false),
                new ArrayList<AuthMethod>() {{
                    add(new AuthMethod(AuthMethod.Type.FACE, true, true, true, true, true, true, true));
                }}
            );
    }

    @After
    public void tearDown() throws Exception {
        authorizationResponse = null;
    }

    @Test
    public void testGetAuthRequestId() {
        assertEquals("Auth Request ID", authorizationResponse.getAuthorizationRequestId());
    }

    @Test
    public void testIsAuthorized() {
        assertTrue(authorizationResponse.isAuthorized());
    }

    @Test
    public void testGetUserHash() {
        assertEquals("User Hash", authorizationResponse.getServiceUserHash());
    }

    @Test
    public void testGetOrganizationUserId() {
        assertEquals("Organization User ID", authorizationResponse.getOrganizationUserHash());
    }

    @Test
    public void testGetUserPushId() {
        assertEquals("User Push ID", authorizationResponse.getUserPushId());
    }

    @Test
    public void testGetDeviceId() {
        assertEquals("Device ID", authorizationResponse.getDeviceId());
    }

    @Test
    public void testGetServicePins() {
        assertEquals(Arrays.asList("pin1", "pin2"), authorizationResponse.getServicePins());
    }

    @Test
    public void testGetType() {
        assertEquals(AdvancedAuthorizationResponse.Type.AUTHORIZED, authorizationResponse.getType());
    }

    @Test
    public void testGetReason() {
        assertEquals(AdvancedAuthorizationResponse.Reason.AUTHENTICATION, authorizationResponse.getReason());
    }

    @Test
    public void testGetDenialReason() {
        assertEquals("Denial Reason", authorizationResponse.getDenialReason());
    }

    @Test
    public void testIsFraud() {
        assertTrue(authorizationResponse.isFraud());
    }

    @Test
    public void testPolicy() {
        assertEquals(
                authorizationResponse.getPolicy(),
                new AuthorizationResponsePolicy(Requirement.OTHER, 0, null, false, false, false)
        );
    }

    @Test
    public void testAuthMethods() {
        assertEquals(
                authorizationResponse.getAuthMethods(),
                new ArrayList<AuthMethod>() {{
                    add(new AuthMethod(AuthMethod.Type.FACE, true, true, true, true, true, true, true));
                }}
        );
    }

    @Test
    public void testEqualsForEqualObjectsIsTrue() {
        AdvancedAuthorizationResponse left = new AdvancedAuthorizationResponse(null, true, null, null, null, null, null, AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.APPROVED, "a", null, null, null);
        AdvancedAuthorizationResponse right = new AdvancedAuthorizationResponse(null, true, null, null, null, null, null, AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.APPROVED, "a", null, null, null);
        assertEquals(left, right);
    }

    @Test
    public void testEqualsForUnEqualAuthRequestIdIsFalse() {
        AdvancedAuthorizationResponse left = new AdvancedAuthorizationResponse("left", true, null, null, null, null, null, AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.APPROVED, "a", null, null, null);
        AdvancedAuthorizationResponse right = new AdvancedAuthorizationResponse("left", true, null, null, null, null, null, AdvancedAuthorizationResponse.Type.DENIED, AdvancedAuthorizationResponse.Reason.APPROVED, "a", null, null, null);
        assertNotEquals(left, right);
    }

    @Test
    public void testEqualsForUnEqualAuthorizedIsFalse() {
        AdvancedAuthorizationResponse left = new AdvancedAuthorizationResponse("left", true, null, null, null, null, null, AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.APPROVED, "a", null, null, null);
        AdvancedAuthorizationResponse right = new AdvancedAuthorizationResponse("left", false, null, null, null, null, null, AdvancedAuthorizationResponse.Type.DENIED, AdvancedAuthorizationResponse.Reason.APPROVED, "a", null, null, null);
        assertNotEquals(left, right);
    }

    @Test
    public void testEqualsForUnEqualServiceUserHashIsFalse() {
        AdvancedAuthorizationResponse left = new AdvancedAuthorizationResponse("left", true, "a", null, null, null, null, AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.APPROVED, "a", null, null, null);
        AdvancedAuthorizationResponse right = new AdvancedAuthorizationResponse("left", true, "b", null, null, null, null, AdvancedAuthorizationResponse.Type.DENIED, AdvancedAuthorizationResponse.Reason.APPROVED, "a", null, null, null);
        assertNotEquals(left, right);
    }

    @Test
    public void testEqualsForUnEqualOrgUserhashIsFalse() {
        AdvancedAuthorizationResponse left = new AdvancedAuthorizationResponse("left", true, null, "a", null, null, null, AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.APPROVED, "a", null, null, null);
        AdvancedAuthorizationResponse right = new AdvancedAuthorizationResponse("left", true, null, "b", null, null, null, AdvancedAuthorizationResponse.Type.DENIED, AdvancedAuthorizationResponse.Reason.APPROVED, "a", null, null, null);
        assertNotEquals(left, right);
    }

    @Test
    public void testEqualsForUnEqualUserPushIdIsFalse() throws Exception {
        AdvancedAuthorizationResponse left = new AdvancedAuthorizationResponse("left", true, null, null, "a", null, null, AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.APPROVED, "a", null, null, null);
        AdvancedAuthorizationResponse right = new AdvancedAuthorizationResponse("left", true, null, null, "b", null, null, AdvancedAuthorizationResponse.Type.DENIED, AdvancedAuthorizationResponse.Reason.APPROVED, "a", null, null, null);
        assertNotEquals(left, right);
    }

    @Test
    public void testEqualsForUnEqualDeviceIdIsFalse() throws Exception {
        AdvancedAuthorizationResponse left = new AdvancedAuthorizationResponse("left", true, null, null, null, "a", null, AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.APPROVED, "a", null, null, null);
        AdvancedAuthorizationResponse right = new AdvancedAuthorizationResponse("left", true, null, null, null, "b", null, AdvancedAuthorizationResponse.Type.DENIED, AdvancedAuthorizationResponse.Reason.APPROVED, "a", null, null, null);
        assertNotEquals(left, right);
    }

    @Test
    public void testEqualsForUnEqualServicePinsIsFalse() throws Exception {
        AdvancedAuthorizationResponse left = new AdvancedAuthorizationResponse("left", true, null, null, null, null, Arrays.asList("a", "b"), AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.APPROVED, "a", null, null, null);
        AdvancedAuthorizationResponse right = new AdvancedAuthorizationResponse("left", true, null, null, null, null, Arrays.asList("c", "d"), AdvancedAuthorizationResponse.Type.DENIED, AdvancedAuthorizationResponse.Reason.APPROVED, "a", null, null, null);
        assertNotEquals(left, right);
    }

    @Test
    public void testEqualsForUnEqualTypeIsFalse() {
        AdvancedAuthorizationResponse left = new AdvancedAuthorizationResponse("left", true, null, null, null, null, null, AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.APPROVED, null, null, null, null);
        AdvancedAuthorizationResponse right = new AdvancedAuthorizationResponse("left", true, null, null, null, null, null, AdvancedAuthorizationResponse.Type.DENIED, AdvancedAuthorizationResponse.Reason.APPROVED, null, null, null, null);
        assertNotEquals(left, right);
    }

    @Test
    public void testEqualsForUnEqualReasonIsFalse() {
        AdvancedAuthorizationResponse left = new AdvancedAuthorizationResponse("left", true, null, null, null, null, null, AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.APPROVED, null, null, null, null);
        AdvancedAuthorizationResponse right = new AdvancedAuthorizationResponse("left", true, null, null, null, null, null, AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.AUTHENTICATION, null, null, null, null);
        assertNotEquals(left, right);
    }

    @Test
    public void testEqualsForUnEqualDenialReasonIsFalse() {
        AdvancedAuthorizationResponse left = new AdvancedAuthorizationResponse("left", true, null, null, null, null, null, AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.APPROVED, "a", null, null, null);
        AdvancedAuthorizationResponse right = new AdvancedAuthorizationResponse("left", true, null, null, null, null, null, AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.APPROVED, "b", null, null, null);
        assertNotEquals(left, right);
    }

    @Test
    public void testEqualsForUnEqualFraudIsFalse() {
        AdvancedAuthorizationResponse left = new AdvancedAuthorizationResponse("left", true, null, null, null, null, null, AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.APPROVED, null, true, null, null);
        AdvancedAuthorizationResponse right = new AdvancedAuthorizationResponse("left", true, null, null, null, null, null, AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.APPROVED, null, false, null, null);
        assertNotEquals(left, right);
    }

    @Test
    public void testEqualsForUnEqualPolicyIsFalse() {
        AdvancedAuthorizationResponse left = new AdvancedAuthorizationResponse("left", true, null, null, null, null, null, AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.APPROVED, null, null, null, null);
        AdvancedAuthorizationResponse right = new AdvancedAuthorizationResponse("left", true, null, null, null, null, null, AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.APPROVED, null, null, new AuthorizationResponsePolicy(Requirement.AMOUNT, 0, null, false, false, false), null);
        assertNotEquals(left, right);
    }

    @Test
    public void testEqualsForUnEqualAuthMethodsIsFalse() {
        AdvancedAuthorizationResponse left = new AdvancedAuthorizationResponse("left", true, null, null, null, null, null, AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.APPROVED, null, null, null, null);
        AdvancedAuthorizationResponse right = new AdvancedAuthorizationResponse("left", true, null, null, null, null, null, AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.APPROVED, null, null, null, new ArrayList<AuthMethod>());
        assertNotEquals(left, right);
    }

    @Test
    public void testHashCodeForEqualObjectsAreEqual() {
        AdvancedAuthorizationResponse left = new AdvancedAuthorizationResponse(null, true, null, null, null, null, null, AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.APPROVED, null, null, null, null);
        AdvancedAuthorizationResponse right = new AdvancedAuthorizationResponse(null, true, null, null, null, null, null, AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.APPROVED, null, null, null, null);
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testHashCodeForUnEqualOtherThanAuthRequestIdIsEqual() {
        AdvancedAuthorizationResponse left = new AdvancedAuthorizationResponse("left", true, "a", "a", "a", "a", Arrays.asList("a", "a"), AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.APPROVED, "a", true, null, null);
        AdvancedAuthorizationResponse right = new AdvancedAuthorizationResponse("left", false,"b", "b", "b", "b", Arrays.asList("b", "b"), AdvancedAuthorizationResponse.Type.AUTHORIZED, AdvancedAuthorizationResponse.Reason.APPROVED, "b", false, new AuthorizationResponsePolicy(Requirement.AMOUNT, 0, null, false, false, false), new ArrayList<AuthMethod>());
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testToStringContainsClassName() {
        assertThat(authorizationResponse.toString(), containsString(AdvancedAuthorizationResponse.class.getSimpleName()));
    }
}
