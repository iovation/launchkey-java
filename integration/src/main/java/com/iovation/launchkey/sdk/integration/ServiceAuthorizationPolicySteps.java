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

package com.iovation.launchkey.sdk.integration;

import com.iovation.launchkey.sdk.domain.service.AuthPolicy;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;

import java.util.ArrayList;
import java.util.List;

public class ServiceAuthorizationPolicySteps {
    private final List<AuthPolicy.Location> locations;
    private Integer factors;
    private boolean inherence;
    private boolean knowledge;
    private boolean possession;

    public ServiceAuthorizationPolicySteps() {
        locations = new ArrayList<>();
        cleanUp();
    }

    @After
    public void cleanUp() {
        factors = null;
        inherence = false;
        knowledge = false;
        possession = false;
        locations.clear();

    }

    @Given("^the current Authorization Policy requires (\\d+) factors$")
    public void theCurrentAuthorizationPolicyRequiresFactors(int numberOfFactors) throws Throwable {
        factors = numberOfFactors;
    }

    @Given("^the current Authorization Policy requires inherence$")
    public void theCurrentAuthorizationPolicyRequiresInherence() throws Throwable {
        inherence = true;
    }

    @And("^the current Authorization Policy requires knowledge$")
    public void theCurrentAuthorizationPolicyRequiresKnowledge() throws Throwable {
        knowledge = true;
    }

    @And("^the current Authorization Policy requires possession$")
    public void theCurrentAuthorizationPolicyRequiresPossession() throws Throwable {
        possession = true;
    }

    @Given("^the current Authorization Policy requires a geofence with a radius of (\\d+\\.\\d+), a latitude of (-?\\d+\\.\\d+), and a longitude of (-?\\d+\\.\\d+)$")
    public void theCurrentAuthorizationPolicyRequiresAGeofenceOf(double radius, double latitude, double longitude)
            throws Throwable {
        this.locations.add(new AuthPolicy.Location(radius, latitude, longitude));
    }

    AuthPolicy getCurrentAuthPolicy() {
        AuthPolicy policy;
        if (factors != null) {
          policy = new AuthPolicy(factors, locations);
        } else {
            policy = new AuthPolicy(knowledge, inherence, possession, locations);
        }
        return policy;
    }

    class GeoFence {
        private final double radius;
        private final double latitude;
        private final double longitude;

        GeoFence(double radius, double latitude, double longitude) {
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
    }
}
