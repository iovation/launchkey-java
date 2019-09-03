package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iovation.launchkey.sdk.domain.policy.Factor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PolicyTest {
    @Test
    public void fullObjectMapperMapsAsExpected() throws Exception {

        String expected = "{\"type\":\"CON_GEO\",\"deny_rooted_jailbroken\":true,\"deny_emulator_simulator\":true," +
                "\"fences\":[{\"name\":\"a GeoCircle Fence\",\"type\":\"GEO_CIRCLE\",\"latitude\":1.0,\"longitude\":1.0," +
                "\"radius\":1.0},{\"name\":\"a Territory Fence\",\"type\":\"TERRITORY\",\"country\":\"country\"," +
                "\"administrative_area\":\"Admin Area\",\"postal_code\":\"ABCDE6\"}]," +
                "\"inside\":{\"deny_rooted_jailbroken\":false,\"deny_emulator_simulator\":false," +
                "\"factors\":[\"KNOWLEDGE\"]},\"outside\":{\"deny_rooted_jailbroken\":false," +
                "\"deny_emulator_simulator\":false,\"factors\":[\"INHERENCE\",\"POSSESSION\"]}}";

        List<String> inPolicyFactors = new ArrayList<>();
        inPolicyFactors.add(Factor.KNOWLEDGE.toString());

        List<String> outPolicyFactors = new ArrayList<>();
        outPolicyFactors.add(Factor.INHERENCE.toString());
        outPolicyFactors.add(Factor.POSSESSION.toString());

        List<Fence> fences = new ArrayList<>();
        Fence geoCircleFence = new GeoCircleFence("a GeoCircle Fence", 1,1,1);
        Fence territorialFence = new TerritoryFence("a Territory Fence", "country", "Admin Area", "ABCDE6");
        fences.add(geoCircleFence);
        fences.add(territorialFence);

        Policy inPolicy = new Policy(false,false,null,null,null,null,inPolicyFactors);
        Policy outPolicy = new Policy(false,false,null,null,null,null,outPolicyFactors);
        Policy policy = new Policy(true,true,fences,inPolicy,outPolicy,"CON_GEO",null);

        String actual = new ObjectMapper().writeValueAsString(policy);
        assertEquals(expected,actual);
    }

}
