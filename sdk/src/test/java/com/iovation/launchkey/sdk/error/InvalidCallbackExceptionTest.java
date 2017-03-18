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
public class InvalidCallbackExceptionTest {
    private final String MESSAGE = "Expected Message";
    private final Throwable CAUSE = new Throwable();
    private final String CODE = "Error Code";

    @Test
    public void constructorMessageCauseErrorCode() throws Exception {
        InvalidCallbackException exception = new InvalidCallbackException(MESSAGE, CAUSE, CODE);
        assertEquals("Unexpected message value", MESSAGE, exception.getMessage());
        assertEquals("Unexpected cause value", CAUSE, exception.getCause());
        assertEquals("Unexpected error code value", CODE, exception.getErrorCode());
    }

    @Test
    public void equalsIsTrueForSameObject() throws Exception {
        InvalidCallbackException base = new InvalidCallbackException(MESSAGE, CAUSE, CODE);
        InvalidCallbackException other = base;
        assertTrue(base.equals(other));
    }

    @Test
    public void equalsIsTrueForEquivalentObject() throws Exception {
        InvalidCallbackException base = new InvalidCallbackException(MESSAGE, CAUSE, CODE);
        InvalidCallbackException other = new InvalidCallbackException(MESSAGE, CAUSE, CODE);
        assertTrue(base.equals(other));
    }

    @Test
    public void equalsIsFalseForDifferentMessage() throws Exception {
        InvalidCallbackException base = new InvalidCallbackException(MESSAGE, CAUSE, CODE);
        InvalidCallbackException other = new InvalidCallbackException(null, CAUSE, CODE);
        assertFalse(base.equals(other));
    }

    @Test
    public void equalsIsFalseForDifferentCause() throws Exception {
        InvalidCallbackException base = new InvalidCallbackException(MESSAGE, CAUSE, CODE);
        InvalidCallbackException other = new InvalidCallbackException(MESSAGE, null, CODE);
        assertFalse(base.equals(other));
    }

    @Test
    public void equalsIsFalseForDifferentErrorCode() throws Exception {
        InvalidCallbackException base = new InvalidCallbackException(MESSAGE, CAUSE, CODE);
        InvalidCallbackException other = new InvalidCallbackException(MESSAGE, CAUSE, null);
        assertFalse(base.equals(other));
    }

    @Test
    public void hashCodeIsEqualForSameObject() throws Exception {
        InvalidCallbackException base = new InvalidCallbackException(MESSAGE, CAUSE, CODE);
        InvalidCallbackException other = base;
        assertEquals(base.hashCode(), other.hashCode());

    }

    @Test
    public void hashCodeIsEqualForEquivalentObject() throws Exception {
        InvalidCallbackException base = new InvalidCallbackException(MESSAGE, CAUSE, CODE);
        InvalidCallbackException other = new InvalidCallbackException(MESSAGE, CAUSE, CODE);
        assertEquals(base.hashCode(), other.hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentMessage() throws Exception {
        InvalidCallbackException base = new InvalidCallbackException(MESSAGE, CAUSE, CODE);
        InvalidCallbackException other = new InvalidCallbackException(null, CAUSE, CODE);
        assertNotEquals(base.hashCode(), other.hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentCause() throws Exception {
        InvalidCallbackException base = new InvalidCallbackException(MESSAGE, CAUSE, CODE);
        InvalidCallbackException other = new InvalidCallbackException(MESSAGE, null, CODE);
        assertNotEquals(base.hashCode(), other.hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentErrorCode() throws Exception {
        InvalidCallbackException base = new InvalidCallbackException(MESSAGE, CAUSE, CODE);
        InvalidCallbackException other = new InvalidCallbackException(MESSAGE, CAUSE, null);
        assertNotEquals(base.hashCode(), other.hashCode());
    }

    @Test
    public void toStringContainsClassName() throws Exception {
        assertTrue(new InvalidCallbackException(MESSAGE, CAUSE, CODE).toString().contains(InvalidCallbackException.class.getSimpleName()));
    }
}
