/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.config;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 *
 * @author czakot
 */
@Configuration
public class I18nConfig implements WebMvcConfigurer {

//  @Bean
//  public MessageSource messageSource() {
//      ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//      messageSource.setBasename("messages");
//      messageSource.setDefaultEncoding("UTF-8");
//      return messageSource;
//  }

  @Bean
  public LocaleResolver localeResolver() {
      CookieLocaleResolver clr = new CookieLocaleResolver();
      clr.setDefaultLocale(new Locale("hu"));
      clr.setCookieName("rentLocal");
      return clr;
//      SessionLocaleResolver slr = new SessionLocaleResolver();
//      slr.setDefaultLocale(Locale.ENGLISH);
//      return slr;
  }

  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
      LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
      lci.setParamName("lang");
      return lci;
  }  

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(localeChangeInterceptor());
  }

}
