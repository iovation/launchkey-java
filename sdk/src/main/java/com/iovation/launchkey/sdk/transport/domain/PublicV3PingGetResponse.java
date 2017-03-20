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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * The response object for the /public/v3/ping call.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PublicV3PingGetResponse {
    private final Date apiTime;

    /**
     * @param apiTime The current time of the LaunchKey API as reported.
     */
    @JsonCreator
    public PublicV3PingGetResponse(@JsonProperty(value = "api_time", required = true) Date apiTime) {
        this.apiTime = apiTime;
    }

    /**
     * Get the current time of the LaunchKey API as reported.
     * @return The current time of the LaunchKey API as reported.
     */
    @JsonProperty("api_time")
    public Date getApiTime() {
        return apiTime;
    }
}
