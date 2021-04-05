package com.oskarm.web_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * 
 * Main Entry point of the application. Initialise a rest template, as well as the logger.
 * 
 */

@SpringBootApplication
public class WebServiceApplication {

	// Logger
	public static final Logger log = LoggerFactory.getLogger(WebServiceApplication.class);
	
	// Weather API Key
	public static final String weatherAPIKey = "";

	public static void main(String[] args) {
		SpringApplication.run(WebServiceApplication.class, args);
	}

	// A bean that helps with UTF 8 encoding
	@Bean
	CharacterEncodingFilter characterEncodingFilter() {
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		return filter;
	}

	// A rest template to allow to send rest requests.
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

}
