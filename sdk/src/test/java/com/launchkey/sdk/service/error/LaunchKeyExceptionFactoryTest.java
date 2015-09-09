package com.launchkey.sdk.service.error;

import com.launchkey.sdk.service.error.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@RunWith(Parameterized.class)
public class LaunchKeyExceptionFactoryTest {

    private final int code;
    private final Class<LaunchKeyException> expectedException;

    public LaunchKeyExceptionFactoryTest(
            int code, Class<LaunchKeyException> expectedException
    ) {
        this.code = code;
        this.expectedException = expectedException;
    }

    @Parameterized.Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(
                new Object[][]{
                        // Unknown code is base
                        {0, LaunchKeyException.class},

                        // auths codes
                        {40421, InvalidRequestException.class},
                        {40422, InvalidCredentialsException.class},
                        {40423, InvalidCredentialsException.class},
                        {40424, NoPairedDevicesException.class},
                        {40425, InvalidCredentialsException.class},
                        {40426, NoSuchUserException.class},
                        {40428, InvalidSignatureException.class},
                        {40429, InvalidCredentialsException.class},
                        {40431, ExpiredAuthRequestException.class},
                        {40432, InvalidSignatureException.class},
                        {40433, InvalidRequestException.class},
                        {40434, InvalidRequestException.class},
                        {40435, InvalidCredentialsException.class},
                        {40436, RateLimitExceededException.class},
                        {40437, InvalidRequestException.class},

                        // logs codes
                        {50441, InvalidRequestException.class},
                        {50442, InvalidCredentialsException.class},
                        {50443, InvalidCredentialsException.class},
                        {50444, InvalidCredentialsException.class},
                        {50445, InvalidCredentialsException.class},
                        {50446, InvalidRequestException.class},
                        {50447, InvalidCredentialsException.class},
                        {50448, InvalidSignatureException.class},
                        {50449, InvalidCredentialsException.class},
                        {50451, ExpiredAuthRequestException.class},
                        {50452, InvalidSignatureException.class},
                        {50453, InvalidSignatureException.class},
                        {50454, InvalidRequestException.class},
                        {50455, InvalidRequestException.class},
                        {50456, InvalidRequestException.class},
                        {50457, InvalidRequestException.class},

                        // ping codes
                        {60401, InvalidRequestException.class},

                        // poll codes
                        {70401, InvalidRequestException.class},
                        {70402, InvalidRequestException.class},
                        {70404, ExpiredAuthRequestException.class},
                }
        );

    }

    @Test
    public void testFromCodeReturnsCorrectExceptionType() throws Exception {
        LaunchKeyException actual = LaunchKeyException.fromCode(code, null);
        assertEquals(expectedException, actual.getClass());
    }

    @Test
    public void testFromCodeReturnsExceptionWithCorrectCode() throws Exception {
        LaunchKeyException actual = LaunchKeyException.fromCode(code, null);
        assertEquals(code, actual.getCode());
    }

    @Test
    public void testFromCodeReturnsExceptionWithCorrectMessage() throws Exception {
        LaunchKeyException actual = LaunchKeyException.fromCode(code, "Expected Message");
        assertEquals("Expected Message", actual.getMessage());
    }
}