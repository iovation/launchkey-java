package com.iovation.launchkey.sdk.integration.steps;

import com.iovation.launchkey.sdk.FactoryFactory;
import com.iovation.launchkey.sdk.FactoryFactoryBuilder;
import com.iovation.launchkey.sdk.client.OrganizationFactory;
import com.iovation.launchkey.sdk.crypto.JCECrypto;
import com.iovation.launchkey.sdk.integration.constants.Launchkey;
import com.iovation.launchkey.sdk.integration.managers.DirectoryManager;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.Provider;
import java.security.interfaces.RSAPrivateKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;


@ScenarioScoped
public class KeySteps {
    private final GenericSteps genericSteps;
    private final FactoryFactory factoryFactory;
    private final JCECrypto jceCrypto;
    private final Provider provider;

    private OrganizationFactory organizationFactory;

    @Inject
    public KeySteps(GenericSteps genericSteps) {
        String baseUrl = System.getProperty(Launchkey.API.base_url);
        this.genericSteps = genericSteps;
        this.provider = new BouncyCastleProvider();
        this.jceCrypto = new JCECrypto(provider);
        this.factoryFactory = new FactoryFactoryBuilder()
            .setAPIBaseURL(baseUrl)
            .setJCEProvider(provider)
            .setRequestExpireSeconds(1)
            .build();
    }

    private RSAPrivateKey getPrivateKeyPEM(String typeOfKey) throws IOException {
        String privateKeyFile = null;

        if (typeOfKey.equals("encryption")) {
            privateKeyFile = System.getProperty(Launchkey.Organization.encryption_key);
        } else if (typeOfKey.equals("signature")) {
            privateKeyFile = System.getProperty(Launchkey.Organization.signature_key);
        }

        assertThat(privateKeyFile, is(not(nullValue())));

        String privateKey = readFile(privateKeyFile);

        return jceCrypto.getRSAPrivateKeyFromPEM(provider, privateKey);
    }

    private String readFile(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();

        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = reader.readLine();

        while (line != null) {
            sb.append(line);
            sb.append("\n");
            line = reader.readLine();
        }

        return sb.toString();
    }

    private String getOrganizationId() {
        String organizationId = System.getProperty(Launchkey.Organization.id);

        assertThat(organizationId, not(organizationId.isEmpty()));
        assertThat(organizationId, not(nullValue()));

        return organizationId;
    }

    @Given("^I am using single purpose keys$")
    public void iAmUsingSinglePurposeKeys() throws Throwable {
        String organizationId = this.getOrganizationId();

        RSAPrivateKey encryptionKey = getPrivateKeyPEM("encryption");
        String encryptionKeyFingerprint = jceCrypto.getRsaPublicKeyFingerprint(provider, encryptionKey);

        RSAPrivateKey signatureKey = getPrivateKeyPEM("signature");
        String signatureKeyFingerprint = jceCrypto.getRsaPublicKeyFingerprint(provider, signatureKey);

        // Ensure that two separate keys are passed
        assertThat(
            "The encryption key and signature key must be different keys.",
            encryptionKeyFingerprint, not(equalTo(signatureKeyFingerprint)));

        Map<String, RSAPrivateKey> keys = new ConcurrentHashMap<>();
        keys.put(encryptionKeyFingerprint, encryptionKey);
        keys.put(signatureKeyFingerprint, signatureKey);

        organizationFactory = factoryFactory.makeOrganizationFactory(organizationId, keys, signatureKeyFingerprint);
    }

    @Given("^I am using single purpose keys but I am using my encryption key to sign$")
    public void iAmUsingSinglePurposeKeysWithOnlyEncryptionKeys() throws Throwable {
        String organizationId = this.getOrganizationId();

        RSAPrivateKey encryptionKey = getPrivateKeyPEM("encryption");
        String encryptionKeyFingerprint = jceCrypto.getRsaPublicKeyFingerprint(provider, encryptionKey);

        // Intentionally using encryption key to sign...
        RSAPrivateKey signatureKey = getPrivateKeyPEM("encryption");
        String signatureKeyFingerprint = jceCrypto.getRsaPublicKeyFingerprint(provider, signatureKey);

        Map<String, RSAPrivateKey> keys = new ConcurrentHashMap<>();
        keys.put(encryptionKeyFingerprint, encryptionKey);
        keys.put(signatureKeyFingerprint, signatureKey);

        organizationFactory = factoryFactory.makeOrganizationFactory(organizationId, keys, signatureKeyFingerprint);
    }

    @Given("^I am using single purpose keys but I only set my signature key$")
    public void iAmUsingSinglePurposeKeysWithOnlyASignatureKey() throws Throwable {
        String organizationId = this.getOrganizationId();

        RSAPrivateKey signatureKey = getPrivateKeyPEM("signature");
        String signatureKeyFingerprint = jceCrypto.getRsaPublicKeyFingerprint(provider, signatureKey);

        Map<String, RSAPrivateKey> keys = new ConcurrentHashMap<>();
        keys.put(signatureKeyFingerprint, signatureKey);

        organizationFactory = factoryFactory.makeOrganizationFactory(organizationId, keys, signatureKeyFingerprint);
    }

    @When("^I perform an API call using single purpose keys$")
    @When("^I attempt an API call using single purpose keys$")
    public void iPerformAnApiCallWithSinglePurposeKeys() throws Throwable {
        try {
            DirectoryManager directoryManager = new DirectoryManager(organizationFactory);
            directoryManager.createDirectory();
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @Then("^no valid key will be available to decrypt response$")
    public void noValidKeyExistsToDecryptResponse() throws Throwable {
        genericSteps.anExceptionIsThrown("CryptographyError");
    }
}
