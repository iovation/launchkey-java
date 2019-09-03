package com.iovation.launchkey.sdk.domain.policy;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GeoCircleFenceTest {

    @Test
    public void getFenceName() {
        assertEquals(new GeoCircleFence("mr fence",1,1,1).getFenceName(),"mr fence");
    }

    @Test
    public void getLatitude() {
        assertEquals(new GeoCircleFence(null,1,1,1).getLatitude(),1, 0.0001);
    }

    @Test
    public void getLongitude() {
        assertEquals(new GeoCircleFence(1,1,1).getLongitude(),1, 0.0001);
    }

    @Test
    public void getRadius() {
        assertEquals(new GeoCircleFence(null,1,1,1).getRadius(),1, 0.0001);
    }

}
