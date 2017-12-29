package com.kk.ssj.rest;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestApplication extends ResourceConfig {

	private static final Logger logger = LoggerFactory.getLogger(RestApplication.class);
    
    public RestApplication() {
    	
    	logger.info("-------------RestApplication begin------------");

    	packages("com.cloudafort.flexsafe");
    	
    	// register application resources
//		register(OcActivityResource.class);

		// register filters
//		register(RequestContextFilter.class);
//		register(AuthorizationFilter.class);
//		register(LicenseFilter.class);
	
		// register exception mappers
		
		// register features
		register(JacksonFeature.class);
        register(MultiPartFeature.class);
        
        logger.info("-------------RestApplication end------------");
    }
}