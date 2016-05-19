/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
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

import java.text.*;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class PlatformDateFormat extends Format {

    private static PlatformDateFormat instance;
    private final SimpleDateFormat formatter;

    static PlatformDateFormat getInstance() {
        if (instance == null) {
            instance = new PlatformDateFormat();
        }
        return instance;
    }

    public PlatformDateFormat() {
        this.formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        this.formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public String format(Date obj) {
        return super.format((Object) obj);
    }

    public Date parseObject(String source) throws ParseException {
        return (Date) super.parseObject(source);
    }

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        return this.formatter.format(obj, toAppendTo, pos);
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        return this.formatter.parseObject(source, pos);
    }
}
