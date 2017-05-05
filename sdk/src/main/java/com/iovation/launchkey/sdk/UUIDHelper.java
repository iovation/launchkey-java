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

package com.iovation.launchkey.sdk;

import java.util.UUID;

public class UUIDHelper {
    /**
     * Validate the provided uuid string and return a {@link UUID} if string is a valid UUID with the correction
     * version or throw {@link IllegalArgumentException} when that criteria is not met.
     *
     * @param uuid String representation of a UUID
     * @param version Version expected (1-4)
     * @return UUID
     * @throws IllegalArgumentException When the UUID string is not a valid UUID or the correct version
     */
    public static final UUID fromString(String uuid, int version) throws IllegalArgumentException {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }
        UUID uuidObject = UUID.fromString(uuid);
        validateVersion(uuidObject, version);

        return uuidObject;
    }

    public static void validateVersion(UUID uuid, int version) {
        if (uuid.version() != version) {
            throw new IllegalArgumentException(
                    "UUID version expected to be " + String.valueOf(version) +
                            " but was UUID" + String.valueOf(uuid.version())
            );
        }
    }
}
