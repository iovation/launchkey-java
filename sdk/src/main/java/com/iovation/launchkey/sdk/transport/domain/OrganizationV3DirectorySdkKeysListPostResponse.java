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

import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganizationV3DirectorySdkKeysListPostResponse {
    private final List<UUID> sdkKeys;

    @JsonCreator
    public OrganizationV3DirectorySdkKeysListPostResponse(@JsonProperty("sdk_keys") List<UUID> sdkKeys) {
        this.sdkKeys = sdkKeys;
    }

    public List<UUID> getSdkKeys() {
        return sdkKeys;
    }
}
