# SDK for Java - Spring MVC Example

  * [Overview](#overview)
  * [Pre-Requisites](#prerequisites)
  * [Installation](#installation)
  * [Usage](#usage)

# <a name="overview"></a>Overview

This example project utilizes Spring MVC and Spring Boot to provide a fully self-contained browser based example
of implementing the SDK in a web application environment.  The example application hooks directly into the 
Spring MVC Web Security flow.  It implements Server Sent Events (SSE) to process authentication
responses and logout requests.  Rudimentary JavaScript exists on the home page to check for remote logout and force
the user to re-authenticate.

# <a name="prerequisites"></a>Pre-Requisites

This example requires a JVM for Java 1.7 or greater.

Follow the pre-requisites instructions for the SDK: [SDK Instructions](../../sdk/README.md#prerequisites).
The demo will utilize the Bouncy Castle JCE provider.  Make sure to install that provider in the instructions.

You will also need a reverse proxy in order to allow for Server Sent Events (SSE) to process.  Ngrok is free and simple
to use.  The examples in this document will be based on Ngrok.  [You may obtain Ngrok here](https://ngrok.com/).


# <a name="installation"></a>Installation

Execute the Maven goal of _package_. Executing _clean_ as well is never a bad idea.  For example:

```
mvn clean package
```

#  <a name="usage"></a>Usage
1. Launch the Spring Boot application by running the packaged jar in the target directory.  The following Spring Boot
  environment properties are required for the example to work:
   
  * mfa.app-key
  * mfa.secret-key
  * mfa.private-key-location
  
  There are numerous ways to set those properties but the simplest way is usually to pass arguments to the JAR
  execution.  This is an example of how to run the example app from the main project root with the project version
  of `3.0.0-SNAPSHOT`:
  
  ```
  java -jar examples/spring-mvc/target/examples-spring-mvc-3.0.0-SNAPSHOT.jar --mfa.app-key=1234567890 --mfa.secret-key=xbv739jxzx63xrexdtrkcdkpksotctewk --mfa.private-key-location=/tmp/private-key.pem
  ```

The name and location of the JAR file are subject to change. If you downloaded the JAR, substitute that name. If you 
packaged the JAR with Maven, check the actual name of the version of the JAR build in the `target` directory directly 
under the directory containing this file. 
2. Verify the server is running by accessing the URL of your web server: [http://localhost:8080](http://localhost:8080).

3. Start your reverse proxy.

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

5. Now that your web server and reverse proxy are working, update your application with the callback URL.  This is done
   by placing the URL you just verified from Ngrok plus the path `/callback` into the callback field in the General
   section of your Application configuration in the Dashboard.
   Based ion the Ngrok example above the callback URL would be: `https://d5caea01.ngrok.com/callback`.

6. Access the home page at [http://localhost:8080](http://localhost:8080).  You will be redirected to the `/login`
  page the first time you access the page.

3. Enter your username or, if the Application Key is for a White Label Application, White Label identifier.

4. Authorize or deny the request.  Authorizing will redirect you to the home page.  Denying will redirect you to a
  login error page.  Not responding will also redirect you the login error page after the timeout of five (5)
  minutes.

6. __Winning!__ - You should be ready to try the demo and see how to quickly and easily secure your Java application.
