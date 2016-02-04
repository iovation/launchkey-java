/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response data from "users" call
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UsersResponse {

    /**
     * Inner class required to parse response from "users" call
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class UsersResponseResponse {
        private final String cipher;
        private final String data;

        @JsonCreator
        public UsersResponseResponse(@JsonProperty("cipher") String cipher, @JsonProperty("data") String data) {
            this.cipher = cipher;
            this.data = data;
        }

        @JsonCreator
        public UsersResponseResponse(String emptyData) {
            this.cipher = null;
            this.data = null;
        }

        public final String getCipher() {
            return cipher;
        }

        public final String getData() {
            return data;
        }
    }

    /**
     * Was the request successful
     */
    private final Boolean successful;

    /**
     * The message code for the request
     */
    private final Integer messageCode;

    /**
     * A descriptive message if there was an error
     */
    private final String message;

    /**
     * Base64 encoded RSA encrypted JSON string. Once Base64 decoded, decrypt the result with the private key of RSA
     * public/private key pair associated with the Rocket whose Rocket Key was included in the request. The decrypted
     * string will be a forty-eight (48) character AES cipher string. The first thirty-two (32) characters of the
     * cipher string are the cipher key and the remaining sixteen (16) characters are the cipher IV. The cipher key
     * and IV will be used to decrypt the data attribute. For example, the decrypted cipher string of
     * "myciphermyciphermyciphermycipheriviviviviviviviv" would result in a cipher key of
     * "myciphermyciphermyciphermycipher" and a cipher IV of "iviviviviviviviv".
     */
    private final String cipher;

    /**
     * Base64 encoded AES encrypted JSON string. Once Base64 decoded, decrypt the result with AES in
     * term:CBC mode utilizing the key and IV values extrapolated from the cipher attribute.
     */
    private final String data;

    /**
     *
     * @param response Intermediary transport class for parsing response from "users" call
     * @param successful Was the request successful
     * @param messageCode The message code for the request
     * @param message A descriptive message if there was an error
     */

    @JsonCreator
    public UsersResponse(
            @JsonProperty("response") UsersResponseResponse response,
            @JsonProperty("successful") Boolean successful,
            @JsonProperty("message_code") Integer messageCode,
            @JsonProperty("message") String message
    ) {
        this.cipher = (response == null ? null : response.getCipher());
        this.data = (response == null ? null : response.getData());
        this.successful = successful;
        this.messageCode = messageCode;
        this.message = message;
    }

    /**
     * @return Base64 encoded RSA encrypted JSON string. Once Base64 decoded, decrypt the result with the private key of RSA
     * public/private key pair associated with the Rocket whose Rocket Key was included in the request. The decrypted
     * string will be a forty-eight (48) character AES cipher string. The first thirty-two (32) characters of the
     * cipher string are the cipher key and the remaining sixteen (16) characters are the cipher IV. The cipher key
     * and IV will be used to decrypt the data attribute. For example, the decrypted cipher string of
     * "myciphermyciphermyciphermycipheriviviviviviviviv" would result in a cipher key of
     * "myciphermyciphermyciphermycipher" and a cipher IV of "iviviviviviviviv".
     */
    public String getCipher() {
        return cipher;
    }

    /**
     * @return Base64 encoded AES encrypted JSON string. Once Base64 decoded, decrypt the result with AES in
     * term:CBC mode utilizing the key and IV values extrapolated from the cipher attribute.
     */
    public String getData() {
        return data;
    }

    /**
     * Is the response for a successful request
     * @return
     */
    public Boolean isSuccessful() {
        return successful;
    }

    /**
     * Get the message code for the response
     * @return
     */
    public Integer getMessageCode() {
        return messageCode;
    }

    /**
     * Get the message for the request
     * @return
     */
    public String getMessage() {
        return message;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsersResponse)) return false;

        UsersResponse that = (UsersResponse) o;

        if (cipher != null ? !cipher.equals(that.cipher) : that.cipher != null) return false;
        return !(data != null ? !data.equals(that.data) : that.data != null);

    }

    @Override
    public int hashCode() {
        int result = successful != null ? successful.hashCode() : 0;
        result = 31 * result + (messageCode != null ? messageCode.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (cipher != null ? cipher.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UsersResponse{" +
                "successful=" + successful +
                ", messageCode=" + messageCode +
                ", message='" + message + '\'' +
                ", cipher='" + cipher + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
