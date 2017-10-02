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

import java.util.UUID;

public class OrganizationV3DirectorySdkKeysListPostRequest {
    private final UUID directoryId;

    public OrganizationV3DirectorySdkKeysListPostRequest(UUID directoryId) {
        this.directoryId = directoryId;
    }

    @JsonProperty("directory_id")
    public UUID getDirectoryId() {
        return directoryId;
    }
}
