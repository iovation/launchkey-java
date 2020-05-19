package com.iovation.launchkey.sdk.example.cli;

import com.iovation.launchkey.sdk.crypto.JCECrypto;
import picocli.CommandLine;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Command to Generate RSA Key Pairs
 */
@CommandLine.Command(name = "genrsa")
public class GenRsaCommand implements Runnable {

    @CommandLine.Parameters(paramLabel = "<KEY_SIZE>", defaultValue = "4096")
    private Integer keySize;

    @Override
    public void run() {
        try {

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keySize);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            String privateKeyPEM = JCECrypto.getPEMFromRSAPrivateKey(privateKey);
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            String publicKeyPEM = JCECrypto.getPEMFromRSAPublicKey(publicKey);

            System.out.println();
            System.out.println("KEY GEN: " + keySize.toString());
            System.out.println();
            System.out.println(privateKeyPEM);
            System.out.println();
            System.out.println(publicKeyPEM);

        } catch (NoSuchAlgorithmException e) {
            System.out.println("RSA algorithm not supported" + keySize.toString());
        }
    }
}
