/**
 * Copyright 2017 iovation, Inc.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.domain.policy.*;
import com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy;
import com.iovation.launchkey.sdk.error.InvalidPolicyAttributes;
import com.iovation.launchkey.sdk.error.UnknownFenceTypeException;
import com.iovation.launchkey.sdk.error.UnknownPolicyException;
import com.iovation.launchkey.sdk.transport.domain.AuthPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

class ServiceManagingBaseClient {
    protected ServicePolicy getDomainServicePolicyFromTransportServicePolicy(
            com.iovation.launchkey.sdk.transport.domain.ServicePolicy policy) {

        List<ServicePolicy.Location> geoFences = new ArrayList<>();
        for (AuthPolicy.Location location : policy.getGeoFences()) {
            geoFences.add(new ServicePolicy.Location(location.getName(), location.getRadius(), location.getLatitude(),
                    location.getLongitude()));
        }
        List<ServicePolicy.TimeFence> timeFences = new ArrayList<>();
        for (com.iovation.launchkey.sdk.transport.domain.ServicePolicy.TimeFence timeFence : policy.getTimeFences()) {
            List<ServicePolicy.Day> days = new ArrayList<>();
            for (String day : timeFence.getDays()) {
                days.add(ServicePolicy.Day.fromString(day));
            }
            timeFences.add(new ServicePolicy.TimeFence(timeFence.getName(), days, timeFence.getStartHour(),
                    timeFence.getStartMinute(), timeFence.getEndHour(), timeFence.getEndMinute(),
                    TimeZone.getTimeZone(timeFence.getTimezone())));
        }
        Integer any;
        Boolean knowledge, inherence, possession;
        if (policy.getMinimumRequirements().isEmpty()) {
            any = null;
            knowledge = inherence = possession = null;
        } else {
            any = policy.getMinimumRequirements().get(0).getAny();
            knowledge = policy.getMinimumRequirements().get(0).getKnowledge() == null ? null : policy.getMinimumRequirements().get(0).getKnowledge() == 1;
            inherence = policy.getMinimumRequirements().get(0).getInherence() == null ? null : policy.getMinimumRequirements().get(0).getInherence() == 1;
            possession = policy.getMinimumRequirements().get(0).getPossession() == null ? null : policy.getMinimumRequirements().get(0).getPossession() == 1;
        }
        return new ServicePolicy(any, knowledge, inherence, possession,
                policy.getDeviceIntegrity(), geoFences, timeFences);
    }

    protected com.iovation.launchkey.sdk.transport.domain.ServicePolicy getTransportServicePolicyFromDomainServicePolicy(
            ServicePolicy policy) {
        com.iovation.launchkey.sdk.transport.domain.ServicePolicy transportPolicy =
                new com.iovation.launchkey.sdk.transport.domain.ServicePolicy(policy.getRequiredFactors(),
                        policy.isInherenceFactorRequired(), policy.isKnowledgeFactorRequired(),
                        policy.isPossessionFactorRequired(), policy.isJailBreakProtectionEnabled());
        for (ServicePolicy.TimeFence fence : policy.getTimeFences()) {
            List<String> days = new ArrayList<>();
            for (ServicePolicy.Day day : fence.getDays()) {
                days.add(day.toString());
            }
            transportPolicy.addTimeFence(new com.iovation.launchkey.sdk.transport.domain.ServicePolicy.TimeFence(fence.getName(), days, fence.getStartHour(), fence.getEndHour(),
                    fence.getStartMinute(), fence.getEndMinute(), fence.getTimeZone().getID()));
        }

        for (ServicePolicy.Location location : policy.getLocations()) {
            transportPolicy.addGeoFence(location.getName(), location.getRadius(), location.getLatitude(),
                    location.getLongitude());
        }
        return transportPolicy;
    }

    protected Policy getDomainPolicyFromTransportPolicy(
            com.iovation.launchkey.sdk.transport.domain.Policy transportPolicy) throws UnknownPolicyException, UnknownFenceTypeException, InvalidPolicyAttributes {

        Policy domainPolicy = null;
        String policyType = transportPolicy.getPolicyType();
        Boolean denyRootedJailbroken = transportPolicy.getDenyRootedJailbroken();
        Boolean denyEmulatorSimulator = transportPolicy.getDenyEmulatorSimulator();
        List<com.iovation.launchkey.sdk.transport.domain.Fence> transportPolicyFences = transportPolicy.getFences();
        List<Fence> fences = null;
        if (transportPolicyFences != null) {
            fences = new ArrayList<>();
            for (com.iovation.launchkey.sdk.transport.domain.Fence transportFence : transportPolicyFences ) {
                fences.add(getDomainFenceFromTransportFence(transportFence));
            }
        }
        // No concept of LEGACY policy in transport objects
        if (policyType.equals("COND_GEO")) {
            com.iovation.launchkey.sdk.transport.domain.ConditionalGeoFencePolicy conGeoPolicy =
                    (com.iovation.launchkey.sdk.transport.domain.ConditionalGeoFencePolicy) transportPolicy;
            Policy inPolicy = getDomainPolicyFromTransportPolicy(conGeoPolicy.getInPolicy());
            Policy outPolicy = getDomainPolicyFromTransportPolicy(conGeoPolicy.getOutPolicy());
            domainPolicy = new ConditionalGeoFencePolicy(denyRootedJailbroken,denyEmulatorSimulator,fences,inPolicy,outPolicy);
        }
        else if (policyType.equals("METHOD_AMOUNT")) {
            com.iovation.launchkey.sdk.transport.domain.MethodAmountPolicy methodAmountPolicy =
                    (com.iovation.launchkey.sdk.transport.domain.MethodAmountPolicy) transportPolicy;
            domainPolicy = new MethodAmountPolicy(denyRootedJailbroken,denyEmulatorSimulator,fences,methodAmountPolicy.getAmount());
        }
        else if (policyType.equals("FACTORS")) {
            com.iovation.launchkey.sdk.transport.domain.FactorsPolicy factorsPolicy = (com.iovation.launchkey.sdk.transport.domain.FactorsPolicy) transportPolicy;
            List<String> transportFactors = factorsPolicy.getFactors();
            boolean inherence = false;
            boolean possession = false;
            boolean knowledge = false;
            for (String factor : transportFactors) {
                if (factor.equals("KNOWLEDGE")) {
                    knowledge = true;
                }
                if (factor.equals("INHERENCE")) {
                    inherence = true;
                }
                if (factor.equals("POSSESSION")) {
                    possession = true;
                }
            }
            domainPolicy = new FactorsPolicy(denyRootedJailbroken,denyEmulatorSimulator,fences,inherence,knowledge,possession);
        }
        else {
            throw new UnknownPolicyException("Unknown policy type",null,null);
        }
        return domainPolicy;
    }

    protected com.iovation.launchkey.sdk.transport.domain.PolicyAdapter getTransportPolicyFromDomainPolicy(Policy domainPolicy) throws UnknownPolicyException, UnknownFenceTypeException {
        Boolean denyRootedJailbroken = domainPolicy.getDenyRootedJailbroken();
        Boolean denyEmulatorSimulator = domainPolicy.getDenyEmulatorSimulator();

        List<Fence> domainPolicyFences = domainPolicy.getFences();
        List<com.iovation.launchkey.sdk.transport.domain.Fence> fences = null;
        if (domainPolicyFences != null) {
            fences = new ArrayList<>();
            for (Fence domainFence : domainPolicyFences) {
                fences.add(getTransportFenceFromDomainFence(domainFence));
            }
        }
        com.iovation.launchkey.sdk.transport.domain.Policy inPolicy = null;
        com.iovation.launchkey.sdk.transport.domain.Policy outPolicy = null;

        com.iovation.launchkey.sdk.transport.domain.PolicyAdapter transportPolicy = null;

        if (domainPolicy instanceof ConditionalGeoFencePolicy) {
            ConditionalGeoFencePolicy condGeoPolicy = (ConditionalGeoFencePolicy) domainPolicy;
            Policy condInPolicy = condGeoPolicy.getInPolicy();
            Policy condOutPolicy = condGeoPolicy.getOutPolicy();
            verifySubPolicy(condInPolicy);
            verifySubPolicy(condOutPolicy);
            inPolicy = (com.iovation.launchkey.sdk.transport.domain.Policy) getTransportPolicyFromDomainPolicy(condInPolicy);
            outPolicy = (com.iovation.launchkey.sdk.transport.domain.Policy) getTransportPolicyFromDomainPolicy(condOutPolicy);
            transportPolicy = new com.iovation.launchkey.sdk.transport.domain.ConditionalGeoFencePolicy(denyRootedJailbroken,denyEmulatorSimulator,fences,inPolicy,outPolicy);
        }
        else if (domainPolicy instanceof MethodAmountPolicy) {
            MethodAmountPolicy methodAmountPolicy = (MethodAmountPolicy) domainPolicy;
            int amount = methodAmountPolicy.getAmount();
            transportPolicy = new com.iovation.launchkey.sdk.transport.domain.MethodAmountPolicy(denyRootedJailbroken,denyEmulatorSimulator,fences,amount);

        }
        else if (domainPolicy instanceof FactorsPolicy) {
            FactorsPolicy factorsPolicy = (FactorsPolicy) domainPolicy;
            List<String> factors = new ArrayList<>();
            if (factorsPolicy.isInherenceRequired()) {
                factors.add("INHERENCE");
            }
            if (factorsPolicy.isKnowledgeRequired()) {
                factors.add("KNOWLEDGE");
            }
            if (factorsPolicy.isPossessionRequired()) {
                factors.add("POSSESSION");
            }
            if (factors.isEmpty()) {
                factors = null;
            }
            transportPolicy = new com.iovation.launchkey.sdk.transport.domain.FactorsPolicy(denyRootedJailbroken,denyEmulatorSimulator,fences,factors);
        }
        else if (domainPolicy instanceof LegacyPolicy) {
            LegacyPolicy legacyPolicy = (LegacyPolicy) domainPolicy;
            ServicePolicy servicePolicy = getServicePolicyFromLegacyPolicy(legacyPolicy);
            transportPolicy = getTransportServicePolicyFromDomainServicePolicy(servicePolicy);
        }
        else {
            throw new UnknownPolicyException("Unknown policy type",null,null);
        }
        return transportPolicy;
    }

    protected LegacyPolicy getLegacyPolicyFromServicePolicy(ServicePolicy servicePolicy) {
        List<GeoCircleFence> geoFences = null;
        if (servicePolicy.getLocations() != null) {
            if (!servicePolicy.getLocations().isEmpty()) {
                geoFences = new ArrayList<>();
                for (ServicePolicy.Location location : servicePolicy.getLocations()) {
                    geoFences.add(getGeoCircleFenceFromLocation(location));
                }
            }
        }
        Integer requiredFactors = servicePolicy.getRequiredFactors();
        int amount = (requiredFactors != null)  ? requiredFactors : 0;
        Boolean inherence = servicePolicy.isInherenceFactorRequired();
        Boolean knownledge = servicePolicy.isKnowledgeFactorRequired();
        Boolean possession = servicePolicy.isPossessionFactorRequired();
        boolean denyRootedJailbroken = (servicePolicy.isJailBreakProtectionEnabled() != null) ? servicePolicy.isJailBreakProtectionEnabled() : false;
        List<ServicePolicy.TimeFence> timeFences = servicePolicy.getTimeFences();

        return new LegacyPolicy(amount,inherence,knownledge,possession,denyRootedJailbroken,geoFences,timeFences);
    }

    protected ServicePolicy getServicePolicyFromLegacyPolicy(LegacyPolicy legacyPolicy) {
        List<ServicePolicy.Location> locations = null;
        if (legacyPolicy.getFences() != null) {
            if (!legacyPolicy.getFences().isEmpty()) {
                locations = new ArrayList<>();
                for (Fence fence : legacyPolicy.getFences()) {
                    if (fence instanceof GeoCircleFence) {
                        locations.add(getLocationFromGeoCircleFence((GeoCircleFence) fence));
                    }
                }
            }
        }
        Integer requiredFactors = (legacyPolicy.getAmount() != 0) ? legacyPolicy.getAmount() : null;
        Boolean knowledgeFactorRequired = legacyPolicy.isKnowledgeRequired();
        Boolean inherenceFactorRequired = legacyPolicy.isInherenceRequired();
        Boolean possessionFactorRequired = legacyPolicy.isPossessionRequired();
        Boolean jailBreakProtectionEnabled = legacyPolicy.getDenyRootedJailbroken();
        List<ServicePolicy.TimeFence> timeFences = legacyPolicy.getTimeFences();

        return new ServicePolicy(requiredFactors,
                knowledgeFactorRequired,
                inherenceFactorRequired,
                possessionFactorRequired,
                jailBreakProtectionEnabled,
                locations,
                timeFences);
    }

    private GeoCircleFence getGeoCircleFenceFromLocation(ServicePolicy.Location location) {
        return new GeoCircleFence(location.getName(),location.getLatitude(),location.getLongitude(),location.getRadius());
    }

    private ServicePolicy.Location getLocationFromGeoCircleFence(GeoCircleFence geoCircleFence) {
        return new ServicePolicy.Location(geoCircleFence.getFenceName(),geoCircleFence.getRadius(),geoCircleFence.getLatitude(),geoCircleFence.getLongitude());
    }

    private Fence getDomainFenceFromTransportFence(com.iovation.launchkey.sdk.transport.domain.Fence transportFence) throws UnknownFenceTypeException {
        Fence domainFence = null;
        if (transportFence instanceof com.iovation.launchkey.sdk.transport.domain.GeoCircleFence) {
            com.iovation.launchkey.sdk.transport.domain.GeoCircleFence geoCircleFence = (com.iovation.launchkey.sdk.transport.domain.GeoCircleFence) transportFence;
            String name = geoCircleFence.getFenceName();
            double latitude = geoCircleFence.getLatitude();
            double longitude = geoCircleFence.getLongitude();
            double radius = geoCircleFence.getRadius();
            domainFence = new GeoCircleFence(name,latitude,longitude,radius);
        }
        else if (transportFence instanceof com.iovation.launchkey.sdk.transport.domain.TerritoryFence) {
            com.iovation.launchkey.sdk.transport.domain.TerritoryFence territoryFence = (com.iovation.launchkey.sdk.transport.domain.TerritoryFence) transportFence;
            String name = territoryFence.getFenceName();
            String adminArea = territoryFence.getAdministrativeArea();
            String postalCode = territoryFence.getPostalCode();
            String country = territoryFence.getCountry();
            domainFence = new TerritoryFence(country,adminArea,postalCode,name);
        }
        else {
            throw new UnknownFenceTypeException("Unknown fence type",null,null);
        }
        return domainFence;
    }

    private com.iovation.launchkey.sdk.transport.domain.Fence getTransportFenceFromDomainFence(Fence domainFence) throws UnknownFenceTypeException {
        com.iovation.launchkey.sdk.transport.domain.Fence transportFence = null;
        if (domainFence instanceof GeoCircleFence) {
            GeoCircleFence geoCircleFence = (GeoCircleFence) domainFence;
            String name = geoCircleFence.getFenceName();
            double latitude = geoCircleFence.getLatitude();
            double longitude = geoCircleFence.getLongitude();
            double radius = geoCircleFence.getRadius();
            transportFence = new com.iovation.launchkey.sdk.transport.domain.GeoCircleFence(name,latitude,longitude,radius);
        }
        else if (domainFence instanceof TerritoryFence) {
            TerritoryFence territoryFence = (TerritoryFence) domainFence;
            String name = territoryFence.getFenceName();
            String adminArea = territoryFence.getAdministrativeArea();
            String postalCode = territoryFence.getPostalCode();
            String country = territoryFence.getCountry();
            transportFence = new com.iovation.launchkey.sdk.transport.domain.TerritoryFence(name,country,adminArea,postalCode);

        }
        else {
            throw new UnknownFenceTypeException("Unknown fence type",null,null);
        }
        return transportFence;
    }

    private void verifySubPolicy(Policy subPolicy) throws UnknownPolicyException {
        // Assert policy is either null or is of Type FactorsPolicy or MethodAmountPolicy
        // Assert denyRootedJailbroken and denyEmulatorSimulator are false
        // Assert no fences
        if (subPolicy == null) {
            return;
        }
        if ((subPolicy instanceof FactorsPolicy) || (subPolicy instanceof MethodAmountPolicy)) {
            if (subPolicy.getDenyEmulatorSimulator() || subPolicy.getDenyRootedJailbroken()) {
                throw new UnknownPolicyException("Inside or Outside Policy objects cannot have denyEmulatorSimulator or denyRootedJailbroken set to true", null, null);
            }
            if (subPolicy.getFences() != null) {
                throw new UnknownPolicyException("Fences are not supported on Inside or Outside Policy objects",null,null);
            }
        }
        else {
            throw new UnknownPolicyException("Inside or Outside Policy objects must be of type FactorsPolicy or MethodAmountPolicy, or null if no policy", null, null);
        }
    }
}
