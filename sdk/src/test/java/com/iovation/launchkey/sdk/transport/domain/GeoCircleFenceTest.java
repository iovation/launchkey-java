package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GeoCircleFenceTest {

    @Test
    public void fullObjectMapperMapsAsExpected() throws Exception {
        String expected = "{\"type\":\"GEO_CIRCLE\",\"name\":\"a GeoCircle Fence\",\"latitude\":1.0," +
                "\"longitude\":1.0,\"radius\":1.0}";
        GeoCircleFence geoCircleFence = new GeoCircleFence("a GeoCircle Fence", 1,1,1);
        String actual = new ObjectMapper().writeValueAsString(geoCircleFence);
        assertEquals(expected,actual);
    }

    @Test
    public void getFenceName() throws Exception {
        GeoCircleFence geoCircleFence = new GeoCircleFence("a GeoCircle Fence", 1,1,1);
        assertEquals(geoCircleFence.getFenceName(), "a GeoCircle Fence");
    }

    @Test
    public void getLatitude() throws Exception {
        GeoCircleFence geoCircleFence = new GeoCircleFence("a GeoCircle Fence", 59,1,1);
        assertEquals(geoCircleFence.getLatitude(), 59,0.0001);
    }

    @Test
    public void getLongitude() throws Exception {
        GeoCircleFence geoCircleFence = new GeoCircleFence("a GeoCircle Fence", 1,23,1);
        assertEquals(geoCircleFence.getLongitude(), 23,0.0001);
    }

    @Test
    public void getRadius() throws Exception {
        GeoCircleFence geoCircleFence = new GeoCircleFence("a GeoCircle Fence", 1,23,88);
        assertEquals(geoCircleFence.getRadius(), 88,0.0001);
    }

    @Test
    public void getType() throws Exception {
        GeoCircleFence geoCircleFence = new GeoCircleFence("a GeoCircle Fence", 1,23,88);
        assertEquals(geoCircleFence.getType(), "GEO_CIRCLE");
    }
}
