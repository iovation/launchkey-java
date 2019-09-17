package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.error.BaseException;
import com.iovation.launchkey.sdk.transport.domain.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ServiceManagingBaseClientPolicyConversionTest {

//    @Test
//    public void getDomainPolicyFromTransportPolicyFactorsPolicy() throws Exception {
//
//        // Create constants
//        Factor inherence = Factor.INHERENCE;
//        Factor possession = Factor.POSSESSION;
//
//        List<Factor> factors = new ArrayList<>();
//        factors.add(inherence);
//        factors.add(possession);
//
//        List<String> factorsAsStrings = new ArrayList<>();
//        factorsAsStrings.add(inherence.toString());
//        factorsAsStrings.add(possession.toString());
//
//        String geoCircleFenceName = "a GeoCircle Fence";
//        double latitude = 1;
//        double longitude = 1;
//        double radius = 1;
//        String territoryFenceName = "a Territory Fence";
//        String country = "country";
//        String adminArea = "Admin Area";
//        String postalCode = "ABCDE6";
//
//        Fence geoCircleFence = new GeoCircleFence(geoCircleFenceName, latitude,longitude,radius);
//        Fence territorialFence = new TerritoryFence(territoryFenceName,country,adminArea,postalCode);
//
//        List<Fence> fences = new ArrayList<>();
//        fences.add(geoCircleFence);
//        fences.add(territorialFence);
//
//        Boolean denyRootedJailbroken = false;
//        Boolean denyEmulatorSimulator = false;
//
//        // Create transport policy using constants
//        Policy transportFactorsPolicy = new FactorsPolicy(denyRootedJailbroken,denyEmulatorSimulator,fences,factorsAsStrings);
//        ServiceManagingBaseClient client = new ServiceManagingBaseClient();
//
//        // Convert transport to domain policy
//        com.iovation.launchkey.sdk.domain.policy.FactorsPolicy domainFactorsPolicy = (com.iovation.launchkey.sdk.domain.policy.FactorsPolicy) client.getDomainPolicyFromTransportPolicy(transportFactorsPolicy);
//
//        // Assert using constants
//        assertEquals(domainFactorsPolicy.getDenyEmulatorSimulator(),denyEmulatorSimulator);
//        assertEquals(domainFactorsPolicy.getDenyRootedJailbroken(),denyRootedJailbroken);
//        assertEquals(domainFactorsPolicy.getFactors(),factors);
//        assertNotNull(domainFactorsPolicy.getFences());
//        for (com.iovation.launchkey.sdk.domain.policy.Fence fence : domainFactorsPolicy.getFences()) {
//            if (fence.getFenceName().equals(geoCircleFenceName)) {
//                com.iovation.launchkey.sdk.domain.policy.GeoCircleFence domainGeoCircleFence = (com.iovation.launchkey.sdk.domain.policy.GeoCircleFence) fence;
//                assertEquals(domainGeoCircleFence.getRadius(),radius,0.0001);
//                assertEquals(domainGeoCircleFence.getLatitude(),latitude,0.0001);
//                assertEquals(domainGeoCircleFence.getLongitude(),longitude,0.0001);
//            }
//            else if (fence.getFenceName().equals(territoryFenceName)) {
//                com.iovation.launchkey.sdk.domain.policy.TerritoryFence domainTerritoryFence = (com.iovation.launchkey.sdk.domain.policy.TerritoryFence) fence;
//                assertEquals(domainTerritoryFence.getCountry(),country);
//                assertEquals(domainTerritoryFence.getAdministrativeArea(),adminArea);
//                assertEquals(domainTerritoryFence.getPostalCode(), postalCode);
//            }
//            else {
//                throw new BaseException("This ain't it chief",null,null);
//            }
//        }
//    }
//
//    @Test
//    public void getDomainPolicyFromTransportPolicyMethodAmountPolicy() throws Exception {
//        // Create constants
//        Boolean denyRootedJailbroken = false;
//        Boolean denyEmulatorSimulator = false;
//        String geoCircleFenceName = "a GeoCircle Fence";
//        double latitude = 1;
//        double longitude = 1;
//        double radius = 1;
//        String territoryFenceName = "a Territory Fence";
//        String country = "country";
//        String adminArea = "Admin Area";
//        String postalCode = "ABCDE6";
//        int amount = 5;
//
//        Fence geoCircleFence = new GeoCircleFence(geoCircleFenceName, latitude,longitude,radius);
//        Fence territorialFence = new TerritoryFence(territoryFenceName,country,adminArea,postalCode);
//
//        List<Fence> fences = new ArrayList<>();
//        fences.add(geoCircleFence);
//        fences.add(territorialFence);
//
//        ServiceManagingBaseClient client = new ServiceManagingBaseClient();
//
//        // Create transport Policy
//        Policy transportMethodAmountPolicy = new MethodAmountPolicy(denyRootedJailbroken,denyEmulatorSimulator,fences,amount);
//
//        // Convert transport policy to domain policy
//        com.iovation.launchkey.sdk.domain.policy.MethodAmountPolicy domainMethodAmountPolicy =
//                (com.iovation.launchkey.sdk.domain.policy.MethodAmountPolicy)
//                        client.getDomainPolicyFromTransportPolicy(transportMethodAmountPolicy);
//
//        // Assert using constants from above
//        assertEquals(domainMethodAmountPolicy.getDenyRootedJailbroken(), denyRootedJailbroken);
//        assertEquals(domainMethodAmountPolicy.getDenyEmulatorSimulator(), denyEmulatorSimulator);
//        assertEquals(domainMethodAmountPolicy.getAmount(), amount);
//        assertNotNull(domainMethodAmountPolicy.getFences());
//        for (com.iovation.launchkey.sdk.domain.policy.Fence fence : domainMethodAmountPolicy.getFences()) {
//            if (fence.getFenceName().equals(geoCircleFenceName)) {
//                com.iovation.launchkey.sdk.domain.policy.GeoCircleFence domainGeoCircleFence =
//                        (com.iovation.launchkey.sdk.domain.policy.GeoCircleFence) fence;
//                assertEquals(domainGeoCircleFence.getRadius(),radius,0.0001);
//                assertEquals(domainGeoCircleFence.getLatitude(),latitude,0.0001);
//                assertEquals(domainGeoCircleFence.getLongitude(),longitude,0.0001);
//            }
//            else if (fence.getFenceName().equals(territoryFenceName)) {
//                com.iovation.launchkey.sdk.domain.policy.TerritoryFence domainTerritoryFence =
//                        (com.iovation.launchkey.sdk.domain.policy.TerritoryFence) fence;
//                assertEquals(domainTerritoryFence.getCountry(),country);
//                assertEquals(domainTerritoryFence.getAdministrativeArea(),adminArea);
//                assertEquals(domainTerritoryFence.getPostalCode(), postalCode);
//            }
//            else {
//                throw new BaseException("This ain't it chief",null,null);
//            }
//        }
//    }
//
//    @Test
//    public void getDomainPolicyFromTransportPolicyConditionalGeofencePolicy() throws Exception {
//        // Create constants
//        Factor inherence = Factor.INHERENCE;
//        Factor possession = Factor.POSSESSION;
//
//        List<Factor> factors = new ArrayList<>();
//        factors.add(inherence);
//        factors.add(possession);
//
//        List<String> factorsAsStrings = new ArrayList<>();
//        factorsAsStrings.add(inherence.toString());
//        factorsAsStrings.add(possession.toString());
//
//        String geoCircleFenceName = "a GeoCircle Fence";
//        double latitude = 1;
//        double longitude = 1;
//        double radius = 1;
//        String territoryFenceName = "a Territory Fence";
//        String country = "country";
//        String adminArea = "Admin Area";
//        String postalCode = "ABCDE6";
//
//        Fence geoCircleFence = new GeoCircleFence(geoCircleFenceName, latitude,longitude,radius);
//        Fence territorialFence = new TerritoryFence(territoryFenceName,country,adminArea,postalCode);
//
//        List<Fence> fences = new ArrayList<>();
//        fences.add(geoCircleFence);
//        fences.add(territorialFence);
//
//        Boolean denyRootedJailbroken = false;
//        Boolean denyEmulatorSimulator = false;
//        int amount = 5;
//
//        // Create Transport Policies
//        Policy inPolicy = new MethodAmountPolicy(denyEmulatorSimulator,denyRootedJailbroken,null,amount);
//        Policy outPolicy = new FactorsPolicy(denyEmulatorSimulator,denyRootedJailbroken,null,factorsAsStrings);
//        ServiceManagingBaseClient client = new ServiceManagingBaseClient();
//        Policy transportCondGeoPolicy = new ConditionalGeoFencePolicy(denyRootedJailbroken,denyEmulatorSimulator,fences,inPolicy,outPolicy);
//
//        // Do conversion Transport to Domain Policy
//        com.iovation.launchkey.sdk.domain.policy.ConditionalGeoFencePolicy domainCondGeoPolicy = (com.iovation.launchkey.sdk.domain.policy.ConditionalGeoFencePolicy) client.getDomainPolicyFromTransportPolicy(transportCondGeoPolicy);
//
//        // Assert Values including sub policies
//        assertEquals(domainCondGeoPolicy.getDenyRootedJailbroken(),denyRootedJailbroken);
//        assertEquals(domainCondGeoPolicy.getDenyEmulatorSimulator(),denyEmulatorSimulator);
//        assertNotNull(domainCondGeoPolicy.getFences());
//        for (com.iovation.launchkey.sdk.domain.policy.Fence fence : domainCondGeoPolicy.getFences()) {
//            if (fence.getFenceName().equals(geoCircleFenceName)) {
//                com.iovation.launchkey.sdk.domain.policy.GeoCircleFence domainGeoCircleFence = (com.iovation.launchkey.sdk.domain.policy.GeoCircleFence) fence;
//                assertEquals(domainGeoCircleFence.getRadius(),radius,0.0001);
//                assertEquals(domainGeoCircleFence.getLatitude(),latitude,0.0001);
//                assertEquals(domainGeoCircleFence.getLongitude(),longitude,0.0001);
//            }
//            else if (fence.getFenceName().equals(territoryFenceName)) {
//                com.iovation.launchkey.sdk.domain.policy.TerritoryFence domainTerritoryFence = (com.iovation.launchkey.sdk.domain.policy.TerritoryFence) fence;
//                assertEquals(domainTerritoryFence.getCountry(),country);
//                assertEquals(domainTerritoryFence.getAdministrativeArea(),adminArea);
//                assertEquals(domainTerritoryFence.getPostalCode(), postalCode);
//            }
//            else {
//                throw new BaseException("This ain't it chief",null,null);
//            }
//        }
//        com.iovation.launchkey.sdk.domain.policy.MethodAmountPolicy domainInPolicy = (com.iovation.launchkey.sdk.domain.policy.MethodAmountPolicy) domainCondGeoPolicy.getInPolicy();
//        com.iovation.launchkey.sdk.domain.policy.FactorsPolicy domainOutPolicy = (com.iovation.launchkey.sdk.domain.policy.FactorsPolicy) domainCondGeoPolicy.getOutPolicy();
//
//        assertEquals(domainInPolicy.getDenyRootedJailbroken(), denyRootedJailbroken);
//        assertEquals(domainInPolicy.getDenyEmulatorSimulator(), denyEmulatorSimulator);
//        assertEquals(domainInPolicy.getAmount(), amount);
//        assertEquals(domainOutPolicy.getDenyEmulatorSimulator(),denyEmulatorSimulator);
//        assertEquals(domainOutPolicy.getDenyRootedJailbroken(),denyRootedJailbroken);
//        assertEquals(domainOutPolicy.getFactors(),factors);
//        assertNull(domainOutPolicy.getFences());
//        assertNull(domainInPolicy.getFences());
//    }
//
//    @Test
//    public void getTransportPolicyFromDomainPolicyConditionalGeofencePolicy() throws Exception {
//
//        Boolean denyRootedJailbroken = false;
//        Boolean denyEmulatorSimulator = false;
//        String geoCircleFenceName = "a GeoCircle Fence";
//        double latitude = 1;
//        double longitude = 1;
//        double radius = 1;
//        String territoryFenceName = "a Territory Fence";
//        String country = "country";
//        String adminArea = "Admin Area";
//        String postalCode = "ABCDE6";
//        Factor inherence = Factor.INHERENCE;
//        Factor possession = Factor.POSSESSION;
//
//        List<Factor> factors = new ArrayList<>();
//        factors.add(inherence);
//        factors.add(possession);
//
//        List<String> factorsAsStrings = new ArrayList<>();
//        factorsAsStrings.add(inherence.toString());
//        factorsAsStrings.add(possession.toString());
//
//        com.iovation.launchkey.sdk.domain.policy.TerritoryFence domainTerritoryFence = new com.iovation.launchkey.sdk.domain.policy.TerritoryFence(country,adminArea,postalCode,territoryFenceName);
//        com.iovation.launchkey.sdk.domain.policy.GeoCircleFence domainGeoCircleFence = new com.iovation.launchkey.sdk.domain.policy.GeoCircleFence(geoCircleFenceName,latitude,longitude,radius);
//
//        List<com.iovation.launchkey.sdk.domain.policy.Fence> fences = new ArrayList<>();
//        fences.add(domainTerritoryFence);
//        fences.add(domainGeoCircleFence);
//
//        com.iovation.launchkey.sdk.domain.policy.FactorsPolicy inPolicy = new com.iovation.launchkey.sdk.domain.policy.FactorsPolicy(denyRootedJailbroken,denyEmulatorSimulator,null, factors);
//        com.iovation.launchkey.sdk.domain.policy.MethodAmountPolicy outPolicy = new com.iovation.launchkey.sdk.domain.policy.MethodAmountPolicy();
//        com.iovation.launchkey.sdk.domain.policy.ConditionalGeoFencePolicy domainCondGeoPolicy = new com.iovation.launchkey.sdk.domain.policy.ConditionalGeoFencePolicy(denyRootedJailbroken,denyEmulatorSimulator,fences,inPolicy,outPolicy);
//
//        ServiceManagingBaseClient client = new ServiceManagingBaseClient();
//        ConditionalGeoFencePolicy transportCondGeoPolicy = (ConditionalGeoFencePolicy) client.getTransportPolicyFromDomainPolicy(domainCondGeoPolicy);
//
//        assertEquals(transportCondGeoPolicy.getPolicyType(), "COND_GEO");
//        assertEquals(transportCondGeoPolicy.getDenyRootedJailbroken(),denyRootedJailbroken);
//        assertEquals(transportCondGeoPolicy.getDenyEmulatorSimulator(),denyEmulatorSimulator);
//        List<Fence> transportFences = transportCondGeoPolicy.getFences();
//        assertNotNull(transportFences);
//        for (Fence fence : transportFences) {
//            if (fence instanceof TerritoryFence) {
//                TerritoryFence territoryFence = (TerritoryFence) fence;
//                assertEquals(territoryFence.getFenceName(),territoryFenceName);
//                assertEquals(territoryFence.getPostalCode(),postalCode);
//                assertEquals(territoryFence.getAdministrativeArea(), adminArea);
//                assertEquals(territoryFence.getCountry(), country);
//                assertEquals(territoryFence.getType(), "TERRITORY");
//
//            }
//            else if (fence instanceof  GeoCircleFence) {
//                GeoCircleFence geoCircleFence = (GeoCircleFence) fence;
//                assertEquals(geoCircleFence.getRadius(),radius,0.0001);
//                assertEquals(geoCircleFence.getLongitude(),longitude,0.0001);
//                assertEquals(geoCircleFence.getLatitude(), latitude, 0.0001);
//                assertEquals(geoCircleFence.getFenceName(), geoCircleFenceName);
//                assertEquals(geoCircleFence.getType(), "GEO_CIRCLE");
//            }
//            else {
//                throw new BaseException("This ain't it chief",null,null);
//            }
//        }
//
//        FactorsPolicy transportInPolicy =(FactorsPolicy) transportCondGeoPolicy.getInPolicy();
//        MethodAmountPolicy transportOutPolicy = (MethodAmountPolicy) transportCondGeoPolicy.getOutPolicy();
//
//        assertEquals(transportInPolicy.getDenyRootedJailbroken(),denyRootedJailbroken);
//        assertEquals(transportInPolicy.getDenyEmulatorSimulator(), denyEmulatorSimulator);
//        assertNull(transportInPolicy.getFences());
//        assertEquals(transportInPolicy.getFactors(),factorsAsStrings);
//        assertEquals(transportInPolicy.getPolicyType(),"FACTORS");
//
//        assertEquals(transportOutPolicy.getDenyRootedJailbroken(), denyRootedJailbroken);
//        assertEquals(transportOutPolicy.getDenyEmulatorSimulator(), denyEmulatorSimulator);
//        assertNull(transportOutPolicy.getFences());
//        assertEquals(transportOutPolicy.getAmount(),0);
//        assertEquals(transportOutPolicy.getPolicyType(),"METHOD_AMOUNT");
//
//    }
//
//    @Test
//    public void getTransportPolicyFromDomainPolicyFactorsPolicy() throws Exception {
//        Boolean denyRootedJailbroken = true;
//        Boolean denyEmulatorSimulator = false;
//        String geoCircleFenceName = "a GeoCircle Fence";
//        double latitude = 1;
//        double longitude = 1;
//        double radius = 1;
//        String territoryFenceName = "a Territory Fence";
//        String country = "country";
//        String adminArea = "Admin Area";
//        String postalCode = "ABCDE6";
//        Factor inherence = Factor.INHERENCE;
//        Factor possession = Factor.POSSESSION;
//
//        List<Factor> factors = new ArrayList<>();
//        factors.add(inherence);
//        factors.add(possession);
//
//        List<String> factorsAsStrings = new ArrayList<>();
//        factorsAsStrings.add(inherence.toString());
//        factorsAsStrings.add(possession.toString());
//
//        com.iovation.launchkey.sdk.domain.policy.TerritoryFence domainTerritoryFence = new com.iovation.launchkey.sdk.domain.policy.TerritoryFence(country,adminArea,postalCode,territoryFenceName);
//        com.iovation.launchkey.sdk.domain.policy.GeoCircleFence domainGeoCircleFence = new com.iovation.launchkey.sdk.domain.policy.GeoCircleFence(geoCircleFenceName,latitude,longitude,radius);
//
//        List<com.iovation.launchkey.sdk.domain.policy.Fence> fences = new ArrayList<>();
//        fences.add(domainTerritoryFence);
//        fences.add(domainGeoCircleFence);
//
//        com.iovation.launchkey.sdk.domain.policy.FactorsPolicy domainFactorsPolicy = new com.iovation.launchkey.sdk.domain.policy.FactorsPolicy(denyRootedJailbroken,denyEmulatorSimulator,fences, factors);
//
//        ServiceManagingBaseClient client = new ServiceManagingBaseClient();
//        FactorsPolicy transportFactorsPolicy = (FactorsPolicy) client.getTransportPolicyFromDomainPolicy(domainFactorsPolicy);
//
//        assertEquals(transportFactorsPolicy.getDenyRootedJailbroken(),denyRootedJailbroken);
//        assertEquals(transportFactorsPolicy.getDenyEmulatorSimulator(),denyEmulatorSimulator);
//        assertEquals(transportFactorsPolicy.getFactors(),factorsAsStrings);
//        assertEquals(transportFactorsPolicy.getPolicyType(), "FACTORS");
//        List<Fence> transportFences = transportFactorsPolicy.getFences();
//        assertNotNull(transportFences);
//        for (Fence fence : transportFences) {
//            if (fence instanceof TerritoryFence) {
//                TerritoryFence territoryFence = (TerritoryFence) fence;
//                assertEquals(territoryFence.getFenceName(),territoryFenceName);
//                assertEquals(territoryFence.getPostalCode(),postalCode);
//                assertEquals(territoryFence.getAdministrativeArea(), adminArea);
//                assertEquals(territoryFence.getCountry(), country);
//                assertEquals(territoryFence.getType(), "TERRITORY");
//            }
//            else if (fence instanceof  GeoCircleFence) {
//                GeoCircleFence geoCircleFence = (GeoCircleFence) fence;
//                assertEquals(geoCircleFence.getRadius(),radius,0.0001);
//                assertEquals(geoCircleFence.getLongitude(),longitude,0.0001);
//                assertEquals(geoCircleFence.getLatitude(), latitude, 0.0001);
//                assertEquals(geoCircleFence.getFenceName(), geoCircleFenceName);
//                assertEquals(geoCircleFence.getType(), "GEO_CIRCLE");
//            }
//            else {
//                throw new BaseException("This ain't it chief",null,null);
//            }
//        }
//    }

    @Test
    public void getTransportPolicyFromDomainPolicyMethodAmountPolicy() throws Exception {
        Boolean denyRootedJailbroken = true;
        Boolean denyEmulatorSimulator = false;
        String geoCircleFenceName = "aafdsafsafasf";
        double latitude = 0.0;
        double longitude = 99;
        double radius = 532.423;
        String territoryFenceName = "????";
        String country = "some place";
        String adminArea = "wat";
        String postalCode = "3";
        int amount = 5;

        com.iovation.launchkey.sdk.domain.policy.TerritoryFence domainTerritoryFence = new com.iovation.launchkey.sdk.domain.policy.TerritoryFence(country,adminArea,postalCode,territoryFenceName);
        com.iovation.launchkey.sdk.domain.policy.GeoCircleFence domainGeoCircleFence = new com.iovation.launchkey.sdk.domain.policy.GeoCircleFence(geoCircleFenceName,latitude,longitude,radius);

        List<com.iovation.launchkey.sdk.domain.policy.Fence> fences = new ArrayList<>();
        fences.add(domainTerritoryFence);
        fences.add(domainGeoCircleFence);

        com.iovation.launchkey.sdk.domain.policy.MethodAmountPolicy domainMethodAmountPolicy = new com.iovation.launchkey.sdk.domain.policy.MethodAmountPolicy(denyRootedJailbroken,denyEmulatorSimulator,fences,amount);
        ServiceManagingBaseClient client = new ServiceManagingBaseClient();
        MethodAmountPolicy transportMethodAmountPolicy = (MethodAmountPolicy) client.getTransportPolicyFromDomainPolicy(domainMethodAmountPolicy);

        assertEquals(transportMethodAmountPolicy.getDenyRootedJailbroken(),denyRootedJailbroken);
        assertEquals(transportMethodAmountPolicy.getDenyEmulatorSimulator(), denyEmulatorSimulator);
        assertEquals(transportMethodAmountPolicy.getAmount(), amount);
        assertEquals(transportMethodAmountPolicy.getPolicyType(), "METHOD_AMOUNT");
        List<Fence> transportFences = transportMethodAmountPolicy.getFences();
        assertNotNull(transportFences);
        for (Fence fence : transportFences) {
            if (fence instanceof TerritoryFence) {
                TerritoryFence territoryFence = (TerritoryFence) fence;
                assertEquals(territoryFence.getFenceName(),territoryFenceName);
                assertEquals(territoryFence.getPostalCode(),postalCode);
                assertEquals(territoryFence.getAdministrativeArea(), adminArea);
                assertEquals(territoryFence.getCountry(), country);
                assertEquals(territoryFence.getType(), "TERRITORY");
            }
            else if (fence instanceof  GeoCircleFence) {
                GeoCircleFence geoCircleFence = (GeoCircleFence) fence;
                assertEquals(geoCircleFence.getRadius(),radius,0.0001);
                assertEquals(geoCircleFence.getLongitude(),longitude,0.0001);
                assertEquals(geoCircleFence.getLatitude(), latitude, 0.0001);
                assertEquals(geoCircleFence.getFenceName(), geoCircleFenceName);
                assertEquals(geoCircleFence.getType(), "GEO_CIRCLE");
            }
            else {
                throw new BaseException("This ain't it chief",null,null);
            }
        }
    }
}
