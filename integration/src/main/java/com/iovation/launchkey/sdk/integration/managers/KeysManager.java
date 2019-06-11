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

package com.iovation.launchkey.sdk.integration.managers;


import com.google.inject.Inject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.Provider;
import java.security.interfaces.RSAPublicKey;

import static com.iovation.launchkey.sdk.crypto.JCECrypto.getRSAPublicKeyFromPEM;

public class KeysManager {
    private final Provider provider;
    private String base64EncodedAlphaP12 = null;
    private String base64EncodedBetaP12 = null;
    private String alphaCertificateFingerprint = null;
    private String betaCertificateFingerprint = null;
    private RSAPublicKey alphaPublicKey = null;
    private RSAPublicKey betaPublicKey = null;
    private String alphaMD5Fingerprint = null;
    private String betaMD5Fingerprint = null;

    @Inject
    public KeysManager(Provider provider) {
        this.provider = provider;
    }

    public String getBase64EncodedAlphaP12() throws IOException {
        if (base64EncodedAlphaP12 == null) {
            base64EncodedAlphaP12 = Base64.encodeBase64String(readFile("/keys/alpha-cert.p12"));
        }
        return base64EncodedAlphaP12;
    }

    public String getBase64EncodedBetaP12() throws IOException {
        if (base64EncodedBetaP12 == null) {
            base64EncodedBetaP12 = Base64.encodeBase64String(readFile("/keys/beta-cert.p12"));
        }
        return base64EncodedBetaP12;
    }

    public String getAlphaCertificateFingerprint() throws IOException {
        if (alphaCertificateFingerprint == null) {
            alphaCertificateFingerprint = new String(readFile("/keys/alpha-cert-sha256-fingerprint.txt"));
        }
        return alphaCertificateFingerprint;
    }

    public String getBetaCertificateFingerprint() throws IOException {
        if (betaCertificateFingerprint == null) {
            betaCertificateFingerprint = new String(readFile("/keys/beta-cert-sha256-fingerprint.txt"));
        }
        return betaCertificateFingerprint;
    }

    public RSAPublicKey getAlphaPublicKey() throws IOException {
        if (alphaPublicKey == null) {
            alphaPublicKey =
                    getRSAPublicKeyFromPEM(provider, new String(readFile("/keys/alpha-public-key.pem")).trim());
        }
        return alphaPublicKey;
    }

    public RSAPublicKey getBetaPublicKey() throws IOException {
        if (betaPublicKey == null) {
            betaPublicKey = getRSAPublicKeyFromPEM(provider, new String(readFile("/keys/beta-public-key.pem")).trim());
        }
        return betaPublicKey;
    }

    public String getAlphaPublicKeyMD5Fingerprint() throws IOException {
        if (alphaMD5Fingerprint == null) {
            alphaMD5Fingerprint = new String(readFile("/keys/alpha-public-key-md5-fingerprint.txt"));
        }
        return alphaMD5Fingerprint;
    }

    public String getBetaPublicKeyMD5Fingerprint() throws IOException {
        if (betaMD5Fingerprint == null) {
            betaMD5Fingerprint = new String(readFile("/keys/beta-public-key-md5-fingerprint.txt"));
        }
        return betaMD5Fingerprint;
    }

    private byte[] readFile(String fileName) throws IOException {
        InputStream in = this.getClass().getResourceAsStream(fileName);
        return IOUtils.toByteArray(in);
    }
}
