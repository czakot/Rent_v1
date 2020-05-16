package com.rent.config;

import com.rent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// @EnableGlobalMethodSecurity(securedEnabled = true) // for usage of @Secured("<role>") to secure a method
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return super.userDetailsService();
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity httpSec) throws Exception {
        httpSec
            .authorizeRequests()
                .antMatchers("/css/*", "/js/*").permitAll()
                .antMatchers("/activation/*").permitAll()
                .and()
            .requiresChannel().anyRequest().requiresSecure().and() // instead of server.ssl.enabled = true
            .portMapper().http(8080).mapsTo(8443);
        

        if (!userService.enabledMasterExists()) {
            userService.deleteNotValidatedMaster();
            httpSec
                .authorizeRequests()
                    .antMatchers("/masterreg", "/masterregdo").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin().loginPage("/masterreg").permitAll();
            return;
        }

        httpSec
            .authorizeRequests()
                .antMatchers("/registration", "/reg").permitAll()
//                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
            .formLogin().loginPage("/login").permitAll()
                .and()
            .logout()
                .logoutSuccessUrl("/login?logout").permitAll();

    }

}
