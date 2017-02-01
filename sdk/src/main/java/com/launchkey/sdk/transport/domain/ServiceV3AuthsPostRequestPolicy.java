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

package com.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.tools.jdi.LinkedHashMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(value = {"geoFenceLocations"})
public class ServiceV3AuthsPostRequestPolicy {
    private final Map<String, Object> minimumRequirements;
    private final List<Map<String, Double>> geoFenceLocations;

    public ServiceV3AuthsPostRequestPolicy(int any, boolean knowledge, boolean inherence, boolean possession) {
        this.minimumRequirements = new LinkedHashMap();
        this.minimumRequirements.put("requirement", "authenticated");
        this.minimumRequirements.put("any", Integer.valueOf(any));
        this.minimumRequirements.put("knowledge", knowledge ? Integer.valueOf(1) : Integer.valueOf(0));
        this.minimumRequirements.put("inherence", inherence ? Integer.valueOf(1) : Integer.valueOf(0));
        this.minimumRequirements.put("possession", possession ? Integer.valueOf(1) : Integer.valueOf(0));

        this.geoFenceLocations = new ArrayList<Map<String, Double>>();
    }

    public void addGeoFence(double radius, double latitude, double longitude) {
        Map<String, Double> location = new LinkedHashMap();
        location.put("radius", radius);
        location.put("latitude", latitude);
        location.put("longitude", longitude);
        geoFenceLocations.add(location);
    }


    @JsonProperty(value = "minimum_requirements")
    public Map<String, Object> getMinimumRequirements() {
        return minimumRequirements;
    }

    @JsonProperty(value = "factors")
    public List<Map<String, Object>> getFactors() {
        Map<String, Object> factor = new LinkedHashMap();
        factor.put("factor", "geofence");
        factor.put("requirement", "forced requirement");
        factor.put("priority", Integer.valueOf(1));
        Map<String, Object> attributes = new LinkedHashMap();
        factor.put("attributes", attributes);
        attributes.put("locations", geoFenceLocations);
        return Arrays.asList(factor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceV3AuthsPostRequestPolicy)) return false;

        ServiceV3AuthsPostRequestPolicy that = (ServiceV3AuthsPostRequestPolicy) o;

        if (getMinimumRequirements() != null ? !getMinimumRequirements().equals(that.getMinimumRequirements()) : that.getMinimumRequirements() != null)
            return false;
        return geoFenceLocations != null ? geoFenceLocations.equals(that.geoFenceLocations) : that.geoFenceLocations == null;
    }

    @Override
    public int hashCode() {
        int result = getMinimumRequirements() != null ? getMinimumRequirements().hashCode() : 0;
        result = 31 * result + (geoFenceLocations != null ? geoFenceLocations.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceV3AuthsPostRequestPolicy{" +
                "minimumRequirements=" + minimumRequirements +
                ", geoFenceLocations=" + geoFenceLocations +
                '}';
    }
}
