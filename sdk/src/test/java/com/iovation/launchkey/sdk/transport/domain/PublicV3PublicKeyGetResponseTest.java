package com.iovation.launchkey.sdk.transport.domain; /**
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

import org.junit.Test;


import static org.junit.Assert.*;

public class PublicV3PublicKeyGetResponseTest {
    @Test
    public void publicKeyGetter() throws Exception {
        //noinspection SpellCheckingInspection
        String publicKey = "-----BEGIN PUBLIC KEY-----\n" +
                "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALPHw4M3S8LTwngk+KxzQCXrru6wI1ZJ\n" +
                "evG0xc4iclQ3CUA38IMqD9o8nC6FXUeDdNLcBbVISDRi9X0OWc9hNn0CAwEAAQ==\n" +
                "-----END PUBLIC KEY-----";
        assertEquals(publicKey, new PublicV3PublicKeyGetResponse(publicKey, null).getPublicKey());
    }

    @Test
    public void publicKeyFingerprintGetter() throws Exception {
        assertEquals("FP", new PublicV3PublicKeyGetResponse(null, "FP").getPublicKeyFingerprint());
    }
}