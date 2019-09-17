package com.iovation.launchkey.sdk.domain.policy;

import java.util.List;

public interface Policy {

    boolean getDenyRootedJailbroken();

    boolean getDenyEmulatorSimulator();

    List<Fence> getFences();

}
