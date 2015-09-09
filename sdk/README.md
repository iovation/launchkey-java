# LaunchKey Java SDK

  * [Pre-Requisites](#prerequisites)
  * [Obtaining the Library With Dependencies](#obtaining)
    * [Dependency Management](#dependency-management)
    * [Manual Installation](#manual-installation)
  * [Usage](#usage)
    * [Create Client](#create-client)
        * [Simple](#create-client-simple)
        * [Advanced](#create-client-advanced)
    * [Use the SDK](#use-sdk)
        * [Authorize a Transaction](#use-sdk-authorize)
        * [Login a User](#use-sdk-login)
        * [Poll the Server](#use-sdk-poll)
        * [End a Login Session](#use-sdk-end-session)
        * [Pair a White Label User](#use-sdk-pair)
        * [Process Server Sent Events](#use-sdk-sse)

# <a name="prerequisites"></a>Pre-Requisites


Utilization of the LaunchKey SDK requires the following items:

 * LaunchKey Account - The [LaunchKey Mobile App](https://launchkey.com/app) is required to set up a new account and
 access the LaunchKey Dashboard.
 
## LaunchKey Rocket

 * A new Rocket can be created in the [LaunchKey Dashboard](https://dashboard.launchkey.com/).
   From the Rocket, you will need the following items found in the keys section of the rocket details:

    * The rocket key
    * The secret key
    * The private key
    
## JCE Unlimited Strength Jurisdiction Policy Files

Install the "Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files" for your JRE version.  If
you installed your JRE via a package manager, there will usually be a package for the JCE as well.  If not, download the
JAR for your JRE and follow the installation instructions.

## JCE Cryptography Provider

Follow the instructions for your installing cryptography provider.

We recommend the Bouncy Castle JCE provider.  Installation consists of:

1. Download the JAR file for the Bouncy Castle cryptography provider from [Maven Central here](https://repo1.maven.org/maven2/org/bouncycastle/bcprov-jdk15on/1.52/bcprov-jdk15on-1.52-javadoc.jar).

2. Place the JAR file in the `lib/ext` directory of the JRE you plan to use for running the demo.

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

For Maven, you may also need to supply the requirement for Bouncy Castle in your implementation:

```
<dependency>
    <groupId>bouncycastle</groupId>
    <artifactId>bcprov-jdk16</artifactId>
    <version>[0.0,)</version>
    <scope>provided</scope>
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

# <a name="usage"></a>Usage

## <a name="create-client"></a>Create a Client

### <a name="create-client-simple"></a>Simple

A LaunchKey client can be created with default settings by simply passing the following to the `factory` method:
* Rocket Key
* Secret Key
* Private Key PEM string
* Cryptography Provider

Here is an example using the BouncyCastle provider:
  
```java
long rocketKey = 1234567890;
String secretKey = "xbv739jxzx63xrexdtrkcdkpksotctewk"
String privateKey = "-----BEGIN RSA PRIVATE KEY-----\n"+
        "MIIBOwIBAAJBALEihtCuDrAp35QpaUZ+ycfsdsCGRQGUz8nbkNwP2XsCZPqamj2A\n"+
        "QF3Tgod8tTCvvEw4X1YUuHposQnDiYkb3bkCAwEAAQJBALECAZpy9PaRQXy7TRpH\n"+
        "BLil0Z1eD4MfA+2BXeaqZ/eEqRyGzPqTYP+Ch6EB8t+xuWMoG3xgA55AxTle++TG\n"+
        "dQECIQDdadMLa2z9e8cPQ42+XU4vtPdh3P2Okewg6ST9wYOhkQIhAMzOCKcDC7Pz\n"+
        "eg3SKvG40i2/VLJfHP72gLFkdIb2+GWpAiBi5bbfve8j1hrW5Yy1gAXBZ2qsKsKS\n"+
        "4PkAxkLAmaRLEQIhAMT7ngLUwRrRoaFNdZSMyUrK7fGp3b+048666gEt5XgRAiBf\n"+
        "UHCrSnIR6lx7FksBbVZdvqNZViJL1kVVLNXa9JgHiw==\n"+
        "-----END RSA PRIVATE KEY-----\n";
Provider provider = new BouncyCastleProvider();
LaunchKeyClient client = LaunchKeyClient.factory(rocketKey, secretKey, privateKey, provider);
```

### <a name="create-client-advanced"></a>Advanced

The `Config` object can be used to fine tune a litany of options related to the process of communicating with the
LaunchKey Engine. Here is an example:

```java
long rocketKey = 1234567890;
String secretKey = "xbv739jxzx63xrexdtrkcdkpksotctewk"
String privateKey = "-----BEGIN RSA PRIVATE KEY-----\n"+
        "MIIBOwIBAAJBALEihtCuDrAp35QpaUZ+ycfsdsCGRQGUz8nbkNwP2XsCZPqamj2A\n"+
        "QF3Tgod8tTCvvEw4X1YUuHposQnDiYkb3bkCAwEAAQJBALECAZpy9PaRQXy7TRpH\n"+
        "BLil0Z1eD4MfA+2BXeaqZ/eEqRyGzPqTYP+Ch6EB8t+xuWMoG3xgA55AxTle++TG\n"+
        "dQECIQDdadMLa2z9e8cPQ42+XU4vtPdh3P2Okewg6ST9wYOhkQIhAMzOCKcDC7Pz\n"+
        "eg3SKvG40i2/VLJfHP72gLFkdIb2+GWpAiBi5bbfve8j1hrW5Yy1gAXBZ2qsKsKS\n"+
        "4PkAxkLAmaRLEQIhAMT7ngLUwRrRoaFNdZSMyUrK7fGp3b+048666gEt5XgRAiBf\n"+
        "UHCrSnIR6lx7FksBbVZdvqNZViJL1kVVLNXa9JgHiw==\n"+
        "-----END RSA PRIVATE KEY-----\n";
Provider provider = new BouncyCastleProvider();
Config config = new Config()
    .setJCEProvider(provider)
    .setRSAPrivateKeyPEM(privateKey)
    .setPingResponseCache(enterprisePingResponseCache)
    .setApiBaseURL("https://api.launchkey.com")
    .setApacheHttpClient(sharedApacheHttpClient);
LaunchKeyClient client = LaunchKeyClient.factory(config);
```

## <a name="use-sdk"></a>Use the SDK

### <a name="use-sdk-authorize"></a>Authorize a Transaction

```java
String authRequest = null;
try {
    authRequest = launchKeyClient.auth().authorize(username);
}
catch(LaunchKeyException e) {
    //error handling
}
```

### <a name="use-sdk-login"></a>Login a User

```java
String authRequest = null;
try {
    authRequest = launchKeyClient.auth().login(username);
}
catch(LaunchKeyException e) {
    //error handling
}
```

### <a name="use-sdk-poll"></a>Poll the Server for an Authorization/Login Response

```java
AuthResponse authResponse = null;
try {
    while (authResponse == null) {
        Thread.sleep(1000L);
        authResponse = authService.getAuthResponse(authRequest);
        if (authResponse != null) {
            boolean authorized = authResponse.isAuthorized();
            // handle authorization result
        }
}
catch(LaunchKeyException e) {
    //error handling
}
```

### <a name="use-sdk-end-session"></a>End a Login Session

```java
try {
    launchKeyClient.auth().logout(authRequest);
}
catch(LaunchKeyException e) {
   //error handling
}
```
    
### <a name="use-sdk-pair"></a>Pair a White Label User

```java
try {
    PairResponse result = launchKeyClient.whiteLabel().whiteLabelService.pairUser(identifier);    
    // Show the user the QR Code from the QR Code URL to be validated in a white label application
}
catch(LaunchKeyException e) {
   //error handling
}
```

### <a name="use-sdk-sse"></a>Process Server Sent Events

Server Sent Events (SSE) allow your application to reduce its load by not requiring polling of the LaunchKey Engine API
with the SDK.  You must create an endpoint to receive the SSE request and update your Rocket configuration accordingly.
Here is a link to the setup instructions: [LaunchKey Docs](https://docs.launchkey.com/developer/api/callbacks/).

Server sent events are HTTP GET requests.  Collect the query parameters from the callback endpoint and place them
in a `Map<String, String>` object and call the `handleCallback` method in the `auth` service.
 
 ```java
 try {
    int signatureTimeThreshold = 300; // 5 minute threshold for request TTL
    CallbackResponse callbackResponse = launchKeyClient.auth().handleCallback(callbackData, signatureTimeThreshold);
    if (callbackResponse instanceof AuthResponseCallbackResponse) {
        // process auth response
    } elseif (callbackResponse instanceof DeOrbitCallbackResponse) {
        // process users remote de-orbit request
    }
 }
 catch(LaunchKeyException e) {
    //error handling
 }
```
