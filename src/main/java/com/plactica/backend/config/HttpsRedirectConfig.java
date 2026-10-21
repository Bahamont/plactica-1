package com.plactica.backend.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Expone HTTP solo para redirigir a HTTPS (CONFIDENTIAL).
 * Las peticiones no cifradas no se atienden en claro: se fuerzan a TLS.
 */
@Configuration(proxyBeanMethods = false)
public class HttpsRedirectConfig {

	@Value("${server.http.port:8080}")
	private int httpPort;

	@Value("${server.port:8443}")
	private int httpsPort;

	@Bean
	WebServerFactoryCustomizer<TomcatServletWebServerFactory> httpsRedirectCustomizer() {
		return (tomcat) -> {
			tomcat.addContextCustomizers(this::requireConfidential);
			tomcat.addAdditionalConnectors(httpRedirectConnector());
		};
	}

	private void requireConfidential(Context context) {
		SecurityConstraint constraint = new SecurityConstraint();
		constraint.setUserConstraint("CONFIDENTIAL");
		SecurityCollection collection = new SecurityCollection();
		collection.addPattern("/*");
		constraint.addCollection(collection);
		context.addConstraint(constraint);
	}

	private Connector httpRedirectConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme("http");
		connector.setPort(httpPort);
		connector.setSecure(false);
		connector.setRedirectPort(httpsPort);
		return connector;
	}
}
