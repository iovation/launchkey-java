package com.iovation.launchkey.sdk.domain.policy;

import java.util.List;

public interface Policy extends PolicyAdapter {

    Boolean getDenyRootedJailbroken();

    Boolean getDenyEmulatorSimulator();

    List<Fence> getFences();

}
