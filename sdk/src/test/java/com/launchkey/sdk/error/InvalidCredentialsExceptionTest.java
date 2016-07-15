package com.launchkey.sdk.error;

import org.junit.Test;

import static org.junit.Assert.*;

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
public class InvalidCredentialsExceptionTest {
    private final String MESSAGE = "Expected Message";
    private final Throwable CAUSE = new Throwable();
    private final String CODE = "Error Code";

    @Test
    public void constructorMessageCauseErrorCode() throws Exception {
        InvalidCredentialsException exception = new InvalidCredentialsException(MESSAGE, CAUSE, CODE);
        assertEquals("Unexpected message value", MESSAGE, exception.getMessage());
        assertEquals("Unexpected cause value", CAUSE, exception.getCause());
        assertEquals("Unexpected error code value", CODE, exception.getErrorCode());
    }

    @Test
    public void equalsIsTrueForSameObject() throws Exception {
        InvalidCredentialsException base = new InvalidCredentialsException(MESSAGE, CAUSE, CODE);
        InvalidCredentialsException other = base;
        assertTrue(base.equals(other));
    }

    @Test
    public void equalsIsTrueForEquivalentObject() throws Exception {
        InvalidCredentialsException base = new InvalidCredentialsException(MESSAGE, CAUSE, CODE);
        InvalidCredentialsException other = new InvalidCredentialsException(MESSAGE, CAUSE, CODE);
        assertTrue(base.equals(other));
    }

    @Test
    public void equalsIsFalseForDifferentMessage() throws Exception {
        InvalidCredentialsException base = new InvalidCredentialsException(MESSAGE, CAUSE, CODE);
        InvalidCredentialsException other = new InvalidCredentialsException(null, CAUSE, CODE);
        assertFalse(base.equals(other));
    }

    @Test
    public void equalsIsFalseForDifferentCause() throws Exception {
        InvalidCredentialsException base = new InvalidCredentialsException(MESSAGE, CAUSE, CODE);
        InvalidCredentialsException other = new InvalidCredentialsException(MESSAGE, null, CODE);
        assertFalse(base.equals(other));
    }

    @Test
    public void equalsIsFalseForDifferentErrorCode() throws Exception {
        InvalidCredentialsException base = new InvalidCredentialsException(MESSAGE, CAUSE, CODE);
        InvalidCredentialsException other = new InvalidCredentialsException(MESSAGE, CAUSE, null);
        assertFalse(base.equals(other));
    }

    @Test
    public void hashCodeIsEqualForSameObject() throws Exception {
        InvalidCredentialsException base = new InvalidCredentialsException(MESSAGE, CAUSE, CODE);
        InvalidCredentialsException other = base;
        assertEquals(base.hashCode(), other.hashCode());

    }

    @Test
    public void hashCodeIsEqualForEquivalentObject() throws Exception {
        InvalidCredentialsException base = new InvalidCredentialsException(MESSAGE, CAUSE, CODE);
        InvalidCredentialsException other = new InvalidCredentialsException(MESSAGE, CAUSE, CODE);
        assertEquals(base.hashCode(), other.hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentMessage() throws Exception {
        InvalidCredentialsException base = new InvalidCredentialsException(MESSAGE, CAUSE, CODE);
        InvalidCredentialsException other = new InvalidCredentialsException(null, CAUSE, CODE);
        assertNotEquals(base.hashCode(), other.hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentCause() throws Exception {
        InvalidCredentialsException base = new InvalidCredentialsException(MESSAGE, CAUSE, CODE);
        InvalidCredentialsException other = new InvalidCredentialsException(MESSAGE, null, CODE);
        assertNotEquals(base.hashCode(), other.hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentErrorCode() throws Exception {
        InvalidCredentialsException base = new InvalidCredentialsException(MESSAGE, CAUSE, CODE);
        InvalidCredentialsException other = new InvalidCredentialsException(MESSAGE, CAUSE, null);
        assertNotEquals(base.hashCode(), other.hashCode());
    }

    @Test
    public void toStringContainsClassName() throws Exception {
        assertTrue(new InvalidCredentialsException(MESSAGE, CAUSE, CODE).toString().contains(InvalidCredentialsException.class.getSimpleName()));
    }
}
