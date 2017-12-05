package com.foo.library;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
    @Profile("prod")
	ServletWebServerFactory containerCustomizer(@Value("${server.port}")Integer httpsPort) throws Exception {

        	TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
        		//For Tomcat to perform a redirect, you need to configure it with one or more security constraints. You can do this by post-processing the Context using a TomcatEmbeddedServletContainerFactory subclass
        		 @Override
        	        protected void postProcessContext(Context context) {
        	          SecurityConstraint securityConstraint = new SecurityConstraint();
        	          securityConstraint.setUserConstraint("CONFIDENTIAL");
        	          SecurityCollection collection = new SecurityCollection();
        	          collection.addPattern("/*");
        	          securityConstraint.addCollection(collection);
        	          context.addConstraint(securityConstraint);
        	        }
        	};
        	
            Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
    		connector.setScheme("http");
    	    connector.setPort(8013);
    	    connector.setSecure(false);
    	    connector.setRedirectPort(httpsPort.intValue());
			
    	    tomcat.addAdditionalTomcatConnectors(connector);
			return tomcat;
        }
}
