# CHANGELOG for LaunchKey Java SDK

This changelog references the relevant changes (bug and security fixes) for the lifetime of the library.

To get the diff for a specific change, go to [https://github.com/LaunchKey/launchkey-java/commit/XXX](https://github.com/LaunchKey/launchkey-java/commit/XXX) where XXX
is the change hash To get the diff between two versions, go to
[https://github.com/LaunchKey/launchkey-java/compare/launchkey-sdk-1.0.0...launchkey-sdk-1.1.0](https://github.com/LaunchKey/launchkey-java/compare/launchkey-sdk-1.0.0...launchkey-sdk-1.1.0)

  * 1.0.0
    * First release with authentication methods

  * 1.1.0
    * Added white label user create method
    * Loosened restrictions on dependency versions
    * Refactored to use non-deprecated methods for Apache HTTP client
    * Added ability to connect to staging servers for pre-release testing
    * Fixed bug in authentication manager that would error looking for a user_push_id response attribute when login call
        specified false for userPushId parameter