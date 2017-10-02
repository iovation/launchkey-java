package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.transport.Transport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;

public class OrganizationFactoryTest {

    private OrganizationFactory organizationFactory;

    @Before
    public void setUp() throws Exception {
        UUID uuid = UUID.fromString("49af9c38-31b3-11e7-93ae-92361f002671");
        Transport transport = mock(Transport.class);
        organizationFactory = new OrganizationFactory(transport, uuid);
    }

    @After
    public void tearDown() throws Exception {
        organizationFactory = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMakeDirectoryClientWithNullThrowsIllegalArgument() throws Exception {
        organizationFactory.makeDirectoryClient(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMakeDirectoryClientWithInvalidUUIDThrowsIllegalArgument() throws Exception {
        organizationFactory.makeDirectoryClient("Not A UUID");
    }

    @Test
    public void testMakeDirectoryClientWithValidUUIDReturnsClient() throws Exception {
        assertThat(
                organizationFactory.makeDirectoryClient("49af9c38-31b3-11e7-93ae-92361f002671"),
                instanceOf(DirectoryClient.class)
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMakeServiceClientWithNullThrowsIllegalArgument() throws Exception {
        organizationFactory.makeServiceClient(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMakeServiceClientWithInvalidUUIDThrowsIllegalArgument() throws Exception {
        organizationFactory.makeServiceClient("Not A UUID");
    }

    @Test
    public void testMakeServiceClientWithValidUUIDReturnsClient() throws Exception {
        assertThat(organizationFactory.makeServiceClient("49af9c38-31b3-11e7-93ae-92361f002671"),
                instanceOf(ServiceClient.class));
    }
}
