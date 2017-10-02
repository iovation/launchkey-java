/**
 * Copyright 2017 iovation, Inc. All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iovation.launchkey.sdk.error;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This exception is thrown when the data sent to the API was not the correct data for the endpoint.
 */
public class InvalidRequestException extends CommunicationErrorException {

    /**
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @param  errorCode The error code received from the Platform API. It will be null if the error was not receive
     *                   from the Platform API.
     */
    public InvalidRequestException(String message, Throwable cause, String errorCode) {
        super(message, cause, errorCode);
    }
    private final static Map<String, Class<? extends InvalidRequestException>> ERROR_MAP = new ConcurrentHashMap<>();
    static {
        ERROR_MAP.put("ARG-001", InvalidParameters.class);
        ERROR_MAP.put("ARG-002", InvalidRoute.class);
        ERROR_MAP.put("SVC-001", ServiceNameTaken.class);
        ERROR_MAP.put("SVC-002", InvalidPolicyInput.class);
        ERROR_MAP.put("SVC-003", PolicyFailure.class);
        ERROR_MAP.put("SVC-004", ServiceNotFound.class);
        ERROR_MAP.put("DIR-001", InvalidDirectoryIdentifier.class);
        ERROR_MAP.put("KEY-001", InvalidPublicKey.class);
        ERROR_MAP.put("KEY-002", PublicKeyAlreadyInUse.class);
        ERROR_MAP.put("KEY-003", PublicKeyDoesNotExist.class);
        ERROR_MAP.put("KEY-004", LastRemainingKey.class);
        ERROR_MAP.put("ORG-003", DirectoryNameInUse.class);
        ERROR_MAP.put("ORG-005", LastRemainingSDKKey.class);
        ERROR_MAP.put("ORG-006", InvalidSDKKey.class);
    }
    public static InvalidRequestException fromErrorCode(String errorCode, String errorMessage) {
        Class<? extends InvalidRequestException> clazz = ERROR_MAP.get(errorCode);
        if (clazz == null) clazz = InvalidRequestException.class;
        try {
            Constructor<? extends InvalidRequestException>
                    c = clazz.getDeclaredConstructor(String.class, Throwable.class, String.class);
            c.setAccessible(true);
            return c.newInstance(errorMessage, null, errorCode);
        } catch (NoSuchMethodException|IllegalAccessException|InstantiationException|InvocationTargetException e) {
            return new InvalidRequestException(errorMessage, null, errorCode);
        }
    }
}
