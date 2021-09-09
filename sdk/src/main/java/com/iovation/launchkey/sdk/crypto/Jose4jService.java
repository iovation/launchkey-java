package com.iovation.launchkey.sdk.crypto;

import org.jose4j.jca.ProviderContext;

public class Jose4jService {
    protected ProviderContext getProviderContext(String provider) {
        ProviderContext providerContext = new ProviderContext();
        ProviderContext.Context gpc = providerContext.getGeneralProviderContext();
        gpc.setCipherProvider(provider);
        gpc.setGeneralProvider(provider);
        gpc.setKeyAgreementProvider(provider);
        gpc.setKeyPairGeneratorProvider(provider);
        gpc.setMacProvider(provider);
        gpc.setKeyFactoryProvider(provider);
        gpc.setMessageDigestProvider(provider);
        gpc.setSignatureProvider(provider);
        ProviderContext.Context kpc = providerContext.getSuppliedKeyProviderContext();
        kpc.setCipherProvider(provider);
        kpc.setGeneralProvider(provider);
        kpc.setKeyAgreementProvider(provider);
        kpc.setKeyPairGeneratorProvider(provider);
        kpc.setMacProvider(provider);
        kpc.setKeyFactoryProvider(provider);
        kpc.setMessageDigestProvider(provider);
        kpc.setSignatureProvider(provider);
        return providerContext;
    }
}
