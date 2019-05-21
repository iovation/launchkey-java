package com.iovation.launchkey.sdk.domain.service;

import java.util.Objects;

/**
 * Representation of an auth method at the time an authorization was performed
 */
public class AuthMethod {
    private Type method;
    private Boolean set;
    private Boolean active;
    private Boolean allowed;
    private Boolean supported;
    private Boolean userRequired;
    private Boolean passed;
    private Boolean error;

    /**
     * Get the type of method
     * @return the type of method
     */
    public Type getMethod() {
        return method;
    }

    /**
     * Was the method set?
     * @return if the method was set
     */
    public Boolean getSet() {
        return set;
    }

    /**
     * Was the method active
     * @return if the method was active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * Was the method allowed
     * @return if the method was allowed
     */
    public Boolean getAllowed() {
        return allowed;
    }

    /**
     * Was the method supported
     * @return if the method was supported
     */
    public Boolean getSupported() {
        return supported;
    }

    /**
     * Was the method required by the user
     * @return if the method was required by the user
     */
    public Boolean getUserRequired() {
        return userRequired;
    }

    /**
     * Did the method pass
     * @return if the method passed
     */
    public Boolean getPassed() {
        return passed;
    }

    /**
     * Did the method error
     * @return if the method errored
     */
    public Boolean getError() {
        return error;
    }

    /**
     * @param method Which type of method
     * @param set Was the method set
     * @param active Was the method active
     * @param allowed Was the method allowed
     * @param supported Was the method supported
     * @param userRequired Was the method required by the user
     * @param passed Did the method pass
     * @param error Did the method error
     */
    public AuthMethod(Type method, Boolean set, Boolean active, Boolean allowed, Boolean supported,
                      Boolean userRequired, Boolean passed, Boolean error) {
        this.method = method;
        this.set = set;
        this.active = active;
        this.allowed = allowed;
        this.supported = supported;
        this.userRequired = userRequired;
        this.passed = passed;
        this.error = error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthMethod)) return false;
        AuthMethod that = (AuthMethod) o;
        return getMethod() == that.getMethod() &&
                Objects.equals(getSet(), that.getSet()) &&
                Objects.equals(getActive(), that.getActive()) &&
                Objects.equals(getAllowed(), that.getAllowed()) &&
                Objects.equals(getSupported(), that.getSupported()) &&
                Objects.equals(getUserRequired(), that.getUserRequired()) &&
                Objects.equals(getPassed(), that.getPassed()) &&
                Objects.equals(getError(), that.getError());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMethod(), set, active, allowed, supported, userRequired, passed, error);
    }

    @Override
    public String toString() {
        return "AuthMethod{" +
                "method=" + method +
                ", set=" + set +
                ", active=" + active +
                ", allowed=" + allowed +
                ", supported=" + supported +
                ", userRequired=" + userRequired +
                ", passed=" + passed +
                ", error=" + error +
                '}';
    }

    public enum Type {
        PIN_CODE,
        CIRCLE_CODE,
        GEOFENCING,
        LOCATIONS,
        WEARABLES,
        FINGERPRINT,
        FACE,
        OTHER
    }
}
