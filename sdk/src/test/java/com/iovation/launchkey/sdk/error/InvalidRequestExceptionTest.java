package com.iovation.launchkey.sdk.error;

import com.iovation.launchkey.sdk.transport.domain.Error;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Copyright 2017 iovation, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class InvalidRequestExceptionTest {
    private final String MESSAGE = "Expected Message";
    private final Throwable CAUSE = new Throwable();
    private final String CODE = "Error Code";

    @Test
    public void constructorMessageCauseErrorCode() throws Exception {
        InvalidRequestException exception = new InvalidRequestException(MESSAGE, CAUSE, CODE);
        assertEquals("Unexpected message value", MESSAGE, exception.getMessage());
        assertEquals("Unexpected cause value", CAUSE, exception.getCause());
        assertEquals("Unexpected error code value", CODE, exception.getErrorCode());
    }

    @Test
    public void equalsIsTrueForSameObject() throws Exception {
        InvalidRequestException base = new InvalidRequestException(MESSAGE, CAUSE, CODE);
        assertTrue(base.equals(base));
    }

    @Test
    public void equalsIsTrueForEquivalentObject() throws Exception {
        InvalidRequestException base = new InvalidRequestException(MESSAGE, CAUSE, CODE);
        InvalidRequestException other = new InvalidRequestException(MESSAGE, CAUSE, CODE);
        assertTrue(base.equals(other));
    }

    @Test
    public void equalsIsFalseForDifferentMessage() throws Exception {
        InvalidRequestException base = new InvalidRequestException(MESSAGE, CAUSE, CODE);
        InvalidRequestException other = new InvalidRequestException(null, CAUSE, CODE);
        assertFalse(base.equals(other));
    }

    @Test
    public void equalsIsFalseForDifferentCause() throws Exception {
        InvalidRequestException base = new InvalidRequestException(MESSAGE, CAUSE, CODE);
        InvalidRequestException other = new InvalidRequestException(MESSAGE, null, CODE);
        assertFalse(base.equals(other));
    }

    @Test
    public void equalsIsFalseForDifferentErrorCode() throws Exception {
        InvalidRequestException base = new InvalidRequestException(MESSAGE, CAUSE, CODE);
        InvalidRequestException other = new InvalidRequestException(MESSAGE, CAUSE, null);
        assertFalse(base.equals(other));
    }

    @Test
    public void hashCodeIsEqualForSameObject() throws Exception {
        InvalidRequestException base = new InvalidRequestException(MESSAGE, CAUSE, CODE);
        assertEquals(base.hashCode(), base.hashCode());

    }

    @Test
    public void hashCodeIsEqualForEquivalentObject() throws Exception {
        InvalidRequestException base = new InvalidRequestException(MESSAGE, CAUSE, CODE);
        InvalidRequestException other = new InvalidRequestException(MESSAGE, CAUSE, CODE);
        assertEquals(base.hashCode(), other.hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentMessage() throws Exception {
        InvalidRequestException base = new InvalidRequestException(MESSAGE, CAUSE, CODE);
        InvalidRequestException other = new InvalidRequestException(null, CAUSE, CODE);
        assertNotEquals(base.hashCode(), other.hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentCause() throws Exception {
        InvalidRequestException base = new InvalidRequestException(MESSAGE, CAUSE, CODE);
        InvalidRequestException other = new InvalidRequestException(MESSAGE, null, CODE);
        assertNotEquals(base.hashCode(), other.hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentErrorCode() throws Exception {
        InvalidRequestException base = new InvalidRequestException(MESSAGE, CAUSE, CODE);
        InvalidRequestException other = new InvalidRequestException(MESSAGE, CAUSE, null);
        assertNotEquals(base.hashCode(), other.hashCode());
    }

    @Test
    public void toStringContainsClassName() throws Exception {
        assertTrue(new InvalidRequestException(MESSAGE, CAUSE, CODE).toString()
                .contains(InvalidRequestException.class.getSimpleName()));
    }

    @Test
    public void fromARG001IsInvalidParameters() throws Exception {
        assertEquals(new InvalidParameters("{\"arg1\":\"Invalid arg1\"}", null, "ARG-001"),
                InvalidRequestException.fromError(
                        new Error("ARG-001", new HashMap() {{put("arg1", "Invalid arg1");}}, null)));
    }

    @Test
    public void fromARG002IsInvalidRoute() throws Exception {
        assertEquals(new InvalidRoute("Message", null, "ARG-002"),
                InvalidRequestException.fromError(new Error("ARG-002", "Message", null)));
    }

    @Test
    public void fromSVC001ServiceNameTaken() throws Exception {
        assertEquals(new ServiceNameTaken("Message", null, "SVC-001"),
                InvalidRequestException.fromError(new Error("SVC-001", "Message", null)));
    }

    @Test
    public void fromSVC002IsInvalidPolicyInput() throws Exception {
        assertEquals(new InvalidPolicyInput("Message", null, "SVC-002"),
                InvalidRequestException.fromError(new Error("SVC-002", "Message", null)));
    }

    @Test
    public void fromSVC003IsPolicyFailure() throws Exception {
        assertEquals(new PolicyFailure("Message", null, "SVC-003"),
                InvalidRequestException.fromError(new Error("SVC-003", "Message", null)));
    }

    @Test
    public void fromSVC004IsServiceNotFound() throws Exception {
        assertEquals(new ServiceNotFound("Message", null, "SVC-004"),
                InvalidRequestException.fromError(new Error("SVC-004", "Message", null)));
    }

    @Test
    public void fromSVC005IsInvalidRequestException() throws Exception {
        InvalidRequestException expected = new AuthorizationInProgress(
                "Message", null, "SVC-005", "adc0d351-d8a8-11e8-9fe8-acde48001122", true, new Date(){{setTime(0L);}});
        InvalidRequestException actual = InvalidRequestException.fromError(
                new Error("SVC-005", "Message", new HashMap<Object, Object>(){{
                    put("auth_request", "adc0d351-d8a8-11e8-9fe8-acde48001122");
                    put("my_auth", true);
                    put("expires", "1970-01-01T00:00:00Z");
                }})
        );
        assertEquals(expected, actual);
    }

    @Test
    public void fromSVC005WithNoErrorDataSetsItemsToNullOrFalse() throws Exception {
        InvalidRequestException expected = new AuthorizationInProgress(
                "Message", null, "SVC-005", null, false, null);
        InvalidRequestException actual = InvalidRequestException.fromError(
                new Error("SVC-005", "Message", null)
        );
        assertEquals(expected, actual);
    }

    @Test
    public void fromSVC005WithInvalidErrorDataSetsItemsToNullOrFalse() throws Exception {
        InvalidRequestException expected = new AuthorizationInProgress(
                "Message", null, "SVC-005", null, false, null);
        InvalidRequestException actual = InvalidRequestException.fromError(
                new Error("SVC-005", "Message", new HashMap<Object, Object>(){{
                    put("not", "valid stuff");
                }})
        );
        assertEquals(expected, actual);
    }

    @Test
    public void fromSVC005WithErrorDataExpiresNotAValidDateSetsSetsExpiresToNull() throws Exception {
        InvalidRequestException expected = new AuthorizationInProgress(
                "Message", null, "SVC-005", "adc0d351-d8a8-11e8-9fe8-acde48001122", true, null);
        InvalidRequestException actual = InvalidRequestException.fromError(
                new Error("SVC-005", "Message", new HashMap<Object, Object>(){{
                    put("auth_request", "adc0d351-d8a8-11e8-9fe8-acde48001122");
                    put("my_auth", true);
                    put("expires", "Not a valid Date");
                }})
        );
        assertEquals(expected, actual);
    }

    @Test
    public void fromSVC005WithErrorDataMyAuthNotABooleanSetsMyAuthToFalse() throws Exception {
        InvalidRequestException expected = new AuthorizationInProgress(
                "Message", null, "SVC-005", "adc0d351-d8a8-11e8-9fe8-acde48001122", false, new Date(){{setTime(0L);}});
        InvalidRequestException actual = InvalidRequestException.fromError(
                new Error("SVC-005", "Message", new HashMap<Object, Object>(){{
                    put("auth_request", "adc0d351-d8a8-11e8-9fe8-acde48001122");
                    put("my_auth", "Not a boolean");
                    put("expires", "1970-01-01T00:00:00Z");
                }})
        );
        assertEquals(expected, actual);
    }

    @Test
    public void fromDIR0011IsInvalidDirectoryIdentifier() throws Exception {
        assertEquals(new InvalidParameters("Message", null, "DIR-001"),
                InvalidDirectoryIdentifier.fromError(new Error("DIR-001", "Message", null)));
    }

    @Test
    public void fromKEY001IsInvalidPublicKey() throws Exception {
        assertEquals(new InvalidParameters("Message", null, "KEY-001"),
                InvalidPublicKey.fromError(new Error("KEY-001", "Message", null)));
    }

    @Test
    public void fromKEY002IsPublicKeyAlreadyInUse() throws Exception {
        assertEquals(new InvalidParameters("Message", null, "KEY-002"),
                PublicKeyAlreadyInUse.fromError(new Error("KEY-002", "Message", null)));
    }

    @Test
    public void fromKEY003IsPublicKeyDoesNotExist() throws Exception {
        assertEquals(new PublicKeyDoesNotExist("Message", null, "KEY-003"),
                InvalidRequestException.fromError(new Error("KEY-003", "Message", null)));
    }

    @Test
    public void fromKEY0041IsLastRemainingKey() throws Exception {
        assertEquals(new LastRemainingKey("Message", null, "KEY-003"),
                InvalidRequestException.fromError(new Error("KEY-003", "Message", null)));
    }

    @Test
    public void fromORG003IsDirectoryNameInUse() throws Exception {
        assertEquals(new DirectoryNameInUse("Message", null, "ORG-003"),
                InvalidRequestException.fromError(new Error("ORG-003", "Message", null)));
    }

    @Test
    public void fromORG005IsLastRemainingSDKKey() throws Exception {
        assertEquals(new LastRemainingSDKKey("Message", null, "ORG-005"),
                InvalidRequestException.fromError(new Error("ORG-005", "Message", null)));
    }

    @Test
    public void fromORG006IsInvalidSDKKey() throws Exception {
        assertEquals(new InvalidSDKKey("Message", null, "ORG-006"),
                InvalidRequestException.fromError(new Error("ORG-006", "Message", null)));
    }
}
