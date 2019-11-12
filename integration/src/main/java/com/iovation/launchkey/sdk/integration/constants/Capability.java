package com.iovation.launchkey.sdk.integration.constants;

public final class Capability {
    public static final String app = "Capability.app";
    public static final String application_cache_enabled = "Capability.application_cache_enabled";
    public static final String location_context_enabled = "Capability.location_context_enabled";
    public static final String full_reset = "Capability.full_reset";
    public static final String no_reset = "Capability.no_reset";
    public static final String new_command_timeout = "Capability.new_command_timeout";
    public static final String device_group = "Capability.device_group";
    public static final String device_name = "Capability.device_name";
    public static final String platform_version = "Capability.platform_version";
    public static final String session_name = "Capability.session_name";
    public static final String session_description = "Capability.session_description";
    public static final String capture_screenshots = "Capability.capture_screenshots";
    public static final String device_orientation = "Capability.device_orientation";
    public static final String platform_name = "Capability.platform_name";
    public static final String automation_name = "Capability.automation_name";
    public static final String run_device_tests = "Capability.run_device_tests";

    public static final class Android {
        public static final String app_package = "Capability.android.app_package";
        public static final String appWaitActivity = "Capability.android.appWaitActivity";
        public static final String appActivity = "Capability.android.appActivity";
    }

    public static final class iOS {
        public static final String show_ios_log = "Capability.ios.show_ios_log";
        public static final String bundle_id = "Capability.ios.bundle_id";
        public static final String updated_wda_bundle_id = "Capability.ios.updated_wda_bundle_id";
        public static final String signing_id = "Capability.ios.signing_id";
        public static final String org_id = "Capability.ios.org_id";
        public static final String udid = "Capability.ios.udid";
    }
}
