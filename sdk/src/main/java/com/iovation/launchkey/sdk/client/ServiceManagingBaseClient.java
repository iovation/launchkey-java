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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import com.iovation.launchkey.sdk.domain.policy.*;
import com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy;
import com.iovation.launchkey.sdk.error.InvalidPolicyAttributes;
import com.iovation.launchkey.sdk.error.UnknownFenceTypeException;
import com.iovation.launchkey.sdk.error.UnknownPolicyException;
import com.iovation.launchkey.sdk.transport.domain.Policy.MinimumRequirement;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import static com.iovation.launchkey.sdk.transport.domain.Fence.*;
import static com.iovation.launchkey.sdk.transport.domain.Policy.TYPE_FACTORS;
import static com.iovation.launchkey.sdk.transport.domain.Policy.TYPE_METHOD_AMOUNT;

abstract class ServiceManagingBaseClient {

    Policy getDomainPolicyFromTransportPolicy(
            com.iovation.launchkey.sdk.transport.domain.Policy transportPolicy, boolean isSubPolicy) throws UnknownPolicyException, UnknownFenceTypeException, InvalidPolicyAttributes {

        Policy domainPolicy;
        String policyType = transportPolicy.getPolicyType();
        List<com.iovation.launchkey.sdk.transport.domain.Fence> transportPolicyFences = transportPolicy.getFences();
        List<Fence> fences = null;
        if (transportPolicyFences != null && !(isSubPolicy && transportPolicyFences.size() == 0)) {
            fences = new ArrayList<>();
            for (com.iovation.launchkey.sdk.transport.domain.Fence transportFence : transportPolicyFences) {
                if (com.iovation.launchkey.sdk.transport.domain.Fence.TYPE_TERRITORY.equals(transportFence.getType())) {
                    fences.add(new TerritoryFence(transportFence.getName(), transportFence.getCountry(), transportFence.getAdministrativeArea(), transportFence.getPostalCode()));
                } else if (com.iovation.launchkey.sdk.transport.domain.Fence.TYPE_GEO_CIRCLE.equals(transportFence.getType())) {
                    fences.add(new GeoCircleFence(transportFence.getName(), transportFence.getLatitude(), transportFence.getLongitude(), transportFence.getRadius()));
                }
            }
        }
        if (policyType == null || policyType.equals(com.iovation.launchkey.sdk.transport.domain.Policy.TYPE_LEGACY)) {
            List<GeoCircleFence> geoCircleFences = new ArrayList<GeoCircleFence>();
            List<LegacyPolicy.TimeFence> timeFences = new ArrayList<>();
            boolean deviceIntegrity = false;
            int amount = 0;
            boolean inherence = false;
            boolean knowledge = false;
            boolean possession = false;

            try {
                ArrayNode factors = transportPolicy.getFactors();
                if (factors != null) {
                    for (JsonNode factor : factors) {
                        final String factorName = factor.get("factor").asText();
                        final JsonNode attributes = factor.get("attributes");
                        if (factorName.equals("geofence")) {
                            for (JsonNode location : attributes.get("locations")) {
                                String name = location.has("name") ? location.get("name").asText() : null;
                                geoCircleFences.add(
                                        new GeoCircleFence(name, location.get("latitude").asDouble(),
                                                location.get("longitude").asDouble(), location.get("radius").asDouble()
                                        )
                                );
                            }
                        } else if (factorName.equals("device integrity")) {
                            deviceIntegrity = attributes.get("factor enabled").asInt() == 1;
                        } else if (factorName.equals("timefence")) {
                            for (JsonNode timeFence : attributes.get("time fences")) {
                                List<LegacyPolicy.Day> days = new ArrayList<>();
                                for (JsonNode jsonNode : timeFence.get("days")) {
                                    days.add(LegacyPolicy.Day.fromString(jsonNode.asText()));
                                }
                                timeFences.add(new LegacyPolicy.TimeFence(
                                        timeFence.get("name").asText(),
                                        days,
                                        timeFence.get("start hour").asInt(),
                                        timeFence.get("start minute").asInt(),
                                        timeFence.get("end hour").asInt(),
                                        timeFence.get("end minute").asInt(),
                                        TimeZone.getTimeZone(timeFence.get("timezone").asText())
                                ));
                            }
                        }
                    }
                }
                if (transportPolicy.getMinimumRequirements() != null) {
                    for (MinimumRequirement minimumRequirement
                            : transportPolicy.getMinimumRequirements()) {
                        if (MinimumRequirement.Type.AUTHENTICATED.equals(minimumRequirement.getType())) {
                            if (minimumRequirement.getAny() != null) {
                                amount = minimumRequirement.getAny();
                            }
                            if (minimumRequirement.getInherence() != null) {
                                inherence = minimumRequirement.getInherence().equals(1);
                            }
                            if (minimumRequirement.getKnowledge() != null) {
                                knowledge = minimumRequirement.getKnowledge().equals(1);
                            }
                            if (minimumRequirement.getPossession() != null) {
                                possession = minimumRequirement.getPossession().equals(1);
                            }
                        }
                    }
                }

                domainPolicy = new LegacyPolicy(amount, inherence, knowledge, possession, deviceIntegrity,
                        geoCircleFences, timeFences);
            } catch (ClassCastException | NullPointerException e) {
                throw new UnknownPolicyException("\"factors\" does not conform to the LEGACY specification", e, null);
            }

        } else if (policyType.equals(com.iovation.launchkey.sdk.transport.domain.Policy.TYPE_COND_GEO)) {
            Policy inPolicy = getDomainPolicyFromTransportPolicy(transportPolicy.getInPolicy(), true);
            Policy outPolicy = getDomainPolicyFromTransportPolicy(transportPolicy.getOutPolicy(), true);
            domainPolicy = new ConditionalGeoFencePolicy(
                    transportPolicy.getDenyRootedJailbroken() == null ? false : transportPolicy.getDenyRootedJailbroken(),
                    transportPolicy.getDenyEmulatorSimulator() == null ? false : transportPolicy.getDenyEmulatorSimulator(),
                    fences, inPolicy, outPolicy);
        } else if (policyType.equals(TYPE_METHOD_AMOUNT)) {
            domainPolicy = new MethodAmountPolicy(
                    transportPolicy.getDenyRootedJailbroken() == null ? false : transportPolicy.getDenyRootedJailbroken(),
                    transportPolicy.getDenyEmulatorSimulator() == null ? false : transportPolicy.getDenyEmulatorSimulator(),
                    fences, transportPolicy.getAmount());
        } else if (policyType.equals(TYPE_FACTORS)) {
            ArrayNode transportFactors = transportPolicy.getFactors();
            boolean inherence = false;
            boolean possession = false;
            boolean knowledge = false;
            if (transportFactors != null) {
                for (JsonNode factor : transportFactors) {
                    if (factor.textValue().equals(FACTOR_KNOWLEDGE)) {
                        knowledge = true;
                    } else if (factor.textValue().equals(FACTOR_INHERENCE)) {
                        inherence = true;
                    } else if (factor.textValue().equals(FACTOR_POSSESSION)) {
                        possession = true;
                    }
                }
            }
            domainPolicy = new FactorsPolicy(
                    transportPolicy.getDenyRootedJailbroken() == null ? false : transportPolicy.getDenyRootedJailbroken(),
                    transportPolicy.getDenyEmulatorSimulator() == null ? false : transportPolicy.getDenyEmulatorSimulator(),
                    fences, inherence, knowledge, possession);
        } else {
            throw new UnknownPolicyException("Unknown policy type", null, null);
        }
        return domainPolicy;
    }

    com.iovation.launchkey.sdk.transport.domain.Policy getTransportPolicyFromDomainPolicy(Policy domainPolicy, boolean isNestedPolicy) throws UnknownPolicyException, UnknownFenceTypeException {
        if (domainPolicy == null) {
            return null;
        }
        Boolean denyRootedJailbroken;
        Boolean denyEmulatorSimulator;
        if (isNestedPolicy) {
            // if recursive call (sub policies have already been properly verified to not have these attributes set to true)
            denyRootedJailbroken = null;
            denyEmulatorSimulator = null;
        } else {
            denyRootedJailbroken = domainPolicy.getDenyRootedJailbroken();
            denyEmulatorSimulator = domainPolicy.getDenyEmulatorSimulator();
        }
        final List<Fence> domainPolicyFences = domainPolicy.getFences();
        List<com.iovation.launchkey.sdk.transport.domain.Fence> fences = null;
        if (domainPolicyFences != null) {
            fences = new ArrayList<>();
            for (Fence domainFence : domainPolicyFences) {
                fences.add(getTransportFenceFromDomainFence(domainFence));
            }
        }

        com.iovation.launchkey.sdk.transport.domain.Policy transportPolicy;

        if (domainPolicy instanceof ConditionalGeoFencePolicy) {
            ConditionalGeoFencePolicy condGeoPolicy = (ConditionalGeoFencePolicy) domainPolicy;
            Policy condInPolicy = condGeoPolicy.getInPolicy();
            Policy condOutPolicy = condGeoPolicy.getOutPolicy();
            verifySubPolicy(condInPolicy);
            verifySubPolicy(condOutPolicy);
            com.iovation.launchkey.sdk.transport.domain.Policy inPolicy = (com.iovation.launchkey.sdk.transport.domain.Policy) getTransportPolicyFromDomainPolicy(condInPolicy, true);
            com.iovation.launchkey.sdk.transport.domain.Policy outPolicy = (com.iovation.launchkey.sdk.transport.domain.Policy) getTransportPolicyFromDomainPolicy(condOutPolicy, true);
            transportPolicy = new com.iovation.launchkey.sdk.transport.domain.Policy(com.iovation.launchkey.sdk.transport.domain.Policy.TYPE_COND_GEO, denyRootedJailbroken, denyEmulatorSimulator, fences, null, null, inPolicy, outPolicy, null);
        } else if (domainPolicy instanceof MethodAmountPolicy) {
            MethodAmountPolicy methodAmountPolicy = (MethodAmountPolicy) domainPolicy;
            int amount = methodAmountPolicy.getAmount();
            transportPolicy = new com.iovation.launchkey.sdk.transport.domain.Policy(TYPE_METHOD_AMOUNT, denyRootedJailbroken, denyEmulatorSimulator, fences, null, amount, null, null, null);

        } else if (domainPolicy instanceof FactorsPolicy) {
            FactorsPolicy factorsPolicy = (FactorsPolicy) domainPolicy;
            ArrayNode factors = new ArrayNode(JsonNodeFactory.instance);
            if (factorsPolicy.isInherenceRequired()) {
                factors.add(FACTOR_INHERENCE);
            }
            if (factorsPolicy.isKnowledgeRequired()) {
                factors.add(FACTOR_KNOWLEDGE);
            }
            if (factorsPolicy.isPossessionRequired()) {
                factors.add(FACTOR_POSSESSION);
            }
            transportPolicy = new com.iovation.launchkey.sdk.transport.domain.Policy(
                    TYPE_FACTORS, denyRootedJailbroken,
                    denyEmulatorSimulator, fences, factors, null, null, null, null);

        } else if (domainPolicy instanceof LegacyPolicy) {
            final LegacyPolicy legacyPolicy = (LegacyPolicy) domainPolicy;
            final JsonNodeFactory jnf = new JsonNodeFactory(true);
            ArrayNode factors = new ArrayNode(jnf);
            if (legacyPolicy.getFences() != null && !legacyPolicy.getFences().isEmpty()) {
                ObjectNode geoFenceFactor = new ObjectNode(jnf);
                geoFenceFactor.set("factor", new TextNode("geofence"));
                geoFenceFactor.set("requirement", new TextNode("forced requirement"));
                geoFenceFactor.set("priority", new IntNode(1));
                ObjectNode geoFenceAttributes = new ObjectNode(jnf);
                geoFenceFactor.set("attributes", geoFenceAttributes);
                geoFenceAttributes.set("locations", new ArrayNode(jnf) {{
                    for (Fence fence : legacyPolicy.getFences()) {
                        GeoCircleFence geoFence = (GeoCircleFence) fence;
                        ObjectNode location = new ObjectNode(jnf);
                        if (geoFence.getName() != null) {
                            location.set("name", new TextNode(geoFence.getName()));
                        }
                        location.set("latitude", new DoubleNode(geoFence.getLatitude()));
                        location.set("longitude", new DoubleNode(geoFence.getLongitude()));
                        location.set("radius", new DoubleNode(geoFence.getRadius()));
                        add(location);
                    }
                }});
                factors.add(geoFenceFactor);
            }
            if (!legacyPolicy.getTimeFences().isEmpty()) {
                ObjectNode timeFenceFactor = new ObjectNode(jnf);
                factors.add(timeFenceFactor);
                timeFenceFactor.set("factor", new TextNode("timefence"));
                timeFenceFactor.set("requirement", new TextNode("forced requirement"));
                timeFenceFactor.set("priority", new IntNode(1));
                ObjectNode geoFenceAttributes = new ObjectNode(jnf);
                timeFenceFactor.set("attributes", geoFenceAttributes);
                geoFenceAttributes.set("time fences", new ArrayNode(jnf) {{
                    for (final LegacyPolicy.TimeFence timeFence : legacyPolicy.getTimeFences()) {
                        ObjectNode timeFenceNode = new ObjectNode(jnf);
                        timeFenceNode.set("name", new TextNode(timeFence.getName()));
                        timeFenceNode.set("days", new ArrayNode(jnf) {{
                            for (LegacyPolicy.Day day : timeFence.getDays()) {
                                add(day.toString());
                            }
                        }});
                        timeFenceNode.set("start hour", new IntNode(timeFence.getStartHour()));
                        timeFenceNode.set("end hour", new IntNode(timeFence.getEndHour()));
                        timeFenceNode.set("start minute", new IntNode(timeFence.getStartMinute()));
                        timeFenceNode.set("end minute", new IntNode(timeFence.getEndMinute()));
                        timeFenceNode.set("timezone", new TextNode(timeFence.getTimeZone().getID()));
                        add(timeFenceNode);
                    }
                }});
            }
            if (legacyPolicy.getDenyRootedJailbroken()) {
                ObjectNode deviceIntegrity = new ObjectNode(jnf);
                factors.add(deviceIntegrity);
                deviceIntegrity.set("factor", new TextNode("device integrity"));
                deviceIntegrity.set("requirement", new TextNode("forced requirement"));
                deviceIntegrity.set("priority", new IntNode(1));
                ObjectNode deviceIntegrityAttributes = new ObjectNode(jnf);
                deviceIntegrity.set("attributes", deviceIntegrityAttributes);
                deviceIntegrityAttributes.set("factor enabled", new IntNode(1));
            }
            List<MinimumRequirement> minimumRequirements =
                    new ArrayList<>();
            if (legacyPolicy.isPossessionRequired() != null && legacyPolicy.isPossessionRequired()
                    || legacyPolicy.isKnowledgeRequired() != null && legacyPolicy.isKnowledgeRequired()
                    || legacyPolicy.isInherenceRequired() != null && legacyPolicy.isInherenceRequired()
                    || legacyPolicy.getAmount() > 0) {
                minimumRequirements.add(new MinimumRequirement(
                        MinimumRequirement.Type.AUTHENTICATED,
                        legacyPolicy.getAmount(),
                        legacyPolicy.isKnowledgeRequired() != null && legacyPolicy.isKnowledgeRequired() ? 1 : 0,
                        legacyPolicy.isInherenceRequired() != null && legacyPolicy.isInherenceRequired() ? 1 : 0,
                        legacyPolicy.isPossessionRequired() != null && legacyPolicy.isPossessionRequired() ? 1 : 0
                ));
            }
            transportPolicy = new com.iovation.launchkey.sdk.transport.domain.Policy(
                    com.iovation.launchkey.sdk.transport.domain.Policy.TYPE_LEGACY, null, null, null, factors, null,
                    null, null, minimumRequirements);
        } else {
            throw new UnknownPolicyException("Unknown policy type", null, null);
        }
        return transportPolicy;
    }

    LegacyPolicy getLegacyPolicyFromServicePolicy(ServicePolicy servicePolicy) {
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
        int amount = (requiredFactors != null) ? requiredFactors : 0;
        Boolean inherence = servicePolicy.isInherenceFactorRequired();
        Boolean knownledge = servicePolicy.isKnowledgeFactorRequired();
        Boolean possession = servicePolicy.isPossessionFactorRequired();
        boolean denyRootedJailbroken = (servicePolicy.isJailBreakProtectionEnabled() != null) ? servicePolicy.isJailBreakProtectionEnabled() : false;
        List<LegacyPolicy.TimeFence> timeFences = new ArrayList<>();
        for (ServicePolicy.TimeFence servicePolicyTimefence : servicePolicy.getTimeFences()) {
            List<LegacyPolicy.Day> days = new ArrayList<>();
            for (ServicePolicy.Day day : servicePolicyTimefence.getDays()) {
                days.add(LegacyPolicy.Day.fromString(day.toString()));
            }
            timeFences.add(new LegacyPolicy.TimeFence(
                    servicePolicyTimefence.getName(),
                    days,
                    servicePolicyTimefence.getStartHour(),
                    servicePolicyTimefence.getStartMinute(),
                    servicePolicyTimefence.getEndHour(),
                    servicePolicyTimefence.getEndMinute(),
                    servicePolicyTimefence.getTimeZone())
            );
        }

        return new LegacyPolicy(amount, inherence, knownledge, possession, denyRootedJailbroken, geoFences, timeFences);
    }

    ServicePolicy getServicePolicyFromLegacyPolicy(LegacyPolicy legacyPolicy) {
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
        Boolean knowledgeFactorRequired = null;
        Boolean inherenceFactorRequired = null;
        Boolean possessionFactorRequired = null;
        if (legacyPolicy.isInherenceRequired() || legacyPolicy.isKnowledgeRequired() || legacyPolicy.isPossessionRequired()) {
            inherenceFactorRequired = legacyPolicy.isInherenceRequired();
            knowledgeFactorRequired = legacyPolicy.isKnowledgeRequired();
            possessionFactorRequired = legacyPolicy.isPossessionRequired();
        }
        Boolean jailBreakProtectionEnabled = legacyPolicy.getDenyRootedJailbroken() ? true : null;
        List<ServicePolicy.TimeFence> timeFences = new ArrayList<>();
        for (LegacyPolicy.TimeFence legacyPolicyTimefence : legacyPolicy.getTimeFences()) {
            List<ServicePolicy.Day> days = new ArrayList<>();
            for (LegacyPolicy.Day day : legacyPolicyTimefence.getDays()) {
                days.add(ServicePolicy.Day.fromString(day.toString()));
            }
            timeFences.add(new ServicePolicy.TimeFence(
                    legacyPolicyTimefence.getName(),
                    days,
                    legacyPolicyTimefence.getStartHour(),
                    legacyPolicyTimefence.getStartMinute(),
                    legacyPolicyTimefence.getEndHour(),
                    legacyPolicyTimefence.getEndMinute(),
                    legacyPolicyTimefence.getTimeZone())
            );
        }
        return new ServicePolicy(requiredFactors,
                knowledgeFactorRequired,
                inherenceFactorRequired,
                possessionFactorRequired,
                jailBreakProtectionEnabled,
                locations,
                timeFences);
    }

    private GeoCircleFence getGeoCircleFenceFromLocation(ServicePolicy.Location location) {
        return new GeoCircleFence(location.getName(), location.getLatitude(), location.getLongitude(), location.getRadius());
    }

    private ServicePolicy.Location getLocationFromGeoCircleFence(GeoCircleFence geoCircleFence) {
        return new ServicePolicy.Location(geoCircleFence.getName(), geoCircleFence.getRadius(), geoCircleFence.getLatitude(), geoCircleFence.getLongitude());
    }

    private Fence getDomainFenceFromTransportFence(com.iovation.launchkey.sdk.transport.domain.Fence transportFence) throws UnknownFenceTypeException {
        Fence domainFence = null;
        if (transportFence.getType().equals(com.iovation.launchkey.sdk.transport.domain.Fence.TYPE_GEO_CIRCLE)) {
            String name = transportFence.getName();
            double latitude = transportFence.getLatitude();
            double longitude = transportFence.getLongitude();
            double radius = transportFence.getRadius();
            domainFence = new GeoCircleFence(name, latitude, longitude, radius);
        } else if (transportFence.getType().equals(com.iovation.launchkey.sdk.transport.domain.Fence.TYPE_TERRITORY)) {
            String name = transportFence.getName();
            String adminArea = transportFence.getAdministrativeArea();
            String postalCode = transportFence.getPostalCode();
            String country = transportFence.getCountry();
            domainFence = new TerritoryFence(country, adminArea, postalCode, name);
        } else {
            throw new UnknownFenceTypeException("Unknown fence type", null, null);
        }
        return domainFence;
    }

    private com.iovation.launchkey.sdk.transport.domain.Fence getTransportFenceFromDomainFence(Fence domainFence) throws UnknownFenceTypeException {
        com.iovation.launchkey.sdk.transport.domain.Fence transportFence = null;
        if (domainFence instanceof GeoCircleFence) {
            transportFence = new com.iovation.launchkey.sdk.transport.domain.Fence(
                    domainFence.getName(),
                    com.iovation.launchkey.sdk.transport.domain.Fence.TYPE_GEO_CIRCLE,
                    ((GeoCircleFence) domainFence).getLatitude(),
                    ((GeoCircleFence) domainFence).getLongitude(),
                    ((GeoCircleFence) domainFence).getRadius(),
                    null, null, null);
        } else if (domainFence instanceof TerritoryFence) {
            transportFence = new com.iovation.launchkey.sdk.transport.domain.Fence(
                    domainFence.getName(),
                    com.iovation.launchkey.sdk.transport.domain.Fence.TYPE_TERRITORY,
                    null, null, null,
                    ((TerritoryFence) domainFence).getCountry(),
                    ((TerritoryFence) domainFence).getAdministrativeArea(),
                    ((TerritoryFence) domainFence).getPostalCode());
        } else {
            throw new UnknownFenceTypeException("Unknown fence type", null, null);
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
                throw new UnknownPolicyException("Inside or Outside Policy objects must have denyEmulatorSimulator or denyRootedJailbroken set to false", null, null);
            }
            if (subPolicy.getFences() != null) {
                throw new UnknownPolicyException("Fences are not supported on Inside or Outside Policy objects", null, null);
            }
        } else {
            throw new UnknownPolicyException("Inside or Outside Policy objects must be of type FactorsPolicy or MethodAmountPolicy, or null if no policy", null, null);
        }
    }
}
