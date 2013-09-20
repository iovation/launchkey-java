LaunchKey Java SDK

To use:

1. build the launchkey-java artifacts:
 $> cd launchkey-sdk-java/
 $> mvn clean install

2. include the sdk in your project with the following maven dependency:
 <dependency>
     <groupId>com.launchkey.sdk</groupId>
     <artifactId>launchkey-sdk</artifactId>
     <version>1.0</version>
 </dependency>

3. To use with Spring:

 - define the following in a properties file:

launchkey-app-key=<LAUNCHKEY APP KEY>
launchkey-app-secret=<YOUR APP SECRET>
launchkey-private-key=<YOUR PRIVATE KEY>

 - create an HttpClient to inject into an AuthController:

<bean id="httpClientInstanceFactory"
      class="com.launchkey.sdk.http.HttpClientFactory">
    <property name="maxConnections" value="200"/>
</bean>

<bean id="httpClient" factory-bean="httpClientInstanceFactory"
      factory-method="createClient">
</bean>

 - create an AuthController to inject into an AuthenticationManager:

<bean id="launchkeyAuthController" class="com.launchkey.sdk.http.AuthController">
    <constructor-arg index="0" ref="httpClient"/>
    <property name="appKey" value="${launchkey-app-key}"/>
    <property name="secretKey" value="${launchkey-app-secret}"/>
    <property name="privateKey" value="${launchkey-private-key}"/>
</bean>

 - create an AuthenticationManager, use it to for calling the LaunchKey API methods:

<bean id="launchkeyAuthenticationManager" class="com.launchkey.sdk.auth.AuthenticationManager">
    <constructor-arg index="0" ref="launchkeyAuthController"/>
</bean>

4. To use without Spring:
 - create an HttpClient to inject into an AuthController:

HttpClientFactory factory = new HttpClientFactory();
factory.setMaxConnections(200);
HttpClient httpClient = factory.createClient();

 - create an AuthController to inject into an AuthenticationManager:

AuthController authController = new AuthController(httpClient);
authController.setAppKey();
authController.setSecretKey();
authController.setPrivateKey();


 - create an AuthenticationManager, use it to for calling the LaunchKey API methods:

AuthenticationManager authenticationManager = new AuthenticationManager(authController);

5. use the SDK:

 - Authorize a username via LaunchKey:

AuthorizeResult result = null;
try {
    result = authenticationManager.authorize(username);
}
catch(AuthenticationException e) {
    //error handling
}

 - determine whether the username is still authorized (i.e. has not remotely ended the session):

boolean isAuthorized = false;
try {
    isAuthorized = authenticationManager.isAuthorized(authRequest, serverTime);
}
catch(AuthenticationException e) {
    //error handling
}

 - poll (Poll GET) the server for an authorization:

PollResult pollResult = null;
try {
    pollResult = authenticationManager.poll(authRequest, serverTime);
}
catch(AuthenticationException e) {
    //error handling
}

 - end a session:

try {
    authenticationManager.logout(authRequest);
}
catch(AuthenticationException e) {
   //error handling
}

