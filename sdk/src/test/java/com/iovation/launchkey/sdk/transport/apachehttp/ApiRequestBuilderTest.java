package com.iovation.launchkey.sdk.transport.apachehttp;

import com.iovation.launchkey.sdk.error.CryptographyError;
import com.iovation.launchkey.sdk.error.MarshallingError;
import junit.framework.TestCase;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ApiRequestBuilderTest extends TestCase {

    private ApiRequestBuilder apiRequestBuilder;

    @Before
    public void setUp() throws Exception {
        this.apiRequestBuilder = new ApiRequestBuilder(null,null,null, null, null, null, null, null, null);
    }

    //NOTE: This test has an assertion utilizing a null because one cannot get the value unless it is inside the built
    @Test
    public void testBuildAddsProperUserAgentHeader() throws MarshallingError, CryptographyError {
        HttpUriRequest request = this.apiRequestBuilder.build(null);
        assertEquals("JavaServiceSDK/null", request.getFirstHeader("User-Agent").getValue());
    }
}