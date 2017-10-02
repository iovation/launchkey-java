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

package com.iovation.launchkey.sdk.transport.domain;

import java.util.List;

public class ServicesGetResponse {

    private final List<ServicesGetResponseService> services;

    public ServicesGetResponse(List<ServicesGetResponseService> services) {
        this.services = services;
    }

    public List<ServicesGetResponseService> getServices() {
        return services;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServicesGetResponse)) return false;

        ServicesGetResponse response = (ServicesGetResponse) o;

        return services != null ? services.equals(response.services) : response.services == null;
    }

    @Override
    public String toString() {
        return "ServicesGetResponse{" +
                "services=" + services +
                '}';
    }
}
