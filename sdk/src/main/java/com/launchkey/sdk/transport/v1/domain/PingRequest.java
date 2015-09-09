/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
 *
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.transport.v1.domain;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Request data for a "ping" call
 */
public class PingRequest {
    /**
     * When the current public key was generated. Returned only if a key is returned.
     */
    private final String dateStamp;

    /**
     * @param dateStamp When the current public key was generated. Returned only if a key is returned.
     */
    public PingRequest(Date dateStamp) {
        this.dateStamp = dateStamp == null ? null : LaunchKeyDateFormat.getInstance().format(dateStamp);
    }

    public PingRequest() {
        this(null);
    }

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
        return dateStamp != null ? dateStamp.hashCode() : 0;
    }


    @Override
    public String toString() {
        return "PingRequest{" +
                "dateStamp='" + dateStamp + '\'' +
                '}';
    }
}
