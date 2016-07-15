/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.crypto.jwt;

public interface JWTService {
    /**
     * Encode A JWT based on the provided claim data
     * @param jti Token ID
     * @param method Request method
     * @param path Request path
     * @param contentHashAlgorithm Hashing algorithm used to create the contentHash from the content
     * @param contentHash Content hashed using the contentHashingAlgorithm
     * @return Compact serialization encoded JWT
     * @throws JWTError
     */
    String encode(
            String jti, String method, String path, String contentHashAlgorithm, String contentHash
    ) throws JWTError;

    /**
     * Decode the provided JWT string in the a claims object
     * @param jwt
     * @return Claims object
     * @throws JWTError
     */
    JWTClaims decode(String jwt) throws JWTError;
}
