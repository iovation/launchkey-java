package com.launchkey.sdk;

import com.launchkey.sdk.service.organization.whitelabel.WhiteLabelService;
import com.launchkey.sdk.service.organization.whitelabel.WhiteLabelServiceFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

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
public class BasicOrgClientTest {

    private WhiteLabelService whiteLabelService;
    private OrgClient orgClient;
    private WhiteLabelServiceFactory whiteLabelServiceFactory;

    @Before
    public void setUp() throws Exception {
        whiteLabelServiceFactory = mock(WhiteLabelServiceFactory.class);
        whiteLabelService = mock(WhiteLabelService.class);
        when(whiteLabelServiceFactory.getService(anyString())).thenReturn(whiteLabelService);
        orgClient = new BasicOrgClient(whiteLabelServiceFactory);
    }

    @After
    public void tearDown() throws Exception {
        orgClient = null;
        whiteLabelService = null;
        whiteLabelServiceFactory = null;
    }

    @Test
    public void testWhiteLabelReturnsExpectedWhiteLabelService() throws Exception {
        assertSame(whiteLabelService, orgClient.whiteLabel(null));
    }

    @Test
    public void testWhiteLabelPassesExpectedSdkKeyToWhiteLabelServicedFactory() throws Exception {
        String expected = "Expected SDK Key";
        orgClient.whiteLabel(expected);
        verify(whiteLabelServiceFactory).getService(expected);
    }
}
