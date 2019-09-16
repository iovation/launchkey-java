package com.iovation.launchkey.sdk.domain.policy;

import java.util.List;

public interface Policy extends PolicyAdapter {

    boolean getDenyRootedJailbroken();

    boolean getDenyEmulatorSimulator();

    List<Fence> getFences();

}
