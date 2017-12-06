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

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.google.inject.spi.Message;
import com.iovation.launchkey.sdk.FactoryFactoryBuilder;
import com.iovation.launchkey.sdk.client.OrganizationFactory;
import com.iovation.launchkey.sdk.crypto.Crypto;
import com.iovation.launchkey.sdk.crypto.JCECrypto;
import cucumber.api.guice.CucumberModules;
import cucumber.runtime.java.guice.InjectorSource;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.Provider;
import java.util.Properties;
import java.util.UUID;

public class CucumberGuiceInjectorSource implements InjectorSource {

    @Override
    public Injector getInjector() {
        return Guice.createInjector(Stage.PRODUCTION, CucumberModules.SCENARIO, new CucumberJuiceModule());
    }

    private class CucumberJuiceModule extends AbstractModule {

        @Override
        protected void configure() {
            try (InputStream in = CucumberGuiceInjectorSource.class.getResourceAsStream("/application.properties")) {
                Properties properties = new Properties();
                properties.load(in);
                for (String key : properties.stringPropertyNames()) {
                    if (System.getProperty(key) == null) {
                        System.setProperty(key, properties.getProperty(key));
                    }
                }
            } catch (Exception e) {
                addError("Unable to load properties file.", e);
            }

            Provider provider = new BouncyCastleProvider();


            bind(Provider.class).toInstance(provider);

            JCECrypto crypto = new JCECrypto(provider);
            bind(Crypto.class).toInstance(crypto);

            String pem = getPrivateKeyPEM(provider);
            String baseURL = getApiBaseUrl();
            String organizationId = getOrganizationId();
            OrganizationFactory organizationFactory;

            if (pem != null && baseURL != null && organizationId != null) {
                organizationFactory = new FactoryFactoryBuilder()
                        .setAPIBaseURL(baseURL)
                        .setJCEProvider(provider)
                        .setRequestExpireSeconds(1)
                        .build()
                        .makeOrganizationFactory(organizationId, pem);
            } else {
                organizationFactory = null;
            }
            bind(OrganizationFactory.class).toInstance(organizationFactory);
        }

        private String getApiBaseUrl() {
            String baseUrl = System.getProperty("lk.api.base_url");
            try {
                //noinspection ResultOfMethodCallIgnored
                URI.create(baseUrl);
            } catch (Exception e) {
                addError(new Message("Invalid Base URL specified.", e));
            }
            return baseUrl;
        }

        private String getPrivateKeyPEM(Provider provider) {
            String privateKeyFile = System.getProperty("lk.organization.private_key");
            String privateKey = null;
            if (privateKeyFile == null || privateKeyFile.isEmpty()) {
                addError(new Message("No Private Key file location provided in the \"lk.organization.private_key\" " +
                        "property. A PEM formatted RSA Private key for the provided Organization ID is required " +
                        "to perform integration tests."));
            } else {
                try {
                    privateKey = readFile(privateKeyFile);
                    JCECrypto.getRSAPrivateKeyFromPEM(provider, privateKey);
                } catch (IOException e) {
                    addError(new Message("Unable to read RSA private key from file.", e));
                } catch (Exception e) {
                    addError(new Message("Invalid RSA Private Key provided. The key must be PEM formatted.", e));
                }
            }
            return privateKey;
        }

        private String getOrganizationId() {
            String organizationId = System.getProperty("lk.organization.id");
            if (organizationId == null || organizationId.isEmpty()) {
                addError(new Message("No Organization ID provided in the property \"lk.organization.id\". " +
                        "The Organization ID is required to perform integration tests."));
            } else {
                try {
                    //noinspection ResultOfMethodCallIgnored
                    UUID.fromString(organizationId);
                } catch (Exception e) {
                    addError(new Message("The Organization ID was invalid. It must be a UUID.", e));
                }
            }
            return organizationId;
        }

        @SuppressWarnings("Duplicates")
        private String readFile(String fileName) throws IOException {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line = reader.readLine();
                while (line != null) {
                    sb.append(line);
                    sb.append("\n");
                    line = reader.readLine();
                }
            }
            return sb.toString();
        }
    }
}
