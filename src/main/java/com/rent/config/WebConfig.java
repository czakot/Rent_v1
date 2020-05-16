package com.rent.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;  // deprecated

@Configuration
//public class WebConfig extends WebMvcConfigurerAdapter {  // deprecated
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
//		super.addViewControllers(registry); // deprecated
        registry.addViewController("/login").setViewName("auth/login");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/bloggers").setViewName("bloggers");
        registry.addViewController("/stories").setViewName("stories");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);		
	}
	
}
