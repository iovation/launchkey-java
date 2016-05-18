package com.launchkey.sdk.transport.v1.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.containsString;
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
public class PingResponseTest {

    private PingResponse pingResponse;
    private ObjectMapper mapper;

    @SuppressWarnings("SpellCheckingInspection")
    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        mapper.setDateFormat(new ISO8601DateFormat());
        pingResponse = new PingResponse(
                "Expected Fingerprint",
                new Date(1000L),
                "-----BEGIN PUBLIC KEY-----\n\n" +
                        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8zQos4iDSjmUVrFUAg5G\n" +
                        "uhU6GehNKb8MCXFadRWiyLGjtbGZAk8fusQU0Uj9E3o0mne0SYESACkhyK+3M1Er\n" +
                        "bHlwYJHN0PZHtpaPWqsRmNzui8PvPmhm9QduF4KBFsWu1sBw0ibBYsLrua67F/wK\n" +
                        "PaagZRnUgrbRUhQuYt+53kQNH9nLkwG2aMVPxhxcLJYPzQCat6VjhHOX0bgiNt1i\n" +
                        "HRHU2phxBcquOW2HpGSWcpzlYgFEhPPQFAxoDUBYZI3lfRj49gBhGQi32qQ1YiWp\n" +
                        "aFxOB8GA0Ny5SfI67u6w9Nz9Z9cBhcZBfJKdq5uRWjZWslHjBN3emTAKBpAUPNET\n" +
                        "nwIDAQAB\n\n" +
                        "-----END PUBLIC KEY-----\n"
        );
    }

    @After
    public void tearDown() throws Exception {
        pingResponse = null;
    }

    @Test
    public void testGetDateStamp() throws Exception {
        assertNull(pingResponse.getDateStamp());
    }

    @Test
    public void testGetLaunchKeyTime() throws Exception {
        assertEquals(new Date(1000L), this.pingResponse.getLaunchKeyTime());
    }

    @Test
    @SuppressWarnings("SpellCheckingInspection")
    public void testGetPublicKey() throws Exception {
        assertEquals(
                "-----BEGIN PUBLIC KEY-----\n\n" +
                        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8zQos4iDSjmUVrFUAg5G\n" +
                        "uhU6GehNKb8MCXFadRWiyLGjtbGZAk8fusQU0Uj9E3o0mne0SYESACkhyK+3M1Er\n" +
                        "bHlwYJHN0PZHtpaPWqsRmNzui8PvPmhm9QduF4KBFsWu1sBw0ibBYsLrua67F/wK\n" +
                        "PaagZRnUgrbRUhQuYt+53kQNH9nLkwG2aMVPxhxcLJYPzQCat6VjhHOX0bgiNt1i\n" +
                        "HRHU2phxBcquOW2HpGSWcpzlYgFEhPPQFAxoDUBYZI3lfRj49gBhGQi32qQ1YiWp\n" +
                        "aFxOB8GA0Ny5SfI67u6w9Nz9Z9cBhcZBfJKdq5uRWjZWslHjBN3emTAKBpAUPNET\n" +
                        "nwIDAQAB\n\n" +
                        "-----END PUBLIC KEY-----\n",
                pingResponse.getPublicKey()
        );
    }

    @Test
    @SuppressWarnings("SpellCheckingInspection")
    public void testJSONParsable() throws Exception {
        String json = "{\"fingerprint\" : \"Expected Fingerprint\"," +
                "\"api_time\" : \"1970-01-01T00:00:01Z\"," +
                "\"key\" : \"-----BEGIN PUBLIC KEY-----\\n\\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8zQos4iDSjmUVrFUAg5G\\n" +
                "uhU6GehNKb8MCXFadRWiyLGjtbGZAk8fusQU0Uj9E3o0mne0SYESACkhyK+3M1Er\\n" +
                "bHlwYJHN0PZHtpaPWqsRmNzui8PvPmhm9QduF4KBFsWu1sBw0ibBYsLrua67F/wK\\n" +
                "PaagZRnUgrbRUhQuYt+53kQNH9nLkwG2aMVPxhxcLJYPzQCat6VjhHOX0bgiNt1i\\n" +
                "HRHU2phxBcquOW2HpGSWcpzlYgFEhPPQFAxoDUBYZI3lfRj49gBhGQi32qQ1YiWp\\n" +
                "aFxOB8GA0Ny5SfI67u6w9Nz9Z9cBhcZBfJKdq5uRWjZWslHjBN3emTAKBpAUPNET\\n" +
                "nwIDAQAB\\n\\n" +
                "-----END PUBLIC KEY-----\\n\"}";
        PingResponse actual = mapper.readValue(json, PingResponse.class);
        assertEquals(pingResponse, actual);
    }

    @Test
    @SuppressWarnings("SpellCheckingInspection")
    public void testJSONParseAllowsUnknown() throws Exception {
        String json = "{\"fingerprint\" : \"Expected Fingerprint\"," +
                "\"api_time\" : \"1970-01-01T00:00:01Z\"," +
                "\"key\" : \"-----BEGIN PUBLIC KEY-----\\n\\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8zQos4iDSjmUVrFUAg5G\\n" +
                "uhU6GehNKb8MCXFadRWiyLGjtbGZAk8fusQU0Uj9E3o0mne0SYESACkhyK+3M1Er\\n" +
                "bHlwYJHN0PZHtpaPWqsRmNzui8PvPmhm9QduF4KBFsWu1sBw0ibBYsLrua67F/wK\\n" +
                "PaagZRnUgrbRUhQuYt+53kQNH9nLkwG2aMVPxhxcLJYPzQCat6VjhHOX0bgiNt1i\\n" +
                "HRHU2phxBcquOW2HpGSWcpzlYgFEhPPQFAxoDUBYZI3lfRj49gBhGQi32qQ1YiWp\\n" +
                "aFxOB8GA0Ny5SfI67u6w9Nz9Z9cBhcZBfJKdq5uRWjZWslHjBN3emTAKBpAUPNET\\n" +
                "nwIDAQAB\\n\\n" +
                "-----END PUBLIC KEY-----\\n\", \"unknown\": \"Unknown Value\"}";
        ObjectMapper mapper = new ObjectMapper();
        PingResponse actual = mapper.readValue(json, PingResponse.class);
        assertEquals(this.pingResponse, actual);
    }

    @Test
    public void testEqualObjectsReturnTrueForEquals() throws Exception {
        PingResponse left = new PingResponse("Expected Fingerprint", new Date(0L), "public key");
        PingResponse right = new PingResponse("Expected Fingerprint", new Date(0L), "public key");
        assertTrue(left.equals(right));
    }

    @Test
    public void testNotEqualObjectsReturnFalseForEquals() throws Exception {
        PingResponse left = new PingResponse("Expected Fingerprint", new Date(0L), "public key");
        PingResponse right = new PingResponse("Expected Fingerprint", new Date(1L), "public key");
        assertFalse(left.equals(right));
    }

    @Test
    public void testEqualObjectsReturnSameHashCode() throws Exception {
        PingResponse left = new PingResponse("Expected Fingerprint", new Date(0L), "public key");
        PingResponse right = new PingResponse("Expected Fingerprint", new Date(0L), "public key");
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testNotEqualObjectsReturnDifferentHashCode() throws Exception {
        PingResponse left = new PingResponse("Expected Fingerprint", new Date(0L), "public key");
        PingResponse right = new PingResponse("Expected Fingerprint", new Date(1L), "public key");
        assertNotEquals(left.hashCode(), right.hashCode());
    }


    @Test
    public void testToStringContainsClassName() throws Exception {
        PingResponse pingResponse = new PingResponse("Expected Fingerprint", new Date(0L), "public key");
        assertThat(pingResponse.toString(), containsString(PingResponse.class.getSimpleName()));
    }
}