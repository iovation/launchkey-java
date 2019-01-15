package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class ErrorTest {

    private ObjectMapper mapper;
    private Object errorData;
    private Object errorDetail;
    private Error error;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        errorDetail = new Object();
        errorData = new Object();
        error = new Error("Error Code", errorDetail, errorData);
    }

    @Test
    public void getErrorCode() {
        assertEquals("Error Code", error.getErrorCode());
    }

    @Test
    public void getErrorDetail() {
        assertSame(errorDetail, error.getErrorDetail());
    }

    @Test
    public void getErrorData() {
        assertSame(errorData, error.getErrorData());
    }

    @Test
    public void objectMapperPopulatesErrorCode() throws Exception {
        String json = "{\"error_code\":\"Error Code\"}";
        Error error = mapper.readValue(json, Error.class);
        assertEquals("Error Code", error.getErrorCode());
    }

    @Test
    public void objectMapperPopulatesErrorDetailWhenString() throws Exception {
        String json = "{\"error_code\":\"Error Code\",\"error_detail\":\"Error Detail\"}";
        Error error = mapper.readValue(json, Error.class);
        assertEquals("Error Detail", error.getErrorDetail());
    }

    @Test
    public void objectMapperPopulatesErrorDetailWhenObject() throws Exception {
        String json = "{\"error_code\":\"Error Code\",\"error_detail\":{\"one\":1,\"two\":2.0,\"three\":\"three\"}}";
        Error error = mapper.readValue(json, Error.class);
        Map<Object, Object> detail = (Map<Object, Object>) error.getErrorDetail();
        assertEquals(1, detail.get("one"));
        assertEquals(2.0, detail.get("two"));
        assertEquals("three", detail.get("three"));
    }

    @Test
    public void objectMapperPopulatesErrorData() throws Exception {
        String json =
                "{" +
                    "\"error_code\": \"SVC-005\"," +
                    "\"error_detail\": \"An auth already exists.\"," +
                    "\"error_data\": {" +
                        "\"auth_request\": \"adc0d351-d8a8-11e8-9fe8-acde48001122\"," +
                        "\"my_auth\": true," +
                        "\"expires\": \"2018-11-28T22:04:44Z\"" +
                    "}" +
                "}";
        Error error = mapper.readValue(json, Error.class);
        Map<Object, Object> data = (Map<Object, Object>) error.getErrorData();
        assertEquals("adc0d351-d8a8-11e8-9fe8-acde48001122", data.get("auth_request"));
        assertEquals(true, data.get("my_auth"));
        assertEquals("2018-11-28T22:04:44Z", data.get("expires"));
    }

    @Test
    public void objectMapperIgnoresUnknown() throws Exception {
        String json = "{\"error_code\":\"Error Code\",\"error_detail\":{\"one\":1,\"two\":2.0,\"three\":\"three\"},\"unknown\":null}";
        Error error = mapper.readValue(json, Error.class);
        assertNotNull(error.getErrorDetail());
    }

}