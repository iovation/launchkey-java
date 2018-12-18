package com.iovation.launchkey.sdk.example.cli;

import com.iovation.launchkey.sdk.FactoryFactory;
import com.iovation.launchkey.sdk.FactoryFactoryBuilder;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import picocli.CommandLine;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@CommandLine.Command(subcommands = {
        ServiceCommand.class
})
public class LaunchKeyCommand implements Callable<Void> {


    @CommandLine.Option(names = {"-u", "--base-url"}, defaultValue = "https://api.launchkey.com",
            description = "Base URL to the iovation LaunchKey API")
    String baseUrl;

    @CommandLine.Option(names={"-n", "--no-verify"}, description =
            "Disable SSL certificate verification. Required for getting past certificate verification errors for " +
                    "self-signed certs or local CA's")
    boolean noVerify;


    @Override
    public Void call() throws Exception {
        CommandLine.usage(this, System.out);
        return null;
    }

    FactoryFactory getFactoryFactory() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        Provider provider = new BouncyCastleProvider();
        FactoryFactoryBuilder builder = new FactoryFactoryBuilder()
                .setJCEProvider(provider)
                .setAPIBaseURL(baseUrl);

        if (noVerify) {
            HttpClient httpClient = getHttpClientWithoutSslVerify();
            builder.setHttpClient(httpClient);
        }

        FactoryFactory factoryFactory = builder.build();
        return factoryFactory;
    }

    private static HttpClient getHttpClientWithoutSslVerify() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        final SSLContextBuilder builder = SSLContexts.custom();
        builder.loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
                return true;
            }
        });
        final SSLContext sslContext = builder.build();
        final SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                builder.build());

        final Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory>create().register("https", socketFactory)
                .build();

        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry, null, null, null, 30, TimeUnit.SECONDS
        );

        connectionManager.setMaxTotal(30);
        connectionManager.setDefaultMaxPerRoute(30);
        return HttpClients
                .custom()
                .setConnectionManager(connectionManager)
                .build();
    }
}
