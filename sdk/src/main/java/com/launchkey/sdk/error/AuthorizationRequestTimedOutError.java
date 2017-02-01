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

package com.launchkey.sdk.error;

/**
 * Error that is thrown when attempting to retrieve data regarding an expired authorization request
 */
public class AuthorizationRequestTimedOutError extends BaseException {
    public AuthorizationRequestTimedOutError() {
        super("The user did not respond to the request in the allotted time!", null, "HTTP-408");
    }
}
