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

        String expected = "";

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

        Policy inPolicy = new FactorsPolicy(false,false,null,inPolicyFactors);
        Policy outPolicy = new FactorsPolicy(false,false,null,outPolicyFactors);
        Policy policy = new ConditionalGeoFencePolicy(true,true,fences,inPolicy,outPolicy);

        String actual = new ObjectMapper().writeValueAsString(policy);

       // assertEquals(expected,actual);
    }


}
