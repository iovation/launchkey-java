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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class DirectoryV3DevicesPostRequest {
    public final String identifier;
    private final Integer ttl;

    public DirectoryV3DevicesPostRequest(String identifier, Integer ttl) {
        this.identifier = identifier;
        this.ttl = ttl;
    }

    @JsonProperty("identifier")
    public String getIdentifier() {
        return identifier;
    }

    @JsonProperty("ttl")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Integer getTTL() {
        return ttl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectoryV3DevicesPostRequest)) return false;
        DirectoryV3DevicesPostRequest that = (DirectoryV3DevicesPostRequest) o;
        return Objects.equals(getIdentifier(), that.getIdentifier()) &&
                Objects.equals(getTTL(), that.getTTL());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdentifier(), getTTL());
    }

    @Override
    public String toString() {
        return "DirectoryV3DevicesPostRequest{" +
                "identifier='" + identifier + '\'' +
                ", ttl=" + ttl +
                '}';
    }
}
