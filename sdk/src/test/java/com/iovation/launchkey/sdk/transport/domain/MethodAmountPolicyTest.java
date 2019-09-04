package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MethodAmountPolicyTest {

    @Test
    public void fullObjectMapperMapsAsExpected() throws Exception {

        String expected = "{\"type\":\"METHOD_AMOUNT\",\"deny_rooted_jailbroken\":true," +
                "\"deny_emulator_simulator\":true,\"fences\":[{\"type\":\"GEO_CIRCLE\"," +
                "\"name\":\"a GeoCircle Fence\",\"latitude\":1.0,\"longitude\":1.0,\"radius\":1.0}," +
                "{\"type\":\"TERRITORY\",\"name\":\"a Territory Fence\",\"country\":\"country\"," +
                "\"administrative_area\":\"Admin Area\",\"postal_code\":\"ABCDE6\"}],\"amount\":5}";

        List<Fence> fences = new ArrayList<>();
        Fence geoCircleFence = new GeoCircleFence("a GeoCircle Fence", 1,1,1);
        Fence territorialFence = new TerritoryFence("a Territory Fence", "country", "Admin Area", "ABCDE6");
        fences.add(geoCircleFence);
        fences.add(territorialFence);
        Policy policy = new MethodAmountPolicy(true,true,fences,5);

        String actual = new ObjectMapper().writeValueAsString(policy);

        assertEquals(expected,actual);
    }
}
