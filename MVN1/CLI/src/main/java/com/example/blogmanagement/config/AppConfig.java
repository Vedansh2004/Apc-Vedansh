package com.example.blogmanagement.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.example.blogmanagement.dao.BlogDAO;
import com.example.blogmanagement.dao.BlogDAOImpl;

@Configuration
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/blog?useSSL=false&serverTimezone=UTC");
        ds.setUsername("root");
        ds.setPassword("sethi@2004");
        return ds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public BlogDAO blogDAO() {
        return new BlogDAOImpl(jdbcTemplate());
    }
}
