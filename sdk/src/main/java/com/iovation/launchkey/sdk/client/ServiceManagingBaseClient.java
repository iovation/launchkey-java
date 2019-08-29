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
import com.iovation.launchkey.sdk.transport.domain.AuthPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

class ServiceManagingBaseClient {
    ServicePolicy getDomainServicePolicyFromTransportServicePolicy(
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

    com.iovation.launchkey.sdk.transport.domain.ServicePolicy getTransportServicePolicyFromDomainServicePolicy(
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

    Policy getDomainPolicyFromTransportPolicy(
            com.iovation.launchkey.sdk.transport.domain.Policy transportPolicy) {

        String policyType = transportPolicy.getPolicyType();
        Boolean denyRootedJailbroken = transportPolicy.getDenyRootedJailbroken();
        Boolean denyEmulatorSimulator = transportPolicy.getDenyEmulatorSimulator();
        List<Fence> fences = transportPolicy.getFences();
        Policy domainPolicy = null;

        if (policyType.equals("COND_GEO")) {
            Policy inPolicy = getDomainPolicyFromTransportPolicy(transportPolicy.getInPolicy());
            Policy outPolicy = getDomainPolicyFromTransportPolicy(transportPolicy.getOutPolicy());
            // TODO: Throw error if policy wrong type
            domainPolicy = new ConditionalGeoFencePolicy(denyRootedJailbroken,denyEmulatorSimulator,fences,inPolicy,outPolicy);
        }
        else if (policyType.equals("METHOD_AMOUNT")) {
            domainPolicy = new MethodAmountPolicy(denyRootedJailbroken,denyEmulatorSimulator,fences);
        }
        else if (policyType.equals("FACTORS")) {
            List<String> transportFactors = transportPolicy.getFactors();
            List<Factor> factors = new ArrayList<>();
            for (String factor : transportFactors) {
                factors.add(Factor.valueOf(factor));
            }
            domainPolicy = new FactorsPolicy(denyRootedJailbroken,denyEmulatorSimulator,fences,factors);
        }
        return domainPolicy;
    }

    com.iovation.launchkey.sdk.transport.domain.Policy getTransportPolicyFromDomainPolicy(Policy domainPolicy) {

        String policyType = domainPolicy.getPolicyType();
        Boolean denyRootedJailbroken = domainPolicy.getDenyRootedJailbroken();
        Boolean denyEmulatorSimulator = domainPolicy.getDenyEmulatorSimulator();
        List<Fence> fences = domainPolicy.getFences();
        com.iovation.launchkey.sdk.transport.domain.Policy inPolicy = null;
        com.iovation.launchkey.sdk.transport.domain.Policy outPolicy = null;
        List<String> factors = new ArrayList<>();

        if (domainPolicy instanceof ConditionalGeoFencePolicy) {
            ConditionalGeoFencePolicy condGeoPolicy = (ConditionalGeoFencePolicy) domainPolicy;
            Policy condInPolicy = condGeoPolicy.getInPolicy();
            Policy condOutPolicy = condGeoPolicy.getOutPolicy();
            // TODO: Throw error if policy wrong type
            inPolicy = getTransportPolicyFromDomainPolicy(condInPolicy);
            outPolicy = getTransportPolicyFromDomainPolicy(condOutPolicy);
        }
        else if (domainPolicy instanceof MethodAmountPolicy) {
            // TODO: Fill in or remove
        }
        else if (domainPolicy instanceof FactorsPolicy) {
            List<Factor> domainFactors = ((FactorsPolicy) domainPolicy).getFactors();
            for (Factor factor : domainFactors) {
                factors.add(factor.toString());
            }
        }

        return new com.iovation.launchkey.sdk.transport.domain.Policy(denyRootedJailbroken,denyEmulatorSimulator,fences,inPolicy,outPolicy,policyType,factors);
    }
}
