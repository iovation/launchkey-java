package com.iovation.launchkey.sdk.domain.servicemanager;

import java.util.List;

public interface Policy {

    Boolean getDenyRootedJailbroken();

    void setDenyRootedJailbroken();

    Boolean getDenyEmulatorSimulator();

    void setDenyEmulatorSimulator();

    List<Fence> getFences();

    void setFences(List<Fence> fences);

}
