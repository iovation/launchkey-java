package com.launchkey.sdk.http;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JSONResponseHandlerTest {

    private final static String JSON_STRING = "{ \"foo\": \"bar\" }";

    @Mock
    private HttpResponse httpResponse;

    @Mock
    private StatusLine statusLine;

    @Mock
    private HttpEntity httpEntity;

    private HttpController.JSONResponseHandler jsonResponseHandler;

    @Before
    public void setUp() throws Exception {
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        when(httpEntity.getContent()).thenReturn(new ByteArrayInputStream(JSON_STRING.getBytes()));
        jsonResponseHandler = new HttpController.JSONResponseHandler();
    }

    @After
    public void tearDown() {
        jsonResponseHandler = null;
    }

    @Test
    public void testHandleResponseSetsStatusToTrueForStatusCode200() throws Exception {
        when(statusLine.getStatusCode()).thenReturn(200);
        JSONResponse response = jsonResponseHandler.handleResponse(httpResponse);
        assertTrue(response.isSuccess());
    }

    @Test
    public void testHandleResponseSetsStatusToTrueForStatusCode201() throws Exception {
        when(statusLine.getStatusCode()).thenReturn(201);
        JSONResponse response = jsonResponseHandler.handleResponse(httpResponse);
        assertTrue(response.isSuccess());
    }

    @Test
    public void testHandleResponseSetsStatusToFalseForErrorStatusCode() throws Exception {
        when(statusLine.getStatusCode()).thenReturn(405);
        JSONResponse response = jsonResponseHandler.handleResponse(httpResponse);
        assertFalse(response.isSuccess());
    }

    @Test
    public void testHandleResponseHasProperJsonObject() throws Exception {
        when(statusLine.getStatusCode()).thenReturn(200);
        JSONResponse response = jsonResponseHandler.handleResponse(httpResponse);
        JSONObject expected = JSONObject.fromObject(JSON_STRING);
        assertEquals(expected, response.getJson());
    }
}