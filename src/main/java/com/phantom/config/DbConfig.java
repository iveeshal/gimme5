package com.phantom.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import javax.annotation.Resource;

/**
 * Created by vishal on 10/28/15.
 */

@Configuration
@PropertySource("classpath:gimmeFive.properties")
public class DbConfig {

@Resource
private Environment env;


    @Value("${mysql.insertstoredProcName:wth_insert}")
    private String insertstoredProcName;


    @Value("${mysql.countStoredProcName:wth_count}")
    private String countStoredProcName;

    @Bean
    public javax.sql.DataSource dataSource(){
        DataSource dataSource= new DataSource();
        dataSource.setDriverClassName(env.getProperty("mysql.driver", "com.mysql.jdbc.Driver"));
        dataSource.setUrl(env.getProperty("mysql.jdbcurl",
                "jdbc:mysql://127.0.0.1:3306/rating_schema?autoReconnect=true"));
        dataSource.setUsername(env.getProperty("mysql.user", "root"));
        dataSource.setPassword(env.getProperty("mysql.password", "admin"));
        return dataSource;
    }

    @Bean
    public JdbcTemplate setJdbcTemplate(){
        JdbcTemplate jdbcTemplate= new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }

    @Bean(name="insert")
    public SimpleJdbcCall setJdbcSimpleCallForInsert(){
        SimpleJdbcCall simpleJdbc= new SimpleJdbcCall(dataSource()).
                withProcedureName(insertstoredProcName);
        return simpleJdbc;
    }

    @Bean(name="count")
    public SimpleJdbcCall setJdbcSimpleCallForCount(){
        SimpleJdbcCall simpleJdbc= new SimpleJdbcCall(dataSource()).
                withProcedureName(countStoredProcName);
        return simpleJdbc;
    }

    @Bean
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(){
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource());
        return namedParameterJdbcTemplate;
    }
}
