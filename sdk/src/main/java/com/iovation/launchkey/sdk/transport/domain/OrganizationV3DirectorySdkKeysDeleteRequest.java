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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.UUID;

@JsonPropertyOrder({"directory_id", "sdk_key"})
public class OrganizationV3DirectorySdkKeysDeleteRequest {
    private final UUID directoryId;
    private final UUID sdkKey;

    public OrganizationV3DirectorySdkKeysDeleteRequest(UUID directoryId, UUID sdkKey) {
        this.directoryId = directoryId;
        this.sdkKey = sdkKey;
    }

    @JsonProperty("directory_id")
    public UUID getDirectoryId() {
        return directoryId;
    }

    @JsonProperty("sdk_key")
    public UUID getSdkKey() {
        return sdkKey;
    }
}
