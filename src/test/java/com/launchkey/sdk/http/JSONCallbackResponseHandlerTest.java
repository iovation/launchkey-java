package com.launchkey.sdk.http;

import com.sun.org.apache.xpath.internal.operations.*;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.lang.String;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JSONCallbackResponseHandlerTest {

    private final static String JSON_STRING = "{ \"foo\": \"bar\" }";

    @Mock
    private JSONHttpCallback jsonHttpCallback;

    @Mock
    private HttpResponse httpResponse;

    @Mock
    private StatusLine statusLine;

    @Mock
    private HttpEntity httpEntity;

    private HttpController.JSONCallbackResponseHandler jsonCallbackResponseHandler;

    @Before
    public void setUp() throws Exception {
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        when(httpEntity.getContent()).thenReturn(new ByteArrayInputStream(JSON_STRING.getBytes()));
        when(statusLine.getStatusCode()).thenReturn(200);
        jsonCallbackResponseHandler = new HttpController.JSONCallbackResponseHandler(jsonHttpCallback);
    }

    @After
    public void tearDown() {
        jsonCallbackResponseHandler = null;
    }

    @Test
    public void testCallbackReturnsStatusCodeFromResponse() throws Exception {
        int code = jsonCallbackResponseHandler.handleResponse(httpResponse);
        assertEquals(200, code);
    }

    @Test
    public void testCallbackCallsOnSuccess() throws Exception {
        jsonCallbackResponseHandler.handleResponse(httpResponse);
        verify(jsonHttpCallback).onSuccess(any(JSONObject.class));
    }

    @Test
    public void testCallbackCallsOnSuccessWithTheProperJsonObject() throws Exception {
        JSONObject expected = JSONObject.fromObject(JSON_STRING);
        ArgumentCaptor<JSONObject> captor = ArgumentCaptor.forClass(JSONObject.class);
        jsonCallbackResponseHandler.handleResponse(httpResponse);
        verify(jsonHttpCallback).onSuccess(captor.capture());
        assertEquals(expected, captor.getValue());
    }
}