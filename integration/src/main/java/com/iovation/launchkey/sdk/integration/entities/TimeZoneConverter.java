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

package com.iovation.launchkey.sdk.integration.entities;

import cucumber.deps.com.thoughtworks.xstream.converters.SingleValueConverter;

import java.util.TimeZone;

public class TimeZoneConverter implements SingleValueConverter {

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(TimeZone.class);
    }

    @Override
    public String toString(Object o) {
        if (o instanceof TimeZone) {
            return ((TimeZone) o).getID();
        }
        throw new IllegalArgumentException("I can only convert TimeZone objects");
    }

    @Override
    public Object fromString(String s) {
        return TimeZone.getTimeZone(s);
    }
}
