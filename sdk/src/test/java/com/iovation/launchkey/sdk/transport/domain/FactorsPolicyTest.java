package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FactorsPolicyTest {

//    @Test
//    public void fullObjectMapperMapsAsExpected() throws Exception {
//
//        String expected = "{\"type\":\"FACTORS\",\"deny_rooted_jailbroken\":false,\"deny_emulator_simulator\":false," +
//                "\"fences\":[{\"type\":\"GEO_CIRCLE\",\"name\":\"a GeoCircle Fence\",\"latitude\":1.0," +
//                "\"longitude\":1.0,\"radius\":1.0},{\"type\":\"TERRITORY\",\"name\":\"a Territory Fence\"," +
//                "\"country\":\"country\",\"administrative_area\":\"Admin Area\",\"postal_code\":\"ABCDE6\"}]," +
//                "\"factors\":[\"INHERENCE\",\"POSSESSION\"]}";
//
//        List<String> factors = new ArrayList<>();
//        factors.add(Factor.INHERENCE.toString());
//        factors.add(Factor.POSSESSION.toString());
//
//        List<Fence> fences = new ArrayList<>();
//        Fence geoCircleFence = new GeoCircleFence("a GeoCircle Fence", 1,1,1);
//        Fence territorialFence = new TerritoryFence("a Territory Fence", "country", "Admin Area", "ABCDE6");
//        fences.add(geoCircleFence);
//        fences.add(territorialFence);
//
//        Policy policy = new FactorsPolicy(false,false,fences,factors);
//        String actual = new ObjectMapper().writeValueAsString(policy);
//
//        assertEquals(expected,actual);
//    }

    @Test
    public void getPolicyType() {
        FactorsPolicy policy = new FactorsPolicy(null,null,null,null);
        assertEquals(policy.getPolicyType(),"FACTORS");
    }

    @Test
    public void getDenyRootedJailbroken() {
        Boolean expected = true;
        FactorsPolicy policy = new FactorsPolicy(expected,null,null,null);
        assertEquals(policy.getDenyRootedJailbroken(),expected);
    }

    @Test
    public void getDenyEmulatorSimulator() {
        Boolean expected = true;
        FactorsPolicy policy = new FactorsPolicy(null,expected,null,null);
        assertEquals(policy.getDenyEmulatorSimulator(),expected);
    }

    @Test
    public void getFences() {
        List<Fence> expected = new ArrayList<>();
        Fence geoCircleFence = new GeoCircleFence("a GeoCircle Fence", 1,1,1);
        Fence territorialFence = new TerritoryFence("a Territory Fence", "country", "Admin Area", "ABCDE6");
        expected.add(geoCircleFence);
        expected.add(territorialFence);
        FactorsPolicy policy = new FactorsPolicy(null,null,expected,null);
        assertEquals(policy.getFences(),expected);
    }

//    @Test
//    public void getFactors() {
//        List<String> expected = new ArrayList<>();
//        expected.add(Factor.INHERENCE.toString());
//        expected.add(Factor.POSSESSION.toString());
//        FactorsPolicy policy = new FactorsPolicy(null,null,null,expected);
//        assertEquals(policy.getFactors(),expected);
//    }
}
