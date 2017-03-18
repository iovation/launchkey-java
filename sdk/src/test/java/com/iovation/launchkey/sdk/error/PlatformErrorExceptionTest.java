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
public class PlatformErrorExceptionTest {
    private final String MESSAGE = "Expected Message";
    private final Throwable CAUSE = new Throwable();
    private final String CODE = "Error Code";

    @Test
    public void constructorMessageCauseErrorCode() throws Exception {
        PlatformErrorException exception = new PlatformErrorException(MESSAGE, CAUSE, CODE);
        assertEquals("Unexpected message value", MESSAGE, exception.getMessage());
        assertEquals("Unexpected cause value", CAUSE, exception.getCause());
        assertEquals("Unexpected error code value", CODE, exception.getErrorCode());
    }

    @Test
    public void equalsIsTrueForSameObject() throws Exception {
        PlatformErrorException base = new PlatformErrorException(MESSAGE, CAUSE, CODE);
        PlatformErrorException other = base;
        assertTrue(base.equals(other));
    }

    @Test
    public void equalsIsTrueForEquivalentObject() throws Exception {
        PlatformErrorException base = new PlatformErrorException(MESSAGE, CAUSE, CODE);
        PlatformErrorException other = new PlatformErrorException(MESSAGE, CAUSE, CODE);
        assertTrue(base.equals(other));
    }

    @Test
    public void equalsIsFalseForDifferentMessage() throws Exception {
        PlatformErrorException base = new PlatformErrorException(MESSAGE, CAUSE, CODE);
        PlatformErrorException other = new PlatformErrorException(null, CAUSE, CODE);
        assertFalse(base.equals(other));
    }

    @Test
    public void equalsIsFalseForDifferentCause() throws Exception {
        PlatformErrorException base = new PlatformErrorException(MESSAGE, CAUSE, CODE);
        PlatformErrorException other = new PlatformErrorException(MESSAGE, null, CODE);
        assertFalse(base.equals(other));
    }

    @Test
    public void equalsIsFalseForDifferentErrorCode() throws Exception {
        PlatformErrorException base = new PlatformErrorException(MESSAGE, CAUSE, CODE);
        PlatformErrorException other = new PlatformErrorException(MESSAGE, CAUSE, null);
        assertFalse(base.equals(other));
    }

    @Test
    public void hashCodeIsEqualForSameObject() throws Exception {
        PlatformErrorException base = new PlatformErrorException(MESSAGE, CAUSE, CODE);
        PlatformErrorException other = base;
        assertEquals(base.hashCode(), other.hashCode());

    }

    @Test
    public void hashCodeIsEqualForEquivalentObject() throws Exception {
        PlatformErrorException base = new PlatformErrorException(MESSAGE, CAUSE, CODE);
        PlatformErrorException other = new PlatformErrorException(MESSAGE, CAUSE, CODE);
        assertEquals(base.hashCode(), other.hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentMessage() throws Exception {
        PlatformErrorException base = new PlatformErrorException(MESSAGE, CAUSE, CODE);
        PlatformErrorException other = new PlatformErrorException(null, CAUSE, CODE);
        assertNotEquals(base.hashCode(), other.hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentCause() throws Exception {
        PlatformErrorException base = new PlatformErrorException(MESSAGE, CAUSE, CODE);
        PlatformErrorException other = new PlatformErrorException(MESSAGE, null, CODE);
        assertNotEquals(base.hashCode(), other.hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentErrorCode() throws Exception {
        PlatformErrorException base = new PlatformErrorException(MESSAGE, CAUSE, CODE);
        PlatformErrorException other = new PlatformErrorException(MESSAGE, CAUSE, null);
        assertNotEquals(base.hashCode(), other.hashCode());
    }

    @Test
    public void toStringContainsClassName() throws Exception {
        assertTrue(new PlatformErrorException(MESSAGE, CAUSE, CODE).toString().contains(PlatformErrorException.class.getSimpleName()));
    }
}
