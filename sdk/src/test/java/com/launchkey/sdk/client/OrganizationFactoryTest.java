package com.launchkey.sdk.client;

import com.launchkey.sdk.transport.Transport;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.internal.matchers.InstanceOf;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class OrganizationFactoryTest {

    private OrganizationFactory organizationFactory;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        UUID uuid = UUID.randomUUID();
        Transport transport = mock(Transport.class);
        organizationFactory = new OrganizationFactory(transport, uuid);
    }

    @After
    public void tearDown() throws Exception {
        organizationFactory = null;
    }

    @Test
    public void testMakeDirectoryClientWithNullThrowsIllegalArgument() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        organizationFactory.makeDirectoryClient(null);
    }

    @Test
    public void testMakeDirectoryClientWithInvalidUUIDThrowsIllegalArgument() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        organizationFactory.makeDirectoryClient("Not A UUID");
    }

    @Test
    public void testMakeDirectoryClientWithValidUUIDReturnsClient() throws Exception {
        assertThat(
                organizationFactory.makeDirectoryClient(UUID.randomUUID().toString()),
                new InstanceOf(DirectoryClient.class)
        );
    }

    @Test
    public void testMakeServiceClientWithNullThrowsIllegalArgument() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        organizationFactory.makeServiceClient(null);
    }

    @Test
    public void testMakeServiceClientWithInvalidUUIDThrowsIllegalArgument() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        organizationFactory.makeServiceClient("Not A UUID");
    }

    @Test
    public void testMakeServiceClientWithValidUUIDReturnsClient() throws Exception {
        assertThat(
                organizationFactory.makeServiceClient(UUID.randomUUID().toString()),
                new InstanceOf(ServiceClient.class));
    }


}
