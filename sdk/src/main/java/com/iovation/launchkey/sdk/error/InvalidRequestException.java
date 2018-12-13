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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.iovation.launchkey.sdk.transport.domain.Error;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * This exception is thrown when the data sent to the API was not the correct data for the endpoint.
 */
public class InvalidRequestException extends CommunicationErrorException {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final static Pattern STRING_JSON_PATTERN = Pattern.compile("^\"[^\"]*\"$");

    private final static Map<String, Class<? extends InvalidRequestException>> ERROR_MAP = new ConcurrentHashMap<>();

    static {
        ERROR_MAP.put("ARG-001", InvalidParameters.class);
        ERROR_MAP.put("ARG-002", InvalidRoute.class);
        ERROR_MAP.put("SVC-001", ServiceNameTaken.class);
        ERROR_MAP.put("SVC-002", InvalidPolicyInput.class);
        ERROR_MAP.put("SVC-003", PolicyFailure.class);
        ERROR_MAP.put("SVC-004", ServiceNotFound.class);
        ERROR_MAP.put("SVC-005", AuthorizationInProgress.class);
        ERROR_MAP.put("DIR-001", InvalidDirectoryIdentifier.class);
        ERROR_MAP.put("KEY-001", InvalidPublicKey.class);
        ERROR_MAP.put("KEY-002", PublicKeyAlreadyInUse.class);
        ERROR_MAP.put("KEY-003", PublicKeyDoesNotExist.class);
        ERROR_MAP.put("KEY-004", LastRemainingKey.class);
        ERROR_MAP.put("ORG-003", DirectoryNameInUse.class);
        ERROR_MAP.put("ORG-005", LastRemainingSDKKey.class);
        ERROR_MAP.put("ORG-006", InvalidSDKKey.class);
        OBJECT_MAPPER.setDateFormat(new StdDateFormat());
    }


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

    public static InvalidRequestException fromError(Error error) {
        String errorMessage;
        try {
            errorMessage = OBJECT_MAPPER.writeValueAsString(error.getErrorDetail());
            if (STRING_JSON_PATTERN.matcher(errorMessage).find()) {
                errorMessage = errorMessage.substring(1, errorMessage.length() - 1);
            }
        } catch (IOException e) {
            return new InvalidRequestException("Unable to parse error in response", e, null);
        }

        Class<? extends InvalidRequestException> clazz = ERROR_MAP.get(error.getErrorCode());
        if (clazz == null) clazz = InvalidRequestException.class;
        try {
            if (clazz == AuthorizationInProgress.class) {
                String authorizationRequestId;
                Date expires;
                Boolean myAuthorizationRequest;
                Map<Object, Object> errorData;

                try {
                    errorData = (Map<Object, Object>) error.getErrorData();
                } catch (ClassCastException e) {
                    errorData = null;
                }

                if (errorData == null) {
                    authorizationRequestId = null;
                    expires = null;
                    myAuthorizationRequest = null;
                } else {
                    authorizationRequestId = (String) errorData.get("auth_request");
                    try {
                        myAuthorizationRequest = (Boolean) errorData.get("from_same_service");
                    } catch (ClassCastException e) {
                        myAuthorizationRequest = false;
                    }

                    String expiresString = (String) errorData.get("expires");
                    if (expiresString == null) {
                        expires = null;
                    } else {
                        try {
                            Calendar date = DatatypeConverter.parseDateTime(expiresString);
                            expires = date.getTime();
                        } catch (IllegalArgumentException e) {
                            expires = null;
                        }
                    }
                }

                return new AuthorizationInProgress(errorMessage, null, error.getErrorCode(), authorizationRequestId,
                        myAuthorizationRequest != null && myAuthorizationRequest, expires);
            } else {

                Constructor<? extends InvalidRequestException>
                        c = clazz.getDeclaredConstructor(String.class, Throwable.class, String.class);
                c.setAccessible(true);
                return c.newInstance(errorMessage, null, error.getErrorCode());
            }
        } catch (NoSuchMethodException|IllegalAccessException|InstantiationException|InvocationTargetException e) {
            return new InvalidRequestException(errorMessage, null, error.getErrorCode());
        }
    }
}
