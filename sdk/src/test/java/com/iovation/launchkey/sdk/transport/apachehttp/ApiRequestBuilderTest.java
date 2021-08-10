package com.iovation.launchkey.sdk.transport.apachehttp;

import com.iovation.launchkey.sdk.error.CryptographyError;
import com.iovation.launchkey.sdk.error.MarshallingError;
import junit.framework.TestCase;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;

import static org.junit.Assert.*;

public class ApiRequestBuilderTest extends TestCase {

    private ApiRequestBuilder apiRequestBuilder;

    @Before
    public void setUp() throws Exception {
        this.apiRequestBuilder = new ApiRequestBuilder(null,null,null, null, null, null, null, null, null);
    }

    @Test
    public void testBuildAddsProperUserAgentHeader() throws MarshallingError, CryptographyError {
        HttpUriRequest request = this.apiRequestBuilder.build(null);
        String userAgent = request.getFirstHeader("User-Agent").getValue();
        String[] userAgentParts = userAgent.split("/");
        assertThat(userAgent, JUnitMatchers.containsString("JavaServiceSDK/"));
        assertEquals(userAgentParts[0], "JavaServiceSDK");
        assertNotNull(userAgentParts[1]);
    }
}