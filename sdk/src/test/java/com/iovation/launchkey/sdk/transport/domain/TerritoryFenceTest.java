package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class TerritoryFenceTest {

    @Test
    public void fullObjectMapperMapsAsExpected() throws Exception {
        String expected = "{\"type\":\"TERRITORY\",\"name\":\"a Territory Fence\",\"country\":\"country\"," +
                "\"administrative_area\":\"Admin Area\",\"postal_code\":\"ABCDE6\"}";
        TerritoryFence territorialFence = new TerritoryFence("a Territory Fence", "country", "Admin Area", "ABCDE6");
        String actual = new ObjectMapper().writeValueAsString(territorialFence);
        assertEquals(expected,actual);
    }

    @Test
    public void getFenceName() throws Exception {
        TerritoryFence territorialFence = new TerritoryFence("a Territory Fence", "country", "Admin Area", "ABCDE6");
        assertEquals(territorialFence.getFenceName(), "a Territory Fence");
    }

    @Test
    public void getType() throws Exception {
        TerritoryFence territorialFence = new TerritoryFence("a Territory Fence", "country", "Admin Area", "ABCDE6");
        assertEquals(territorialFence.getType(), "TERRITORY");
    }

    @Test
    public void getCountry() throws Exception {
        TerritoryFence territorialFence = new TerritoryFence("a Territory Fence", "country", "Admin Area", "ABCDE6");
        assertEquals(territorialFence.getCountry(), "country");
    }

    @Test
    public void getAdministrativeArea() throws Exception {
        TerritoryFence territorialFence = new TerritoryFence("a Territory Fence", "country", "Admin Area", "ABCDE6");
        assertEquals(territorialFence.getAdministrativeArea(), "Admin Area");
    }

    @Test
    public void getPostalCode() throws Exception {
        TerritoryFence territorialFence = new TerritoryFence("a Territory Fence", "country", "Admin Area", "ABCDE6");
        assertEquals(territorialFence.getPostalCode(), "ABCDE6");
    }
}
