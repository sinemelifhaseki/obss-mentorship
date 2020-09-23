package com.obsssummerintern.mentorship.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.connect.GoogleConnectionFactory;

import javax.inject.Inject;
import javax.sql.DataSource;

public class SocialConfig {
    @Inject
    private Environment environment;

    @Inject
    private DataSource dataSource;

    @Bean
    public ConnectionFactoryLocator connectionFactoryLocator(){
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.addConnectionFactory(new GoogleConnectionFactory(environment.getProperty("google.clientId"), environment.getProperty("google.clientSecret")));
        return registry;
    }


}
