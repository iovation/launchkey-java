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

package com.launchkey.sdk.transport.v1.domain;

import java.util.Date;

/**
 * Request data for a "ping" call
 */
public class PingRequest {

    private final String fingerprint;

    public PingRequest(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public PingRequest() {
        this((String) null);
    }

    public String getFingerprint() {
        return fingerprint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PingRequest)) return false;

        PingRequest that = (PingRequest) o;

        return !(fingerprint != null ? !fingerprint.equals(that.fingerprint) : that.fingerprint != null);

    }

    @Override
    public int hashCode() {
        return fingerprint != null ? fingerprint.hashCode() : 0;
    }


    @Override
    public String toString() {
        return "PingRequest{" +
                "fingerprint='" + fingerprint + '\'' +
                '}';
    }
}
