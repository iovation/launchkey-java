/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.service.error;

/**
 * This exception is thrown when a signed data packet is received from the Platform API and the signature
 * is not valid or does not match the package data.
 */
public class InvalidSignatureException extends ApiException {
    /**
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param code    HTTP status code or 0 if no HTTP status code was returned
     */
    public InvalidSignatureException(String message, int code) {
        super(message, code);
    }
}
