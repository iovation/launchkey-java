package com.iovation.launchkey.sdk.transport.domain;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class AuthMethod {
    private final String method;
    private final Boolean set;
    private final Boolean active;
    private final Boolean allowed;
    private final Boolean supported;
    private final Boolean userRequired;
    private final Boolean passed;
    private final Boolean error;

    public AuthMethod(@JsonProperty("method") String method, @JsonProperty("set") Boolean set,
                      @JsonProperty("active") Boolean active, @JsonProperty("allowed") Boolean allowed,
                      @JsonProperty("supported") Boolean supported, @JsonProperty("user_required") Boolean userRequired,
                      @JsonProperty("passed") Boolean passed, @JsonProperty("error") Boolean error) {
        this.method = method;
        this.set = set;
        this.active = active;
        this.allowed = allowed;
        this.supported = supported;
        this.userRequired = userRequired;
        this.passed = passed;
        this.error = error;
    }

    public String getMethod() {
        return method;
    }

    public Boolean getSet() {
        return set;
    }

    public Boolean getActive() {
        return active;
    }

    public Boolean getAllowed() {
        return allowed;
    }

    public Boolean getSupported() {
        return supported;
    }

    public Boolean getUserRequired() {
        return userRequired;
    }

    public Boolean getPassed() {
        return passed;
    }

    public Boolean getError() {
        return error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthMethod)) return false;
        AuthMethod that = (AuthMethod) o;
        return Objects.equals(getMethod(), that.getMethod()) &&
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
        return Objects.hash(getMethod(), getSet(), getActive(), getAllowed(), getSupported(), getUserRequired(), getPassed(), getError());
    }

    @Override
    public String toString() {
        return "AuthMethod{" +
                "method='" + method + '\'' +
                ", set=" + set +
                ", active=" + active +
                ", allowed=" + allowed +
                ", supported=" + supported +
                ", userRequired=" + userRequired +
                ", passed=" + passed +
                ", error=" + error +
                '}';
    }
}
