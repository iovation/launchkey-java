/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
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
    /**
     * When the current public key was generated. Returned only if a key is returned.
     */
    private final String dateStamp;

    private final String fingerprint;

    /**
     * @param dateStamp When the current public key was generated. Returned only if a key is returned.
     * @deprecated Use {@link PingRequest(String)} and {@link #getFingerprint()} instead
     */
    @Deprecated
    public PingRequest(Date dateStamp) {
        this.fingerprint = null;
        this.dateStamp = dateStamp == null ? null : PlatformDateFormat.getInstance().format(dateStamp);
    }

    public PingRequest(String fingerprint) {
        this.fingerprint = fingerprint;
        this.dateStamp = null;
    }

    public PingRequest() {
        this((String) null);
    }

    public String getFingerprint() {
        return fingerprint;
    }

    /**
     * @deprecated Use {@link PingRequest(String)} and {@link #getFingerprint()} instead
     * @return
     */
    @Deprecated
    public String getDateStamp() {
        return this.dateStamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PingRequest)) return false;

        PingRequest that = (PingRequest) o;

        return !(dateStamp != null ? !dateStamp.equals(that.dateStamp) : that.dateStamp != null);

    }

    @Override
    public int hashCode() {
        int hash = dateStamp != null ? dateStamp.hashCode() : 0;
        hash += fingerprint != null ? fingerprint.hashCode() : 0;
        return hash;
    }


    @Override
    public String toString() {
        return "PingRequest{" +
                "dateStamp='" + dateStamp + '\'' +
                "fingerprint='" + fingerprint + '\'' +
                '}';
    }
}
