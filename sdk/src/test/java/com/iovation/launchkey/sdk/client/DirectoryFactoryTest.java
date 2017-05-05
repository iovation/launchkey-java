package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.transport.Transport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.InstanceOf;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class DirectoryFactoryTest {

    private DirectoryFactory directoryFactory;

    @Before
    public void setUp() throws Exception {
        UUID uuid = UUID.fromString("49af9c38-31b3-11e7-93ae-92361f002671");
        Transport transport = mock(Transport.class);
        directoryFactory = new DirectoryFactory(transport, uuid);
    }

    @After
    public void tearDown() throws Exception {
        directoryFactory = null;
    }

    @Test
    public void testMakeDirectoryClientReturnsClient() throws Exception {
        assertThat(
                directoryFactory.makeDirectoryClient(),
                new InstanceOf(DirectoryClient.class)
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMakeServiceClientWithNullThrowsIllegalArgument() throws Exception {
        directoryFactory.makeServiceClient(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMakeServiceClientWithInvalidUUIDThrowsIllegalArgument() throws Exception {
        directoryFactory.makeServiceClient("Not A UUID");
    }

    @Test
    public void testMakeServiceClientWithValidUUIDReturnsClient() throws Exception {
        assertThat(
                directoryFactory.makeServiceClient("49af9c38-31b3-11e7-93ae-92361f002671"),
                new InstanceOf(ServiceClient.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMakeServiceClientWithNonUUID1ThrowsIllegalArgument() throws Exception {
        directoryFactory.makeServiceClient(UUID.randomUUID().toString());
    }

}
