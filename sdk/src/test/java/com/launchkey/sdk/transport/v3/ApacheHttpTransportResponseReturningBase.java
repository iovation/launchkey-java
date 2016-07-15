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

package com.launchkey.sdk.transport.v3;

import com.launchkey.sdk.crypto.jwe.JWEFailure;
import com.launchkey.sdk.error.CommunicationErrorException;
import com.launchkey.sdk.error.InvalidResponseException;
import com.launchkey.sdk.error.InvalidStateException;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public abstract class ApacheHttpTransportResponseReturningBase extends ApacheHttpTransportBase {

    @Test(expected = CommunicationErrorException.class)
    public void throwsCommunicationErrorExceptionWhenResponseEntityGetContentThrowsIoException() throws Exception {
        when(httpResponseEntity.getContent()).thenThrow(new IOException());
        when(httpResponseEntity.getContentLength()).thenReturn(99L);
        makeTransportCall();
    }

    @Test(expected = InvalidResponseException.class)
    public void throwsInvalidResponseExceptionWhenResponseContentIsNotMappable() throws Exception {
        setResponseContent("data");
        when(jweService.decrypt(anyString())).thenReturn("{\"Un-mappable\": \"Data\"}");
        makeTransportCall();
    }

    @Test(expected = InvalidResponseException.class)
    public void raisesErrorWithStatusCodeForMessageCodeWhenNoContentInBody() throws Exception {
        setResponseContent("{Invalid JSON}");
        makeTransportCall();
    }

    @Test(expected = InvalidResponseException.class)
    public void jweServiceDecryptJweFailureThrowsInvalidResponseException() throws Exception {
        setResponseContent("content");
        when(jweService.decrypt(anyString()))
                .thenThrow(new JWEFailure(null, null));
        makeTransportCall();
    }
}
