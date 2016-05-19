package com.launchkey.sdk.transport.v1.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchkey.sdk.transport.v1.domain.UsersRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
public class UsersRequestTest {

    private UsersRequest usersRequest;

    @Before
    @SuppressWarnings("SpellCheckingInspection")
    public void setUp() throws Exception {
        usersRequest = new UsersRequest(
                "a714bb0c-526f-4100-9e45-b8297fb4cc3d",
                9999999999L,
                "gJQw0unVnDkpSCFMmlAMuzcAP1HpmLQxecN7PyWpnbtlxEsCmWTconAZaZ5vjp+24gLX4A5SiFIT3srRNtg/v9gDfB2jaMZbXi" +
                        "oNJIiwG58uMaDKL7bNJenLn1Du9/TSFXuju3TALdgEeeH0SW0KW+w6A6Aa/PRhvUfCr4lJ0/AzByrMDkdaIWoP3sxp" +
                        "zV8Bc+L2C2xw/J5tIs33ryzj+bYXIpvh8yrDhtJCyQZKKvSiVZWMlS14uCQvUI78DRXMTKVEJG2KkObet5ov2fY0ys" +
                        "BFW2BT4Vdwh3wUs9SbMBsz21hjS1YnKvIhvwTB8CByczR8EUu57Hr74XSYjhlUvQ=="
        );
    }

    @After
    public void tearDown() throws Exception {
        usersRequest = null;
    }

    @Test
    public void testGetIdentifier() throws Exception {
        assertEquals("a714bb0c-526f-4100-9e45-b8297fb4cc3d", usersRequest.getIdentifier());
    }

    @Test
    public void testGetAppKey() throws Exception {
        assertEquals(9999999999L, usersRequest.getAppKey());
    }

    @Test
    @SuppressWarnings("SpellCheckingInspection")
    public void testGetSecretKey() throws Exception {
        assertEquals("gJQw0unVnDkpSCFMmlAMuzcAP1HpmLQxecN7PyWpnbtlxEsCmWTconAZaZ5vjp+24gLX4A5SiFIT3srRNtg/v9gDfB2jaMZbXi" +
                "oNJIiwG58uMaDKL7bNJenLn1Du9/TSFXuju3TALdgEeeH0SW0KW+w6A6Aa/PRhvUfCr4lJ0/AzByrMDkdaIWoP3sxp" +
                "zV8Bc+L2C2xw/J5tIs33ryzj+bYXIpvh8yrDhtJCyQZKKvSiVZWMlS14uCQvUI78DRXMTKVEJG2KkObet5ov2fY0ys" +
                "BFW2BT4Vdwh3wUs9SbMBsz21hjS1YnKvIhvwTB8CByczR8EUu57Hr74XSYjhlUvQ==",
            usersRequest.getSecretKey());
    }

    @Test
    public void testJSONEncode() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        String expected = "{\"app_key\":9999999999,\"secret_key\":\"gJQw0unVnDkpSCFMmlAMuzcAP1HpmLQxecN7PyWpnbtl" +
                "xEsCmWTconAZaZ5vjp+24gLX4A5SiFIT3srRNtg/v9gDfB2jaMZbXioNJIiwG58uMaDKL7bNJenLn1Du9/TSFXuju3TALdgEee" +
                "H0SW0KW+w6A6Aa/PRhvUfCr4lJ0/AzByrMDkdaIWoP3sxpzV8Bc+L2C2xw/J5tIs33ryzj+bYXIpvh8yrDhtJCyQZKKvSiVZWMl" +
                "S14uCQvUI78DRXMTKVEJG2KkObet5ov2fY0ysBFW2BT4Vdwh3wUs9SbMBsz21hjS1YnKvIhvwTB8CByczR8EUu57Hr74XSYjhlU" +
                "vQ==\",\"identifier\":\"a714bb0c-526f-4100-9e45-b8297fb4cc3d\"}";

        ObjectMapper mapper = new ObjectMapper();
        String actual = mapper.writeValueAsString(usersRequest);
        assertEquals(expected, actual);

    }
}
