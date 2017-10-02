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

package com.iovation.launchkey.sdk.integration;


import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class Utils {
    private static final SecureRandom random = new SecureRandom();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    static String createRandomServiceName() {
        return appendRandomSuffix("Test Service ", 35);
    }

    static String createRandomDirectoryName() {
        return appendRandomSuffix("Test Directory ", 30);
    }

    static String createRandomDirectoryUserName() {
        return appendRandomSuffix("TestUser", 25);
    }

    static String appendRandomSuffix(String string, int size) {
        StringBuilder result = new StringBuilder(string);
        for (int i = 0; i < size; i++) {
            result.append(String.valueOf(random.nextInt(9)).toCharArray());
        }
        return result.toString();
    }

    static Date parseDateString(String dateString) throws ParseException {
        return dateFormat.parse(dateString);
    }

    static Boolean getBooleanFromBooleanTextSwitch(String switchString) {
        Boolean switchValue;
        switch (switchString) {
            case "does not":
            case "not set":
                return false;
            case "does":
            case "set":
                return true;
            case "has no":
                return null;
        }
        throw new IllegalArgumentException("Switch string must be one of [does not|does|has no]");
    }
}
