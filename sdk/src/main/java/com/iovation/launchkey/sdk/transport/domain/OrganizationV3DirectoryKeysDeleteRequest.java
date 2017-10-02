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

@JsonPropertyOrder({"directory_id", "key_id"})
public class OrganizationV3DirectoryKeysDeleteRequest {
    private final UUID directoryId;
    private final String keyId;

    public OrganizationV3DirectoryKeysDeleteRequest(UUID directoryId, String keyId) {
        this.directoryId = directoryId;
        this.keyId = keyId;
    }

    @JsonProperty("directory_id")
    public UUID getDirectoryId() {
        return directoryId;
    }

    @JsonProperty("key_id")
    public String getKeyId() {
        return keyId;
    }
}
