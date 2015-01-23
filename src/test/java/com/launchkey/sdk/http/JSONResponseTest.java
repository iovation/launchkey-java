package com.launchkey.sdk.http;

import net.sf.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSONResponseTest {

    private JSONResponse jsonResponse;

    @Before
    public void setUp() {
        jsonResponse = new JSONResponse();
    }

    @After
    public void tearDown() {
        jsonResponse = null;
    }

    @Test
    public void testSetGetJson() throws Exception {
        JSONObject expected = new JSONObject();
        jsonResponse.setJson(expected);
        JSONObject actual = jsonResponse.getJson();
        assertSame(expected, actual);
    }

    @Test
    public void testSetIsSuccess() throws Exception {
        jsonResponse.setSuccess(true);
        assertTrue(jsonResponse.isSuccess());
    }
}