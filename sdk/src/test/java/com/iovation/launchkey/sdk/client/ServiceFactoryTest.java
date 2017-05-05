package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.transport.Transport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.InstanceOf;

import java.util.UUID;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class ServiceFactoryTest {

    private Transport transport;
    private ServiceFactory serviceFactory;

    @Before
    public void setUp() throws Exception {
        transport = mock(Transport.class);
        serviceFactory = new ServiceFactory(transport, UUID.fromString("49af9c38-31b3-11e7-93ae-92361f002671"));
    }

    @After
    public void tearDown() throws Exception {
        serviceFactory = null;
        transport = null;
    }

    @Test
    public void testMakeServiceClientReturnsServiceClient() throws Exception {
        assertThat(serviceFactory.makeServiceClient(), new InstanceOf(ServiceClient.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nonUUID1ThrowsIllegalArgument() throws Exception {
        new ServiceFactory(transport, UUID.randomUUID());
    }
}
