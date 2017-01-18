/**
 * Copyright 2017 Adam Englander
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.transport.v3.domain;

import com.launchkey.sdk.domain.auth.AuthPolicy;

public class ServiceAuthsPostRequest {
    String username;
    String context;
    AuthPolicy authPolicy;
}
