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

import java.security.PublicKey;
import java.util.Date;

public interface JWTService {
    /**
     * Encode A JWT based on the provided claim data
     *
     * @param jti Token ID
     * @param issuer JWT issuer
     * @param subject JWT subject
     * @param method Request method
     * @param path Request path
     * @param contentHashAlgorithm Hashing algorithm used to create the contentHash from the content
     * @param contentHash Content hashed using the contentHashingAlgorithm
     * @return Compact serialization encoded JWT
     * @throws JWTError When an error occurs encoding the JWT
     */
    String encode(String jti, String issuer, String subject, Date currentTime, String method, String path,
                  String contentHashAlgorithm, String contentHash) throws JWTError;

    /**
     * Decode the provided JWT string in the a claims object
     *
     * @param publicKey Public Key to verify JWT signature
     * @param expectedAudience Audience identifier expected in response
     * @param expectedTokenId Expected Token ID
     * @param jwt Compact serialization encoded JWT
     * @return Claims object
     * @throws JWTError When an error occurs decoding the JWT
     */
    JWTClaims decode(PublicKey publicKey, String expectedAudience, String expectedTokenId, Date currentTime, String jwt) throws JWTError;

    /**
     * Get the kid value from the header of the JWT
     * @param jwt Get the data from the JWT without verifying or validating
     * @return JWT data from claims and header
     * @throws JWTError When an error occurs parsing the JWT
     */
    JWTData getJWTData(String jwt) throws JWTError;
}
