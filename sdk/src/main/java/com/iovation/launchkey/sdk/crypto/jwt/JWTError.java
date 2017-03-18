/**
 * Copyright 2017 iovation, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iovation.launchkey.sdk.crypto.jwt;

/**
 * Error thrown when a problem arises from attempting top encode or decode a JWT
 */
public class JWTError extends Exception {
    /**
     * @see Exception#Exception(String, Throwable)
     * @param message Error message
     * @param cause Underlying cause of the error
     */
    public JWTError(String message, Throwable cause) {
        super(message, cause);
    }
}
