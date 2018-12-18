package com.iovation.launchkey.sdk.example.cli;

import picocli.CommandLine;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class DemoApp {

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        CommandLine.call(new LaunchKeyCommand(), args);
    }
}
