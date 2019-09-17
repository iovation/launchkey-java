package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ConditionalGeoFencePolicyTest {
//    @Test
//    public void fullObjectMapperMapsAsExpected() throws Exception {
//
//        String expected = "{\"type\":\"COND_GEO\",\"deny_rooted_jailbroken\":true,\"deny_emulator_simulator\":true," +
//                "\"fences\":[{\"type\":\"GEO_CIRCLE\",\"name\":\"a GeoCircle Fence\",\"latitude\":1.0," +
//                "\"longitude\":1.0,\"radius\":1.0},{\"type\":\"TERRITORY\",\"name\":\"a Territory Fence\"," +
//                "\"country\":\"country\",\"administrative_area\":\"Admin Area\",\"postal_code\":\"ABCDE6\"}]," +
//                "\"inside\":{\"type\":\"FACTORS\",\"deny_rooted_jailbroken\":false,\"deny_emulator_simulator\":false," +
//                "\"factors\":[\"KNOWLEDGE\"]},\"outside\":{\"type\":\"FACTORS\",\"deny_rooted_jailbroken\":false," +
//                "\"deny_emulator_simulator\":false,\"factors\":[\"INHERENCE\",\"POSSESSION\"]}}";
//
//        List<String> inPolicyFactors = new ArrayList<>();
//        inPolicyFactors.add(Factor.KNOWLEDGE.toString());
//
//        List<String> outPolicyFactors = new ArrayList<>();
//        outPolicyFactors.add(Factor.INHERENCE.toString());
//        outPolicyFactors.add(Factor.POSSESSION.toString());
//
//        List<Fence> fences = new ArrayList<>();
//        Fence geoCircleFence = new GeoCircleFence("a GeoCircle Fence", 1,1,1);
//        Fence territorialFence = new TerritoryFence("a Territory Fence", "country", "Admin Area", "ABCDE6");
//        fences.add(geoCircleFence);
//        fences.add(territorialFence);
//
//        Policy inPolicy = new FactorsPolicy(false,false,null,inPolicyFactors);
//        Policy outPolicy = new FactorsPolicy(false,false,null,outPolicyFactors);
//        Policy policy = new ConditionalGeoFencePolicy(true,true,fences,inPolicy,outPolicy);
//
//        String actual = new ObjectMapper().writeValueAsString(policy);
//
//        assertEquals(expected,actual);
//    }

    @Test
    public void getPolicyType() {
        ConditionalGeoFencePolicy policy = new ConditionalGeoFencePolicy(null,null,null,null,null);
        assertEquals(policy.getPolicyType(),"COND_GEO");
    }

    @Test
    public void getDenyRootedJailbroken() {
        Boolean expected = true;
        ConditionalGeoFencePolicy policy = new ConditionalGeoFencePolicy(expected,null,null,null,null);
        assertEquals(policy.getDenyRootedJailbroken(),expected);
    }

    @Test
    public void getDenyEmulatorSimulator() {
        Boolean expected = true;
        ConditionalGeoFencePolicy policy = new ConditionalGeoFencePolicy(null,expected,null,null,null);
        assertEquals(policy.getDenyEmulatorSimulator(),expected);
    }

    @Test
    public void getFences() {
        List<Fence> expected = new ArrayList<>();
        Fence geoCircleFence = new GeoCircleFence("a GeoCircle Fence", 1,1,1);
        Fence territorialFence = new TerritoryFence("a Territory Fence", "country", "Admin Area", "ABCDE6");
        expected.add(geoCircleFence);
        expected.add(territorialFence);
        ConditionalGeoFencePolicy policy = new ConditionalGeoFencePolicy(null,null,expected,null,null);
        assertEquals(policy.getFences(),expected);
    }

//    @Test
//    public void getInPolicy() {
//        List<String> inPolicyFactors = new ArrayList<>();
//        inPolicyFactors.add(Factor.KNOWLEDGE.toString());
//        Policy expected = new FactorsPolicy(false,false,null,inPolicyFactors);
//        ConditionalGeoFencePolicy policy = new ConditionalGeoFencePolicy(null,null,null,expected,null);
//        assertEquals(policy.getInPolicy(),expected);
//    }

//    @Test
//    public void getOutPolicy() {
//        List<String> outPolicyFactors = new ArrayList<>();
//        outPolicyFactors.add(Factor.INHERENCE.toString());
//        outPolicyFactors.add(Factor.POSSESSION.toString());
//        Policy expected = new FactorsPolicy(false,false,null,outPolicyFactors);
//        ConditionalGeoFencePolicy policy = new ConditionalGeoFencePolicy(null,null,null,null,expected);
//        assertEquals(policy.getOutPolicy(),expected);
//    }


}
