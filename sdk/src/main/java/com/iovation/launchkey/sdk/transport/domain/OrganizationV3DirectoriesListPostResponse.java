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

public class OrganizationV3DirectoriesListPostResponse {
    private final List<OrganizationV3DirectoriesListPostResponseDirectory> directories;

    public OrganizationV3DirectoriesListPostResponse(
            List<OrganizationV3DirectoriesListPostResponseDirectory> directories) {
        this.directories = directories;
    }

    public List<OrganizationV3DirectoriesListPostResponseDirectory> getDirectories() {
        return directories;
    }
}
