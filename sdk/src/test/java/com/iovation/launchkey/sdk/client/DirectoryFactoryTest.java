package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.transport.Transport;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.internal.matchers.InstanceOf;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class DirectoryFactoryTest {

    private DirectoryFactory directoryFactory;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        UUID uuid = UUID.randomUUID();
        Transport transport = mock(Transport.class);
        directoryFactory = new DirectoryFactory(transport, uuid);
    }

    @After
    public void tearDown() throws Exception {
        directoryFactory = null;
    }

    @Test
    public void testMakeDirectoryClientWithValidUUIDReturnsClient() throws Exception {
        assertThat(
                directoryFactory.makeDirectoryClient(),
                new InstanceOf(DirectoryClient.class)
        );
    }

    @Test
    public void testMakeServiceClientWithNullThrowsIllegalArgument() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        directoryFactory.makeServiceClient(null);
    }

    @Test
    public void testMakeServiceClientWithInvalidUUIDThrowsIllegalArgument() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        directoryFactory.makeServiceClient("Not A UUID");
    }

    @Test
    public void testMakeServiceClientWithValidUUIDReturnsClient() throws Exception {
        assertThat(
                directoryFactory.makeServiceClient(UUID.randomUUID().toString()),
                new InstanceOf(ServiceClient.class));
    }


}
