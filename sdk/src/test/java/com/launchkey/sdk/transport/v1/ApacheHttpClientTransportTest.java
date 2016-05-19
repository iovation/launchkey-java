package com.launchkey.sdk.transport.v1;

import com.launchkey.sdk.crypto.Crypto;
import org.apache.http.client.HttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

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
public class ApacheHttpClientTransportTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
    private HttpClient httpClient;
    private Crypto crypto;

    @Before
    public void setUp() throws Exception {
        httpClient = mock(HttpClient.class);
        crypto = mock(Crypto.class);
    }

    @After
    public void tearDown() throws Exception {
        httpClient = null;
        crypto = null;
    }

    @Test
    public void testConstructorWithValidUriDoesNotError() throws Exception {
        ApacheHttpClientTransport actual = new ApacheHttpClientTransport(httpClient, "https://test.com", crypto);
        assertThat(actual, instanceOf(ApacheHttpClientTransport.class));
    }

    @Test
    public void testConstructorWithValidUriAndTrailingSlashDoesNotError() throws Exception {
        ApacheHttpClientTransport actual = new ApacheHttpClientTransport(httpClient, "https://test.com/", crypto);
        assertThat(actual, instanceOf(ApacheHttpClientTransport.class));
    }

    @Test
    public void testConstructorWithInvalidUriThrowsIllegalArgumentException() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        new ApacheHttpClientTransport(httpClient, "Invalid URL", crypto);
    }
}
