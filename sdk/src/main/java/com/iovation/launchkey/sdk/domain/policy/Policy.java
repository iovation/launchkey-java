package com.iovation.launchkey.sdk.domain.policy;

import java.util.List;

public interface Policy extends PolicyAdapter {

    String getPolicyType();

    Boolean getDenyRootedJailbroken();

    void setDenyRootedJailbroken(Boolean denyRootedJailbroken);

    Boolean getDenyEmulatorSimulator();

    void setDenyEmulatorSimulator(Boolean denyEmulatorSimulator);

    List<Fence> getFences();

    void setFences(List<Fence> fences);

}
