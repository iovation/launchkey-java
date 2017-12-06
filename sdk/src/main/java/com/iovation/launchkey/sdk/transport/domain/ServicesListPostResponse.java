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

public class ServicesListPostResponse {

    private final List<ServicesListPostResponseService> services;

    public ServicesListPostResponse(List<ServicesListPostResponseService> services) {
        this.services = services;
    }

    public List<ServicesListPostResponseService> getServices() {
        return services;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServicesListPostResponse)) return false;

        ServicesListPostResponse that = (ServicesListPostResponse) o;

        return services != null ? services.equals(that.services) : that.services == null;
    }

    @Override
    public String toString() {
        return "ServicesListPostResponse{" +
                "services=" + services +
                '}';
    }
}
