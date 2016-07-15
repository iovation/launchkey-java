package com.launchkey.sdk.service.organization.whitelabel;

import com.launchkey.sdk.transport.v3.Transport;
import com.launchkey.sdk.transport.v3.domain.WhiteLabelDeviceDeleteRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.security.interfaces.RSAPrivateKey;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
public class V3WhiteLabelServiceFactoryTest {
    private static final long ORG_KEY = 12345L;
    private static final String SDK_KEY = "SDK Key";
    private Transport transport;
    private RSAPrivateKey privateKey;
    private V3WhiteLabelServiceFactory factory;

    @Before
    public void setUp() throws Exception {
        transport = mock(Transport.class);
        privateKey = mock(RSAPrivateKey.class);
        factory = new V3WhiteLabelServiceFactory(transport, privateKey, ORG_KEY);
    }

    @After
    public void tearDown() throws Exception {
        transport = null;
        privateKey = null;
        factory = null;
    }

    @Test
    public void getServiceUsesProvidedTransport() throws Exception {
        WhiteLabelService service = factory.getService(SDK_KEY);
        service.unlinkDevice("identifier", "device");
        verify(transport).whiteLabelUserDeviceDelete(any(WhiteLabelDeviceDeleteRequest.class));
    }

    @Test
    public void getServiceUsesProvidedSdkKey() throws Exception {
        WhiteLabelService service = factory.getService(SDK_KEY);
        service.unlinkDevice("identifier", "device");
        ArgumentCaptor<WhiteLabelDeviceDeleteRequest> captor = ArgumentCaptor.forClass(WhiteLabelDeviceDeleteRequest.class);
        verify(transport).whiteLabelUserDeviceDelete(captor.capture());
        assertEquals(SDK_KEY, captor.getValue().getSdkKey());
    }

    @Test
    public void getServiceUsesProvidedPrivateKey() throws Exception {
        WhiteLabelService service = factory.getService(SDK_KEY);
        service.unlinkDevice("identifier", "device");
        ArgumentCaptor<WhiteLabelDeviceDeleteRequest> captor = ArgumentCaptor.forClass(WhiteLabelDeviceDeleteRequest.class);
        verify(transport).whiteLabelUserDeviceDelete(captor.capture());
        assertEquals(privateKey, captor.getValue().getPrivateKey());
    }
}
