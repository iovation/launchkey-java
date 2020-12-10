# SDK for Java - Spring MVC Example

  * [Overview](#overview)
  * [Pre-Requisites](#prerequisites)
  * [Installation](#installation)
  * [Usage](#usage)

# <a name="overview"></a>Overview

This example project utilizes Spring MVC and Spring Boot to provide a fully self-contained, browser based tool to assist
with building a Mobile Authenticator utilizing a TruValidate Multifactor Authentication Mobile Authenticator SDK. It is also an example of best
practices for building a proper backend implementation utilizing the TruValidate Multifactor Authentication Service SDK for Java via webhooks.

The application removes some of the configuration headache regarding webhooks in Directories for successful linking
webhooks and Services for auth response and session end webhooks. As such, it will interfere with any other
implementations that utilize webhooks as it will alter the webhook configuration for the Directory and Service. 

# <a name="prerequisites"></a>Pre-Requisites

This example requires a JVM for Java 1.7 or greater.

Follow the pre-requisites instructions for the SDK: [SDK Instructions](../../sdk/README.md#prerequisites).
The demo will utilize the Bouncy Castle JCE provider.  Make sure to install that provider in the instructions.

You will also need a reverse proxy in order to allow for webhooks to process.  Ngrok is free and simple
to use.  The examples in this document will be based on Ngrok.  [You may obtain Ngrok here](https://ngrok.com/).


# <a name="installation"></a>Installation

Execute the Maven goal of _package_. Executing _clean_ as well is never a bad idea.  For example:

```
mvn clean package
```

#  <a name="usage"></a>Usage


1. Start your reverse proxy.

    ```bash
    $ ngrok http 8080
    ```
    
    Once started, you should see a a screen similar to:
  
    ```
    ngrok by @inconshreveable                                       (Ctrl+C to quit)
                                                                                    
    Tunnel Status                 online                                            
    Version                       2.0.19/2.0.19                                     
    Web Interface                 http://127.0.0.1:4040                             
    Forwarding                    http://d5caea01.ngrok.io -> localhost:8080        
    Forwarding                    https://d5caea01.ngrok.io -> localhost:8080       
                                                                                    
    Connections                   ttl     opn     rt1     rt5     p50     p90       
                                  0       0       0.00    0.00    0.00    0.00      
     
    ```

2. Launch the Spring Boot application by running the packaged jar in the target directory.  The following Spring Boot
    environment properties are required for the example to work:
       
    * lk.organization-id - Organization ID for an organization 
    * lk.directory-id - Directory ID for a Directory owned by the Organization 
    * lk.service-id - Service ID of a Service owned by the Directory
    * lk.private-key-location - a valid and active private key for the Organization
    * lk.external-url - the https forwarding value from the ngrok output
    
    There are numerous ways to set those properties but the simplest way is usually to pass arguments to the JAR
    execution.  This is an example of how to run the example app from the main project root with the project version
    of `4.6.0-SNAPSHOT`:
    
    ```
    java -jar examples/spring-mvc/target/examples-spring-mvc-4.6.0-SNAPSHOT.jar -Dlk.organization-id=5e460a6a-974d-11e7-b0c6-6a535e6278de -Dlk.directory-id=7ac4e652-974d-11e7-b0c6-6a535e6278de -Dlk.service-id=69cc4e34-fd9b-11e8-897e-d60562cc216d -Dlk.private-key-location=/tmp/keys/private-key.pem -Dlk.external-url=https://d5caea01.ngrok.io
    ```
    
    The name and location of the JAR file are subject to change. If you downloaded the JAR, substitute that name. If you 
    packaged the JåAR with Maven, check the actual name of the version of the JAR build in the `target` directory directly 
    under the directory containing this file. 

3. Verify the server is running by accessing the URL of your web server: [http://localhost:8080](http://localhost:8080).

4. Verify your reverse proxy by accessing the reverse proxy endpoint.  The endpoint will be the first part of one of the
    Forwarding lines.  Based on the example above it would be ```https://d5caea01.ngrok.com``` or
    ```http://d5caea01.ngrok.com```.  Copy your value for the Forwarding endpoint into you browser to ensure it is
    working correctly.  If working correctly, it will displaying the same web page you saw when verifying your web server
    as well as show 200 OK responses in the HTTP Requests section of the ngrok screen like below:
    
    ```
    ngrok by @inconshreveable                                       (Ctrl+C to quit)
                                                                                    
    Tunnel Status                 online                                            
    Version                       2.0.19/2.0.19                                     
    Web Interface                 http://127.0.0.1:4040                             
    Forwarding                    http://d5caea01.ngrok.io -> localhost:8080        
    Forwarding                    https://d5caea01.ngrok.io -> localhost:8080       
                                                                                    
    Connections                   ttl     opn     rt1     rt5     p50     p90       
                                  0       1       0.00    0.00    0.00    0.00      
                                                                                    
    HTTP Requests                                                                   
    -------------                                                                   
                                                                                    
    GET /                          302 Found                                                                         
    
    ```
    
5. Access the home page at [http://localhost:8080](http://localhost:8080).  You will be redirected to the `/login`
  page the first time you access the page.

6. Link your device by:
    1. Clicking on the _Link Device_ link.
    2. Entering a username in the _Username_ field
    3. Clicking on the _Link Device_ button
    4. Provide the _Linking Code_ to the device
    5. When linking is successful, the page will redirect to the login page 

7. Enter your username provided when you linked the device

8. Authorize or deny the request.  Authorizing will redirect you to the home page.  Denying will redirect you to a
  login error page.  Not responding will also redirect you the login error page after the timeout of five (5)
  minutes.
