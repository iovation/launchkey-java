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
public class CommunicationErrorExceptionTest {
    private final String MESSAGE = "Expected Message";
    private final Throwable CAUSE = new Throwable();
    private final String CODE = "Error Code";

    @Test
    public void constructorMessageCauseErrorCode() throws Exception {
        CommunicationErrorException exception = new CommunicationErrorException(MESSAGE, CAUSE, CODE);
        assertEquals("Unexpected message value", MESSAGE, exception.getMessage());
        assertEquals("Unexpected cause value", CAUSE, exception.getCause());
        assertEquals("Unexpected error code value", CODE, exception.getErrorCode());
    }

    @Test
    public void equalsIsTrueForSameObject() throws Exception {
        CommunicationErrorException base = new CommunicationErrorException(MESSAGE, CAUSE, CODE);
        assertTrue(base.equals(base));
    }

    @Test
    public void equalsIsTrueForEquivalentObject() throws Exception {
        CommunicationErrorException base = new CommunicationErrorException(MESSAGE, CAUSE, CODE);
        CommunicationErrorException other = new CommunicationErrorException(MESSAGE, CAUSE, CODE);
        assertTrue(base.equals(other));
    }

    @Test
    public void equalsIsFalseForDifferentMessage() throws Exception {
        CommunicationErrorException base = new CommunicationErrorException(MESSAGE, CAUSE, CODE);
        CommunicationErrorException other = new CommunicationErrorException(null, CAUSE, CODE);
        assertFalse(base.equals(other));
    }

    @Test
    public void equalsIsFalseForDifferentCause() throws Exception {
        CommunicationErrorException base = new CommunicationErrorException(MESSAGE, CAUSE, CODE);
        CommunicationErrorException other = new CommunicationErrorException(MESSAGE, null, CODE);
        assertFalse(base.equals(other));
    }

    @Test
    public void equalsIsFalseForDifferentErrorCode() throws Exception {
        CommunicationErrorException base = new CommunicationErrorException(MESSAGE, CAUSE, CODE);
        CommunicationErrorException other = new CommunicationErrorException(MESSAGE, CAUSE, null);
        assertFalse(base.equals(other));
    }

    @Test
    public void hashCodeIsEqualForSameObject() throws Exception {
        CommunicationErrorException base = new CommunicationErrorException(MESSAGE, CAUSE, CODE);
        assertEquals(base.hashCode(), base.hashCode());

    }

    @Test
    public void hashCodeIsEqualForEquivalentObject() throws Exception {
        CommunicationErrorException base = new CommunicationErrorException(MESSAGE, CAUSE, CODE);
        CommunicationErrorException other = new CommunicationErrorException(MESSAGE, CAUSE, CODE);
        assertEquals(base.hashCode(), other.hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentMessage() throws Exception {
        CommunicationErrorException base = new CommunicationErrorException(MESSAGE, CAUSE, CODE);
        CommunicationErrorException other = new CommunicationErrorException(null, CAUSE, CODE);
        assertNotEquals(base.hashCode(), other.hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentCause() throws Exception {
        CommunicationErrorException base = new CommunicationErrorException(MESSAGE, CAUSE, CODE);
        CommunicationErrorException other = new CommunicationErrorException(MESSAGE, null, CODE);
        assertNotEquals(base.hashCode(), other.hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentErrorCode() throws Exception {
        CommunicationErrorException base = new CommunicationErrorException(MESSAGE, CAUSE, CODE);
        CommunicationErrorException other = new CommunicationErrorException(MESSAGE, CAUSE, null);
        assertNotEquals(base.hashCode(), other.hashCode());
    }

    @Test
    public void toStringContainsClassName() throws Exception {
        assertTrue(new CommunicationErrorException(MESSAGE, CAUSE, CODE).toString()
                .contains(CommunicationErrorException.class.getSimpleName()));
    }

    @Test
    public void from401IsUnauthorized() throws Exception {
        assertEquals(new Unauthorized("Message", null, "401"),
                CommunicationErrorException.fromStatusCode(401, "Message"));
    }

    @Test
    public void from403IsForbidden() throws Exception {
        assertEquals(new Unauthorized("Message", null, "403"),
                CommunicationErrorException.fromStatusCode(403, "Message"));
    }

    @Test
    public void from404IsEntityNotFound() throws Exception {
        assertEquals(new Unauthorized("Message", null, "404"),
                CommunicationErrorException.fromStatusCode(404, "Message"));
    }

    @Test
    public void from408IsTimedOut() throws Exception {
        assertEquals(new Unauthorized("Message", null, "408"),
                CommunicationErrorException.fromStatusCode(408, "Message"));
    }

    @Test
    public void from429IsRateLimited() throws Exception {
        assertEquals(new Unauthorized("Message", null, "429"),
                CommunicationErrorException.fromStatusCode(429, "Message"));
    }
}
