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

package com.launchkey.sdk.transport.v1.domain;

import com.fasterxml.jackson.databind.JsonNode;

public class ErrorResponse {
    /**
     * The numeric HTTP status code for the response
     */
    private final int statusCode;

    /**
     * The message code for the response.
     */
    private final int messageCode;

    /**
     * Message associated with message_code
     */
    private final String message;

    public ErrorResponse(
            int statusCode,
            int messageCode,
            String message
    ) {
        this.statusCode = statusCode;
        this.messageCode = messageCode;
        this.message = message;
    }

    public static ErrorResponse factory(JsonNode node) {
        int statusCode = node.has("status_code") ? node.get("status_code").intValue() : 0;
        int messageCode = node.has("message_code") ? node.get("message_code").intValue() : 0;
        JsonNode messageNode = node.get("message");
        String message;

        if (messageNode == null) {
            message = null;
        } else if (messageNode.isValueNode()) {
            message = messageNode.textValue();
        } else {
            message = messageNode.toString();
        }
        return new ErrorResponse(statusCode, messageCode, message);
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
    public int getMessageCode() {
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
        if (messageCode != that.messageCode) return false;
        return !(message != null ? !message.equals(that.message) : that.message != null);

    }

    @Override public int hashCode() {
        int result = statusCode;
        result = 31 * result + messageCode;
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
