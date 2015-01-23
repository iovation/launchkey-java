package com.launchkey.sdk.http;

import org.apache.http.client.HttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class HttpControllerTest {

    private HttpController httpController;

    @Mock
    private HttpClient httpClient;

    @Before
    public void setUp() {
        httpController = new HttpController(httpClient);
    }

    @After
    public void tearDown() {
        httpController = null;
    }

    @Test
    public void testSetGetAppKey() throws Exception {
        String expected = "AppKey";
        httpController.setAppKey(expected);
        assertEquals(expected, httpController.getAppKey());
    }

    @Test
    public void testSetGetSecretKey() throws Exception {
        String expected = "SecretKey";
        httpController.setSecretKey(expected);
        assertEquals(expected, httpController.getSecretKey());
    }

    @Test
    public void testSetGetPrivateKey() throws Exception {
        String expected = "PrivateKey";
        httpController.setPrivateKey(expected);
        assertEquals(expected, httpController.getPrivateKey());
    }
}