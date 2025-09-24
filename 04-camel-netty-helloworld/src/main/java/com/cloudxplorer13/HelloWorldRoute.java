package com.cloudxplorer13;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.opentelemetry.OpenTelemetryTracer;
import org.apache.camel.support.jsse.KeyStoreParameters;
import org.apache.camel.support.jsse.KeyManagersParameters;
import org.apache.camel.support.jsse.SSLContextParameters;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Path;

import org.jboss.logging.Logger;

@ApplicationScoped
public class HelloWorldRoute extends RouteBuilder {

    private static final Logger log = Logger.getLogger(HelloWorldRoute.class);

    @Inject
    @org.eclipse.microprofile.config.inject.ConfigProperty(name = "app.https.enabled", defaultValue = "")
    boolean tlsEnabled;

    @Inject
    @org.eclipse.microprofile.config.inject.ConfigProperty(name = "app.https.keystore.file", defaultValue = "")
    String keystoreFile;

    @Inject
    @org.eclipse.microprofile.config.inject.ConfigProperty(name = "app.https.keystore.password", defaultValue = "")
    String keystorePassword;

    @Inject
    @org.eclipse.microprofile.config.inject.ConfigProperty(name = "app.https.keystore.key-password", defaultValue = "")
    String keyPassword;

    @Inject
    org.apache.camel.CamelContext camelContext;

    /**
     * Configure OpenTelemetry tracer and bind it to Camel context
     */
    @PostConstruct
    void configureOpenTelemetry() {
        OpenTelemetryTracer otTracer = null;
        try {
            otTracer = new OpenTelemetryTracer();
            otTracer.init(camelContext); // attach tracer to Camel context
            log.info("OpenTelemetry tracer initialized and registered with Camel context.");
        } catch (Exception e) {
            log.error("Failed to initialize OpenTelemetry tracer: ", e);
        } finally {
            if (otTracer != null) {
                try {
                    otTracer.close();
                } catch (Exception closeEx) {
                    log.warn("Failed to close OpenTelemetry tracer: ", closeEx);
                }
            }
        }
    }

    @Override
    public void configure() throws Exception {
        String routeUri;

        if (tlsEnabled && keystoreFile != null && !keystoreFile.isBlank()) {
            String pathStr = keystoreFile.startsWith("file:") ? keystoreFile.substring(5) : keystoreFile;
            Path keystorePath = Path.of(pathStr);

            if (Files.exists(keystorePath)) {
                KeyStoreParameters ksp = new KeyStoreParameters();
                ksp.setResource("file:" + keystorePath.toAbsolutePath());
                ksp.setPassword(keystorePassword);

                KeyManagersParameters kmp = new KeyManagersParameters();
                kmp.setKeyStore(ksp);
                kmp.setKeyPassword(keyPassword);

                SSLContextParameters sslContextParameters = new SSLContextParameters();
                sslContextParameters.setKeyManagers(kmp);

                camelContext.getRegistry().bind("sslContextParameters", sslContextParameters);

                routeUri = "netty-http:https://0.0.0.0:8443/hello?ssl=true&sslContextParameters=#sslContextParameters";
                log.infof("TLS enabled. Listening on %s", routeUri);

            } else {
                log.warnf("TLS enabled but keystore %s not found. Falling back to HTTP.", keystoreFile);
                routeUri = "netty-http:http://0.0.0.0:8080/hello";
            }

        } else {
            routeUri = "netty-http:http://0.0.0.0:8080/hello";
            log.info("TLS disabled. Listening on HTTP 8080");
        }

        from(routeUri)
            .routeId("hello-world-proxy")
            .log("Incoming request: ${header.CamelHttpPath}")
            .setBody(simple("Hello World!"));
    }
}
