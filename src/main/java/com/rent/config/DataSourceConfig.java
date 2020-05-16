/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.config;

import com.zaxxer.hikari.HikariDataSource;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 *
 * @author czakot
 */
@Configuration
@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
        
    private long connectionTimeout;
    private String prefix;
    private String[] hosts;
    private String port;
    private String dbname;
    private String username;
    private String password;

    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource getDataSource() {

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setConnectionTimeout(connectionTimeout);
//        dataSource.setInitializationFailTimeout(15); // default: 1
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        for (String host : hosts) {
            String url = prefix + "://" + host + ":" + port + "/" + dbname;
            logger.info("Testing DB connection: " + url);
            dataSource.setJdbcUrl(url);
            try {
                dataSource.getConnection();
                logger.info(url + " DB connected");
                return dataSource;
            } catch (SQLException ex) {
                logger.info(url + " DB connection failed.");
            }
        }
        
        // if no available known DB, then create embedded
        logger.info("Creating embedded DB. (No available online DB)");
        String url = "jdbc:derby:rent;create=true";
        // spring.jpa.hibernate.ddl-auto=update
        DataSource embeddedDataSource = DataSourceBuilder.create().url(url).build();
//        try {
//            System.out.println("Before ***");
//            embeddedDataSource.getConnection().getClientInfo().list(System.out);
//            System.out.println("After ***");
//        } catch (SQLException ex) {
//            java.util.logging.Logger.getLogger(DataSourceConfig.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return embeddedDataSource;
    }
    
    public void setConnectionTimeout(long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setHosts(String[] hosts) {
        this.hosts = hosts;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
