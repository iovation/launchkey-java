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

import com.google.inject.*;
import com.google.inject.spi.Message;
import com.iovation.launchkey.sdk.FactoryFactoryBuilder;
import com.iovation.launchkey.sdk.client.OrganizationFactory;
import com.iovation.launchkey.sdk.crypto.Crypto;
import com.iovation.launchkey.sdk.crypto.JCECrypto;
import com.iovation.launchkey.sdk.integration.constants.Appium;
import com.iovation.launchkey.sdk.integration.constants.Capability;
import com.iovation.launchkey.sdk.integration.constants.Launchkey;
import com.iovation.launchkey.sdk.integration.managers.kobiton.KobitonDevice;
import com.iovation.launchkey.sdk.integration.managers.kobiton.KobitonManager;
import com.iovation.launchkey.sdk.integration.managers.kobiton.transport.RequestFactory;
import com.iovation.launchkey.sdk.integration.mobile.driver.NullMobileDriver;
import com.iovation.launchkey.sdk.integration.mobile.driver.SampleAppMobileDriver;
import com.iovation.launchkey.sdk.integration.mobile.driver.android.SampleAppAndroidDriver;
import cucumber.api.guice.CucumberModules;
import cucumber.runtime.java.guice.InjectorSource;
import io.cucumber.java.Before;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.AssumptionViolatedException;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.Provider;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class CucumberGuiceInjectorSource implements InjectorSource {

    @Override
    public Injector getInjector() {
        Module scenarioModule = CucumberModules.createScenarioModule();
        return Guice.createInjector(Stage.PRODUCTION, scenarioModule, new CucumberJuiceModule());
    }

    public static class CucumberJuiceModule extends AbstractModule {
        private boolean skipDeviceScenarios = true;

        @Before("@device_testing")
        public void skipDeviceScenarios() throws Exception {
            if (!System.getProperty(Capability.run_device_tests).equals("true")) {
                throw new AssumptionViolatedException("Skipping device tests");
            }
        }

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
            if (getBooleanPropertyElseAddError(Capability.run_device_tests)) {
                bind(SampleAppMobileDriver.class).toInstance(getMobileDriver());
            } else {
                Logger.getGlobal().warning("Not running device based tests");
                bind(SampleAppMobileDriver.class).toInstance(new NullMobileDriver());
            }
        }

        private String getApiBaseUrl() {
            String baseUrl = getPropertyElseAddError(Launchkey.API.base_url);
            try {
                //noinspection ResultOfMethodCallIgnored
                URI.create(baseUrl);
            } catch (Exception e) {
                addError(new Message("Invalid Base URL specified.", e));
            }
            return baseUrl;
        }

        private String getPrivateKeyPEM(Provider provider) {
            String privateKeyFile = getPropertyElseAddError(Launchkey.Organization.private_key);
            String privateKey = null;
            if (privateKeyFile != null && !privateKeyFile.isEmpty()) {
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
            String organizationId = getPropertyElseAddError(Launchkey.Organization.id);
            if (organizationId != null && !organizationId.isEmpty()) {
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

        private SampleAppMobileDriver getMobileDriver() {
           SampleAppMobileDriver mobileDriver = null;
            boolean useKobiton = getBooleanPropertyElseAddError(Appium.Kobiton.use_kobiton);
            if (useKobiton) {

                String kobitonUploadUrl = getPropertyElseAddError(Appium.Kobiton.upload_url);
                String kobitonAuthCreds = getPropertyElseAddError(Appium.Kobiton.auth);
                String kobitonAppName = getPropertyElseAddError(Appium.Kobiton.app_name);
                String appPhysicalLocation = getPropertyElseAddError(Capability.app);
                String kobitonAppsUrl = getPropertyElseAddError(Appium.Kobiton.apps_url);
                String kobitonDevicesUrl = getPropertyElseAddError(Appium.Kobiton.devices_url);
                KobitonManager kobitonManager = new KobitonManager(new RequestFactory(), kobitonUploadUrl, kobitonAuthCreds, kobitonAppsUrl, kobitonDevicesUrl);

                try {
                    kobitonManager.createApplication(
                        kobitonAppName,
                        appPhysicalLocation);
                } catch (Exception e) {
                    addError("Could not create application on Kobiton", e);
                }

                System.setProperty(Capability.app, kobitonManager.getCurrentAppLocation());

                String platformName = getPropertyElseAddError(Capability.platform_name);

                try {
                    List<KobitonDevice> devices = kobitonManager.getAllDevices();
                    for (KobitonDevice device : devices) {
                        if (!device.isBooked() && device.getPlatformName().equals(platformName) && Integer.valueOf(String.valueOf(device.getPlatformVersion().charAt(0))) >= 5) {
                            System.setProperty(Capability.device_name, device.getDeviceName());
                            System.setProperty(Capability.platform_version, device.getPlatformVersion());
                            break;
                        }
                    }
                } catch (Exception e) {
                    addError("Could not get Kobiton Device", e);
                }
            }

            URL appiumUrl = null;
            try {
                appiumUrl = new URL(getPropertyElseAddError(Appium.url));
            } catch (MalformedURLException e) {
                addError("Appium URL provided is invalid", e);
            }

            SampleAppAndroidDriver driver = null;
            try {
                driver = new SampleAppAndroidDriver(appiumUrl, getDesiredCapabilities());
                mobileDriver = driver;
            } catch (Exception e) {
                addError("Could not load platform driver, make sure Appium.url is set to the correct value", e);
            }

            String commandTimeoutString = getPropertyElseAddError(Capability.new_command_timeout);
            try {
                int commandTimeout = Integer.valueOf(commandTimeoutString);
                driver.manage().timeouts().implicitlyWait(commandTimeout, TimeUnit.SECONDS);
            } catch (NumberFormatException e) {
                addInvalidPropertyError(Capability.new_command_timeout);
            }
            return mobileDriver;
        }

        private DesiredCapabilities getDesiredCapabilities() {
            DesiredCapabilities capabilities = new DesiredCapabilities();

            if (getPropertyElseAddError(Capability.platform_name).equals("Android")) {
                capabilities.setCapability("gpsEnabled", true);
                capabilities.setCapability("disableWindowAnimation", true);
            }

            // General
            capabilities.setCapability("app",
                    getPropertyElseAddError(Capability.app));
            capabilities.setCapability("automationName",
                    getPropertyElseAddError(Capability.automation_name));
            capabilities.setCapability("fullReset",
                    getBooleanPropertyElseAddError(Capability.full_reset));
            capabilities.setCapability("noReset",
                    getBooleanPropertyElseAddError(Capability.no_reset));
            capabilities.setCapability("applicationCacheEnabled",
                    getBooleanPropertyElseAddError(Capability.application_cache_enabled));
            capabilities.setCapability("locationContextEnabled",
                    getPropertyElseAddError(Capability.location_context_enabled));

            // Device specific
            capabilities.setCapability("sessionName",
                    getPropertyElseAddError(Capability.session_name));
            capabilities.setCapability("deviceOrientation",
                    getPropertyElseAddError(Capability.device_orientation));
            capabilities.setCapability("captureScreenshots",
                    getBooleanPropertyElseAddError(Capability.capture_screenshots));
            capabilities.setCapability("deviceGroup",
                    getPropertyElseAddError(Capability.device_group));
            capabilities.setCapability("deviceName",
                    getPropertyElseAddError(Capability.device_name));
            capabilities.setCapability("platformVersion",
                    getPropertyElseAddError(Capability.platform_version));
            capabilities.setCapability("platformName",
                    getPropertyElseAddError(Capability.platform_name));
            return capabilities;
        }

        private void addInvalidPropertyError(String prop) {
            addError(new Message("Property \""+ prop +
                    "\" not provided or invalid. Cannot run tests without this property"));
        }

        private String getPropertyElseAddError(String prop) {
            String propString = System.getProperty(prop);
            if (propString == null || propString.isEmpty()) {
                addInvalidPropertyError(prop);
            }
            return propString;
        }

        private boolean getBooleanPropertyElseAddError(String positiveProp) {
            String propString = getPropertyElseAddError(positiveProp);
            if (!propString.equals("true") && !propString.equals("false")) {
                addError(new Message("Boolean property \"" + positiveProp +
                        "\" has invalid string, true or false are the only accepted values."));
            }
            return propString.equals("true");
        }
    }
}
