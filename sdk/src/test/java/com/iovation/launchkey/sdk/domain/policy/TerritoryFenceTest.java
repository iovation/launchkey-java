package com.iovation.launchkey.sdk.domain.policy;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TerritoryFenceTest {

    @Test
    public void getFenceName() {
        assertEquals(new TerritoryFence("mr fence", null,null,null).getName(),"mr fence");
    }

    @Test
    public void getCountry() {
        assertEquals(new TerritoryFence(null, "USA",null,null).getCountry(),"USA");
    }

    @Test
    public void getAdministrativeArea() {
        assertEquals(new TerritoryFence(null, null,"space",null).getAdministrativeArea(),"space");
    }

    @Test
    public void getPostalCode() {
        assertEquals(new TerritoryFence(null, null,null,"3").getPostalCode(),"3");
    }
}
