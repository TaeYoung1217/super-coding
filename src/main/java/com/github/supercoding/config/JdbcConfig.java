package com.github.supercoding.config;

import com.github.supercoding.properties.DataSourceProperties;
import com.github.supercoding.properties.DataSourceProperties2;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties({DataSourceProperties.class, DataSourceProperties2.class})
@RequiredArgsConstructor
public class JdbcConfig {
    private final DataSourceProperties dataSourceProperties;
    private final DataSourceProperties2 dataSourceProperties2;

    @Bean
    public DataSource dataSource1() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUsername(dataSourceProperties.getUsername()); //보안을 위해 가려야함
        dataSource.setPassword(dataSourceProperties.getPassword()); //보안을 위해 가려야함
        dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        dataSource.setUrl(dataSourceProperties.getUrl());

        return dataSource;
    }

    @Bean
//    @Primary
    public JdbcTemplate jdbcTemplate1() {
        return new JdbcTemplate(dataSource1());
    }

    @Bean(name = "tm1")
    public PlatformTransactionManager transactionManager1() {
        return new DataSourceTransactionManager(dataSource1());
    }


    @Bean
    public DataSource dataSource2() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUsername(dataSourceProperties2.getUsername()); //보안을 위해 가려야함
        dataSource.setPassword(dataSourceProperties2.getPassword()); //보안을 위해 가려야함
        dataSource.setDriverClassName(dataSourceProperties2.getDriverClassName());
        dataSource.setUrl(dataSourceProperties2.getUrl());

        return dataSource;
    }
    @Bean
    @Primary
    public JdbcTemplate jdbcTemplate2() {
        return new JdbcTemplate(dataSource2());
    }
    @Bean(name = "tm2")
    public PlatformTransactionManager transactionManager2() {
        return new DataSourceTransactionManager(dataSource2());
    }
}
