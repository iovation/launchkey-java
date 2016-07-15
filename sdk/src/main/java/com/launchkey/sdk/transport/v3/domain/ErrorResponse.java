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

package com.launchkey.sdk.transport.v3.domain;

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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {
    /**
     * The numeric HTTP status code for the response
     */
    private final int statusCode;

    /**
     * The message code for the response.
     */
    private final String messageCode;

    /**
     * Message associated with message_code
     */
    private final String message;

    @JsonCreator
    public ErrorResponse(
            @JsonProperty(value = "status_code") int statusCode,
            @JsonProperty(value = "message_code", required = true) String messageCode,
            @JsonProperty(value = "message", required = true) String message
    ) {
        this.statusCode = statusCode;
        this.messageCode = messageCode;
        this.message = message;
    }

    /**
     * Get the numeric HTTP status code for the response
     * @return HTTP status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Get the message code for the response.
     * @return Message code
     */
    public String getMessageCode() {
        return messageCode;
    }

    /**
     * Get the message associated with the message code for the response.
     * @return message
     */
    public String getMessage() {
        return message;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorResponse)) return false;

        ErrorResponse that = (ErrorResponse) o;

        if (statusCode != that.statusCode) return false;
        if (messageCode != null ? !messageCode.equals(that.messageCode) : that.messageCode != null) return false;
        return !(message != null ? !message.equals(that.message) : that.message != null);

    }

    @Override public int hashCode() {
        int result = statusCode;
        result = 31 * result + (messageCode != null ? messageCode.hashCode() : 0);;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override public String toString() {
        return "ErrorResponse{" +
                "statusCode=" + statusCode +
                ", messageCode=" + messageCode +
                ", message='" + message + '\'' +
                '}';
    }
}
