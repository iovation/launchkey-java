package com.iovation.launchkey.sdk.domain.policy;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TerritoryFenceTest {

    @Test
    public void getFenceName() {
        assertEquals(new TerritoryFence(null,null,null,"mr fence").getFenceName(),"mr fence");
    }

    @Test
    public void getCountry() {
        assertEquals(new TerritoryFence("USA",null,null,null).getCountry(),"USA");
        assertEquals(new TerritoryFence("mr fence 2").getCountry(), "mr fence 2");
    }

    @Test
    public void getAdministrativeArea() {
        assertEquals(new TerritoryFence(null,"space",null,null).getAdministrativeArea(),"space");
    }

    @Test
    public void getPostalCode() {
        assertEquals(new TerritoryFence(null,null,"3",null).getPostalCode(),"3");
    }
}
