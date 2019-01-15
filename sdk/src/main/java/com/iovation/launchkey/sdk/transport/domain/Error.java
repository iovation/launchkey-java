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

package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Error {
    private final String errorCode;
    private final Object errorDetail;
    private final Object errorData;

    @JsonCreator
    public Error(
            @JsonProperty(value = "error_code") String errorCode,
            @JsonProperty(value = "error_detail") Object errorDetail,
            @JsonProperty(value = "error_data") Object errorData
    ) {
        this.errorCode = errorCode;
        this.errorDetail = errorDetail;
        this.errorData = errorData;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Object getErrorDetail() {
        return errorDetail;
    }

    public Object getErrorData() {
        return errorData;
    }
}
