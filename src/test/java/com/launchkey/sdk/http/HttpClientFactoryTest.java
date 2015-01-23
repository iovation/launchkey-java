package com.launchkey.sdk.http;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnManagerPNames;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HttpClientFactoryTest {

    private HttpClientFactory httpClientFactory;

    private HttpClient httpClient;

    @Before
    public void setUp() throws Exception {
        httpClientFactory = new HttpClientFactory();
        httpClientFactory.setMaxConnections(100);
        httpClient = httpClientFactory.createClient();
    }

    @After
    public void tearDown() throws Exception {
        httpClientFactory = null;
        httpClient = null;
    }

    @Test
    public void testCreateClientUsesHttps() throws Exception {
        assertNotNull(
            "https not registered in schema registry",
            httpClient.getConnectionManager().getSchemeRegistry().getScheme("https")
        );
    }

    @Test
    public void testCreateClientUsesMaxConnections() throws Exception {
        int actual = httpClient.getParams().getIntParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 0);
        assertEquals(100, actual);
    }

    @Test
    public void testSetGetMaxConnections() throws Exception {
        int expected = 666;
        httpClientFactory.setMaxConnections(expected);
        assertEquals(expected, httpClientFactory.getMaxConnections());
    }
}