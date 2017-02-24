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

package com.launchkey.sdk.service;

import com.launchkey.sdk.client.BasicDirectoryClient;
import com.launchkey.sdk.domain.directory.Device;
import com.launchkey.sdk.domain.directory.DeviceStatus;
import com.launchkey.sdk.domain.directory.DirectoryUserDeviceLinkData;
import com.launchkey.sdk.error.*;
import com.launchkey.sdk.transport.Transport;
import com.launchkey.sdk.transport.domain.*;
import com.launchkey.sdk.transport.domain.EntityIdentifier.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Deprecated
public class BasicDirectoryService extends BasicDirectoryClient implements DirectoryService {
    public BasicDirectoryService(UUID directoryId, Transport transport) {
        super(directoryId, transport);
    }
}
