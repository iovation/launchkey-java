/**
 * Copyright 2016 LaunchKey, Inc.  All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.transport.v1.domain.Policy;

import java.util.List;

public class GeoFenceFactor extends Factor {
    private final GeoFenceAttribute attributes;

    public GeoFenceFactor(List<Location> locations, boolean quickFail, FactorRequirementType requirement, int priority) {
        super(quickFail, requirement, priority);
        attributes = new GeoFenceAttribute(locations);
    }

    public GeoFenceFactor(List<Location> locations) {
        super();
        attributes = new GeoFenceAttribute(locations);
    }

    @Override public FactorType getType() {
        return FactorType.GEOFENCE;
    }

    @Override public FactoryCategory getCategory() {
        return FactoryCategory.INHERENCE;
    }

    public class GeoFenceAttribute implements Attribute {

        private List<Location> locations;

        public GeoFenceAttribute(List<Location> locations) {
            if (locations == null || locations.size() < 1) {
                throw new IllegalArgumentException("Locations must be a list with at least one location");
            }
            this.locations = locations;
        }

        @Override
        public FactorType getType() {
            return FactorType.GEOFENCE;
        }

        public List<Location> getLocations() {
            return locations;
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof GeoFenceAttribute)) return false;

            GeoFenceAttribute that = (GeoFenceAttribute) o;

            return locations.equals(that.locations);

        }

        @Override public int hashCode() {
            return locations.hashCode();
        }

    }
    public static final class Location {
        private final double radius;
        private final double latitude;
        private final double longitude;

        public Location(double radius, double latitude, double longitude) {
            this.radius = radius;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getRadius() {
            return radius;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Location)) return false;

            Location location = (Location) o;

            if (Double.compare(location.radius, radius) != 0) return false;
            if (Double.compare(location.latitude, latitude) != 0) return false;
            return Double.compare(location.longitude, longitude) == 0;
        }

        @Override public int hashCode() {
            int result;
            long temp;
            temp = Double.doubleToLongBits(radius);
            result = (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(latitude);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(longitude);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }
}
