package com.iovation.launchkey.sdk.error;

import org.junit.Test;

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
        assertTrue(new InvalidRequestException(MESSAGE, CAUSE, CODE).toString().contains(InvalidRequestException.class.getSimpleName()));
    }

    @Test
    public void fromARG001IsInvalidParameters() throws Exception {
        assertEquals(new InvalidParameters("Message", null, "ARG-001"),
                InvalidRequestException.fromErrorCode("ARG-001", "Message"));
    }

    @Test
    public void fromARG002IsInvalidRoute() throws Exception {
        assertEquals(new InvalidRoute("Message", null, "ARG-002"),
                InvalidRequestException.fromErrorCode("ARG-002", "Message"));
    }

    @Test
    public void fromSVC001ServiceNameTaken() throws Exception {
        assertEquals(new ServiceNameTaken("Message", null, "SVC-001"),
                InvalidRequestException.fromErrorCode("SVC-001", "Message"));
    }

    @Test
    public void fromSVC002IsInvalidPolicyInput() throws Exception {
        assertEquals(new InvalidPolicyInput("Message", null, "SVC-002"),
                InvalidRequestException.fromErrorCode("SVC-002", "Message"));
    }

    @Test
    public void fromSVC003IsPolicyFailure() throws Exception {
        assertEquals(new PolicyFailure("Message", null, "SVC-003"),
                InvalidRequestException.fromErrorCode("SVC-003", "Message"));
    }

    @Test
    public void fromSVC004IsServiceNotFound() throws Exception {
        assertEquals(new ServiceNotFound("Message", null, "SVC-004"),
                InvalidRequestException.fromErrorCode("SVC-004", "Message"));
    }

    @Test
    public void fromDIR0011IsInvalidDirectoryIdentifier() throws Exception {
        assertEquals(new InvalidParameters("Message", null, "DIR-001"),
                InvalidDirectoryIdentifier.fromErrorCode("DIR-001", "Message"));
    }

    @Test
    public void fromKEY001IsInvalidPublicKey() throws Exception {
        assertEquals(new InvalidParameters("Message", null, "KEY-001"),
                InvalidPublicKey.fromErrorCode("KEY-001", "Message"));
    }

    @Test
    public void fromKEY002IsPublicKeyAlreadyInUse() throws Exception {
        assertEquals(new InvalidParameters("Message", null, "KEY-002"),
                PublicKeyAlreadyInUse.fromErrorCode("KEY-002", "Message"));
    }

    @Test
    public void fromKEY003IsPublicKeyDoesNotExist() throws Exception {
        assertEquals(new PublicKeyDoesNotExist("Message", null, "KEY-003"),
                InvalidRequestException.fromErrorCode("KEY-003", "Message"));
    }

    @Test
    public void fromKEY0041IsLastRemainingKey() throws Exception {
        assertEquals(new LastRemainingKey("Message", null, "KEY-003"),
                InvalidRequestException.fromErrorCode("KEY-003", "Message"));
    }

    @Test
    public void fromORG003IsDirectoryNameInUse() throws Exception {
        assertEquals(new DirectoryNameInUse("Message", null, "ORG-003"),
                InvalidRequestException.fromErrorCode("ORG-003", "Message"));
    }

    @Test
    public void fromORG005IsLastRemainingSDKKey() throws Exception {
        assertEquals(new LastRemainingSDKKey("Message", null, "ORG-005"),
                InvalidRequestException.fromErrorCode("ORG-005", "Message"));
    }

    @Test
    public void fromORG006IsInvalidSDKKey() throws Exception {
        assertEquals(new InvalidSDKKey("Message", null, "ORG-006"),
                InvalidRequestException.fromErrorCode("ORG-006", "Message"));
    }
}
