package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.domain.policy.Factor;
import com.iovation.launchkey.sdk.error.BaseException;
import com.iovation.launchkey.sdk.transport.domain.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ServiceManagingBaseClientPolicyConversionTest {


    @Test
    public void getDomainPolicyFromTransportPolicyFactorsPolicy() throws Exception {

        // Create constants
        Factor inherence = Factor.INHERENCE;
        Factor possession = Factor.POSSESSION;

        List<Factor> factors = new ArrayList<>();
        factors.add(inherence);
        factors.add(possession);

        List<String> factorsAsStrings = new ArrayList<>();
        factorsAsStrings.add(inherence.toString());
        factorsAsStrings.add(possession.toString());

        String geoCircleFenceName = "a GeoCircle Fence";
        double latitude = 1;
        double longitude = 1;
        double radius = 1;
        String territoryFenceName = "a Territory Fence";
        String country = "country";
        String adminArea = "Admin Area";
        String postalCode = "ABCDE6";

        Fence geoCircleFence = new GeoCircleFence(geoCircleFenceName, latitude,longitude,radius);
        Fence territorialFence = new TerritoryFence(territoryFenceName,country,adminArea,postalCode);

        List<Fence> fences = new ArrayList<>();
        fences.add(geoCircleFence);
        fences.add(territorialFence);

        Boolean denyRootedJailbroken = false;
        Boolean denyEmulatorSimulator = false;

        // Create transport policy using constants
        Policy transportFactorsPolicy = new FactorsPolicy(denyRootedJailbroken,denyEmulatorSimulator,fences,factorsAsStrings);
        ServiceManagingBaseClient client = new ServiceManagingBaseClient();

        // Convert transport to domain policy
        com.iovation.launchkey.sdk.domain.policy.FactorsPolicy domainFactorsPolicy = (com.iovation.launchkey.sdk.domain.policy.FactorsPolicy) client.getDomainPolicyFromTransportPolicy(transportFactorsPolicy);

        // Assert using constants
        assertEquals(domainFactorsPolicy.getDenyEmulatorSimulator(),denyEmulatorSimulator);
        assertEquals(domainFactorsPolicy.getDenyRootedJailbroken(),denyRootedJailbroken);
        assertEquals(domainFactorsPolicy.getFactors(),factors);
        for (com.iovation.launchkey.sdk.domain.policy.Fence fence : domainFactorsPolicy.getFences()) {
            if (fence.getFenceName().equals(geoCircleFenceName)) {
                com.iovation.launchkey.sdk.domain.policy.GeoCircleFence domainGeoCircleFence = (com.iovation.launchkey.sdk.domain.policy.GeoCircleFence) fence;
                assertEquals(domainGeoCircleFence.getRadius(),radius,0.0001);
                assertEquals(domainGeoCircleFence.getLatitude(),latitude,0.0001);
                assertEquals(domainGeoCircleFence.getLongitude(),longitude,0.0001);
            }
            else if (fence.getFenceName().equals(territoryFenceName)) {
                com.iovation.launchkey.sdk.domain.policy.TerritoryFence domainTerritoryFence = (com.iovation.launchkey.sdk.domain.policy.TerritoryFence) fence;
                assertEquals(domainTerritoryFence.getCountry(),country);
                assertEquals(domainTerritoryFence.getAdministrativeArea(),adminArea);
                assertEquals(domainTerritoryFence.getPostalCode(), postalCode);
            }
            else {
                throw new BaseException("This ain't it chief",null,null);
            }
        }
    }

    @Test
    public void getDomainPolicyFromTransportPolicyMethodAmountPolicy() throws Exception {
        // Create constants
        Boolean denyRootedJailbroken = false;
        Boolean denyEmulatorSimulator = false;
        String geoCircleFenceName = "a GeoCircle Fence";
        double latitude = 1;
        double longitude = 1;
        double radius = 1;
        String territoryFenceName = "a Territory Fence";
        String country = "country";
        String adminArea = "Admin Area";
        String postalCode = "ABCDE6";
        int amount = 5;

        Fence geoCircleFence = new GeoCircleFence(geoCircleFenceName, latitude,longitude,radius);
        Fence territorialFence = new TerritoryFence(territoryFenceName,country,adminArea,postalCode);

        List<Fence> fences = new ArrayList<>();
        fences.add(geoCircleFence);
        fences.add(territorialFence);

        ServiceManagingBaseClient client = new ServiceManagingBaseClient();

        // Create transport Policy
        Policy transportMethodAmountPolicy = new MethodAmountPolicy(denyRootedJailbroken,denyEmulatorSimulator,fences,amount);

        // Convert transport policy to domain policy
        com.iovation.launchkey.sdk.domain.policy.MethodAmountPolicy domainMethodAmountPolicy =
                (com.iovation.launchkey.sdk.domain.policy.MethodAmountPolicy)
                        client.getDomainPolicyFromTransportPolicy(transportMethodAmountPolicy);

        // Assert using constants from above
        assertEquals(domainMethodAmountPolicy.getDenyRootedJailbroken(), denyRootedJailbroken);
        assertEquals(domainMethodAmountPolicy.getDenyEmulatorSimulator(), denyEmulatorSimulator);
        assertEquals(domainMethodAmountPolicy.getAmount(), amount);

        for (com.iovation.launchkey.sdk.domain.policy.Fence fence : domainMethodAmountPolicy.getFences()) {
            if (fence.getFenceName().equals(geoCircleFenceName)) {
                com.iovation.launchkey.sdk.domain.policy.GeoCircleFence domainGeoCircleFence =
                        (com.iovation.launchkey.sdk.domain.policy.GeoCircleFence) fence;
                assertEquals(domainGeoCircleFence.getRadius(),radius,0.0001);
                assertEquals(domainGeoCircleFence.getLatitude(),latitude,0.0001);
                assertEquals(domainGeoCircleFence.getLongitude(),longitude,0.0001);
            }
            else if (fence.getFenceName().equals(territoryFenceName)) {
                com.iovation.launchkey.sdk.domain.policy.TerritoryFence domainTerritoryFence =
                        (com.iovation.launchkey.sdk.domain.policy.TerritoryFence) fence;
                assertEquals(domainTerritoryFence.getCountry(),country);
                assertEquals(domainTerritoryFence.getAdministrativeArea(),adminArea);
                assertEquals(domainTerritoryFence.getPostalCode(), postalCode);
            }
            else {
                throw new BaseException("This ain't it chief",null,null);
            }
        }
    }

    @Test
    public void getDomainPolicyFromTransportPolicyConditionalGeofencePolicy() throws Exception {
        // Create constants
        Factor inherence = Factor.INHERENCE;
        Factor possession = Factor.POSSESSION;

        List<Factor> factors = new ArrayList<>();
        factors.add(inherence);
        factors.add(possession);

        List<String> factorsAsStrings = new ArrayList<>();
        factorsAsStrings.add(inherence.toString());
        factorsAsStrings.add(possession.toString());

        String geoCircleFenceName = "a GeoCircle Fence";
        double latitude = 1;
        double longitude = 1;
        double radius = 1;
        String territoryFenceName = "a Territory Fence";
        String country = "country";
        String adminArea = "Admin Area";
        String postalCode = "ABCDE6";

        Fence geoCircleFence = new GeoCircleFence(geoCircleFenceName, latitude,longitude,radius);
        Fence territorialFence = new TerritoryFence(territoryFenceName,country,adminArea,postalCode);

        List<Fence> fences = new ArrayList<>();
        fences.add(geoCircleFence);
        fences.add(territorialFence);

        Boolean denyRootedJailbroken = false;
        Boolean denyEmulatorSimulator = false;
        int amount = 5;

        // Create Transport Policies
        Policy inPolicy = new MethodAmountPolicy(denyEmulatorSimulator,denyRootedJailbroken,null,amount);
        Policy outPolicy = new FactorsPolicy(denyEmulatorSimulator,denyRootedJailbroken,null,factorsAsStrings);
        ServiceManagingBaseClient client = new ServiceManagingBaseClient();
        Policy transportCondGeoPolicy = new ConditionalGeoFencePolicy(denyRootedJailbroken,denyEmulatorSimulator,fences,inPolicy,outPolicy);

        // Do conversion Transport to Domain Policy
        com.iovation.launchkey.sdk.domain.policy.ConditionalGeoFencePolicy domainCondGeoPolicy = (com.iovation.launchkey.sdk.domain.policy.ConditionalGeoFencePolicy) client.getDomainPolicyFromTransportPolicy(transportCondGeoPolicy);

        // Assert Values including sub policies
        assertEquals(domainCondGeoPolicy.getDenyRootedJailbroken(),denyRootedJailbroken);
        assertEquals(domainCondGeoPolicy.getDenyEmulatorSimulator(),denyEmulatorSimulator);

        for (com.iovation.launchkey.sdk.domain.policy.Fence fence : domainCondGeoPolicy.getFences()) {
            if (fence.getFenceName().equals(geoCircleFenceName)) {
                com.iovation.launchkey.sdk.domain.policy.GeoCircleFence domainGeoCircleFence = (com.iovation.launchkey.sdk.domain.policy.GeoCircleFence) fence;
                assertEquals(domainGeoCircleFence.getRadius(),radius,0.0001);
                assertEquals(domainGeoCircleFence.getLatitude(),latitude,0.0001);
                assertEquals(domainGeoCircleFence.getLongitude(),longitude,0.0001);
            }
            else if (fence.getFenceName().equals(territoryFenceName)) {
                com.iovation.launchkey.sdk.domain.policy.TerritoryFence domainTerritoryFence = (com.iovation.launchkey.sdk.domain.policy.TerritoryFence) fence;
                assertEquals(domainTerritoryFence.getCountry(),country);
                assertEquals(domainTerritoryFence.getAdministrativeArea(),adminArea);
                assertEquals(domainTerritoryFence.getPostalCode(), postalCode);
            }
            else {
                throw new BaseException("This ain't it chief",null,null);
            }
        }
        com.iovation.launchkey.sdk.domain.policy.MethodAmountPolicy domainInPolicy = (com.iovation.launchkey.sdk.domain.policy.MethodAmountPolicy) domainCondGeoPolicy.getInPolicy();
        com.iovation.launchkey.sdk.domain.policy.FactorsPolicy domainOutPolicy = (com.iovation.launchkey.sdk.domain.policy.FactorsPolicy) domainCondGeoPolicy.getOutPolicy();

        assertEquals(domainInPolicy.getDenyRootedJailbroken(), denyRootedJailbroken);
        assertEquals(domainInPolicy.getDenyEmulatorSimulator(), denyEmulatorSimulator);
        assertEquals(domainInPolicy.getAmount(), amount);
        assertEquals(domainOutPolicy.getDenyEmulatorSimulator(),denyEmulatorSimulator);
        assertEquals(domainOutPolicy.getDenyRootedJailbroken(),denyRootedJailbroken);
        assertEquals(domainOutPolicy.getFactors(),factors);
        assertNull(domainOutPolicy.getFences());
        assertNull(domainInPolicy.getFences());
    }
}
