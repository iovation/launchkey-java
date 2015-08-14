# CHANGELOG for LaunchKey Java SDK

This changelog references the relevant changes (bug and security fixes) for the lifetime of the library.

To get the diff for a specific change, go to [https://github.com/LaunchKey/launchkey-java/commit/XXX](https://github.com/LaunchKey/launchkey-java/commit/XXX) where XXX
is the change hash To get the diff between two versions, go to
[https://github.com/LaunchKey/launchkey-java/compare/launchkey-sdk-1.0.0...launchkey-sdk-1.1.0](https://github.com/LaunchKey/launchkey-java/compare/launchkey-sdk-1.0.0...launchkey-sdk-1.1.0)

  * 2.0.0
    * Make client easier to create with factories
    * Make client more extensible
    * Make crypto more testable
    * Separate transport and client to prepare for API v2
    * Move examples and SDK into one code base

  * 1.2.1
    * Update AuthenticationManager.createWhiteLabelUser() to allow for no lk_identifier in response to enable forwards
        compatibility with new API change forthcoming.

  * 1.2.0
    * Update AuthenticationManager.createWhiteLabelUser() for new API implementation
    * Move more cryptography functions to Crypto helper class
    * Allow usage of RSA keys with header data
    * Mark Bouncy Castle as provided in POM.  Can't be used in the unsigned JAR anyway.

  * 1.1.1
    * Remove requirement for providing LaunchKey time for isAuthorized method in authentication manager.  It will use
        the LaunchKey time from the ping call it performs before it checks authorization. Authentication manager method
        isAuthorized(String, String) is now deprecated.  It will simply call the isAuthorized(String) and ignore the
        LaunchKey time provided.

  * 1.1.0
    * Added white label user create method
    * Loosened restrictions on dependency versions
    * Refactored to use non-deprecated methods for Apache HTTP client
    * Added ability to connect to staging servers for pre-release testing
    * Fixed bug in authentication manager that would error looking for a user_push_id response attribute when login call
        specified false for userPushId parameter

  * 1.0.0
    * First release with authentication methods
