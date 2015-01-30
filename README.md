# LaunchKey Java SDK

[![Build Status](https://travis-ci.org/LaunchKey/launchkey-java.svg)](https://travis-ci.org/LaunchKey/launchkey-java)

  * [Overview](#overview)
  * [Pre-Requisites](#prerequisites)
  * [Obtaining the Library With Dependencies](#obtaining)
    * [Dependency Management](#dependency-management)
    * [Manual Installation](#manual-installation)
  * [Usage](#usage)
  * [Support](#support)

# <a name="overview"></a>Overview

LaunchKey is an identity and access management platform  This Java SDK enables developers to quickly integrate
the LaunchKey platform and Java based applications without the need to directly interact with the platform API.

Developer documentation for using the LaunchKey API is found [here](https://launchkey.com/docs/).

An overview of the LaunchKey platform can be found [here](https://launchkey.com/platform).

#  <a name="prerequisites"></a>Pre-Requisites

Utilization of the LaunchKey SDK requires the following items:

 * LaunchKey Account - The [LaunchKey Mobile App](https://launchkey.com/app) is required to set up a new account and
 access the LaunchKey Dashboard.
 
 * An application - A new application can be created in the [LaunchKey Dashboard](https://dashboard.launchkey.com/).
   From the application, you will need the following items found in the keys section of the application details:

    * The app key
    * The secret key
    * The private key

#  <a name="obtaining"></a>Obtaining the Library With Dependencies

The JAR, source, and doc files are available through either Maven or GitHub.

## <a name="dependency-management"></a>Maven/Ivy/SBT/Buildr/Ivy/Grape/Gradle/SBT/Leiningen (Suggested)

__Group ID:__ com.launchkey.sdk
__Artifact ID:__ launchkey-sdk

_Maven Example:_

```
<dependency>
  <groupId>com.launchkey.sdk</groupId>
  <artifactId>launchkey-sdk</artifactId>
  <version>1.0.0</version>
</dependency>
```

_Apache Buildr Example_

```
'com.launchkey.sdk:launchkey-sdk:jar:1.0.0'
```

_Apache Ivy Example_

```
<dependency org="com.launchkey.sdk" name="launchkey-sdk" rev="1.0.0" />
```

_Groovy Grape Example_

```
@Grapes( 
@Grab(group='com.launchkey.sdk', module='launchkey-sdk', version='1.0.0') 
)
```

_Gradle/Grails Example_

```
compile 'com.launchkey.sdk:launchkey-sdk:1.0.0'
```

_Scala SBT Example_

```
libraryDependencies += "com.launchkey.sdk" % "launchkey-sdk" % "1.0.0"
```

_Leiningen Example_

```
[com.launchkey.sdk/launchkey-sdk "1.0.0"]
```


## <a name="manual-installation"></a>Manual Installation (Not Suggested)

Download the JAR files for the LaunchKey SDK and it's dependencies and place them in your classpath:

  * [LaunchKey SDK](https://github.com/LaunchKey/launchkey-java/releases/latest)
  * [Apache HttpComponents - Core](http://hc.apache.org/downloads.cgi)
  * [Apache HttpComponents - Client](http://hc.apache.org/downloads.cgi)
  * [Apache Commons - Logging](http://commons.apache.org/proper/commons-logging/download_logging.cgi)
  * [Apache Commons - Codec](http://commons.apache.org/proper/commons-codec/download_codec.cgi)
  * [Apache Commons - Bean Utils](http://commons.apache.org/proper/commons-beanutils/download_beanutils.cgi)
  * [Apache Commons - Collections](http://commons.apache.org/proper/commons-collections/download_collections.cgi)
  * [Apache Commons - Lang](http://commons.apache.org/proper/commons-lang/download_lang.cgi)
  * [EZMorph](http://sourceforge.net/projects/ezmorph/files/ezmorph/)
  * [JSON Lib](http://sourceforge.net/projects/json-lib/files/json-lib/)
  * [Bouncy Castle - Provider](https://www.bouncycastle.org/latest_releases.html)

__Due to the number of dependencies required by the LaunchKey SDK, it would be best to use a dependency management tool__

#  <a name="usage"></a>Usage

  1. Create an HttpClient to inject into an AuthController

    ```
    HttpClientFactory factory = new HttpClientFactory();
    factory.setMaxConnections(200);
    HttpClient httpClient = factory.createClient();
    ```

  2. Create an AuthController to inject into an AuthenticationManager

    ```
    AuthController authController = new AuthController(httpClient);
    authController.setAppKey("Your App Key");
    authController.setSecretKey("Your Secret Key");
    authController.setPrivateKey("Your Private Key Data Minus the Start and End demarcation lines");
    ```

  3. create an AuthenticationManager, use it to for calling the LaunchKey API methods

    ```java
    AuthenticationManager authenticationManager = new AuthenticationManager(authController);
    ```

  5. Use the SDK
    * Authorize a username via LaunchKey

        ```java
        AuthorizeResult result = null;
        try {
            result = authenticationManager.authorize(username);
        }
        catch(AuthenticationException e) {
            //error handling
        }
        ```

    * Determine whether the username is still authorized (i.e. has not remotely ended the session)

        ```java
        boolean isAuthorized = false;
        try {
            isAuthorized = authenticationManager.isAuthorized(authRequest, serverTime);
        }
        catch(AuthenticationException e) {
            //error handling
        }
        ```

    * Poll (Poll GET) the server for an authorization
    
        ```java
        PollResult pollResult = null;
        try {
            pollResult = authenticationManager.poll(authRequest, serverTime);
        }
        catch(AuthenticationException e) {
            //error handling
        }
        ```

    * End a session

        ```java
        try {
            authenticationManager.logout(authRequest);
        }
        catch(AuthenticationException e) {
           //error handling
        }

        ```

#  <a name="support"></a>Support

## GitHub

Submit feature requests and bugs on [GitHub](https://github.com/LaunchKey/launchkey-java/issues).

## Twitter

Submit a question to the Twitter Handle [@LaunchKeyHelp](https://twitter.com/LaunchKeyHelp).

## IRC

Engage the LaunchKey team in the `#launchkey` chat room on [freenode](https://freenode.net/).

## LaunchKey Help Desk

Browse FAQ's or submit a question to the LaunchKey support team for both
technical and non-technical issues. Visit the LaunchKey Help Desk [here](https://launchkey.desk.com/).
     