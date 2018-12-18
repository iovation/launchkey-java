package com.iovation.launchkey.sdk.example.cli;

import picocli.CommandLine;

public class DemoApp {

    public static void main(String[] args) {
        CommandLine.call(new RootCommand(), args);
    }
}
