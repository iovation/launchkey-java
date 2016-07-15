package com.launchkey.sdk.transport.v3;

import com.launchkey.sdk.error.BaseException;
import com.launchkey.sdk.transport.v3.domain.WhiteLabelDeviceDeleteRequest;
import com.launchkey.sdk.transport.v3.domain.WhiteLabelDeviceListRequest;

import java.security.interfaces.RSAPrivateKey;

import static org.mockito.Mockito.mock;

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
public class ApacheHttpTransportWhiteLabelUserDeviceListTest extends ApacheHttpTransportResponseReturningBase {

    @Override
    protected void makeTransportCall() throws BaseException {
        transport.whiteLabelUserDeviceList(new WhiteLabelDeviceListRequest(
                mock(RSAPrivateKey.class),
                ISSUER_ID,
                "sdkKey",
                "identifier"
        ));
    }
}
