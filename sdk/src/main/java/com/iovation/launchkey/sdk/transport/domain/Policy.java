package com.iovation.launchkey.sdk.transport.domain;

import java.util.List;

public interface Policy extends PolicyAdapter {

    String getPolicyType();

    Boolean getDenyRootedJailbroken();

    Boolean getDenyEmulatorSimulator();

    List<Fence> getFences();
}
