package com.foo.spring.boot.camunda.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class CamundaDataSourceConfig {

    @Bean(name = "camundaBpmDataSource")
    @ConfigurationProperties(prefix = "spring.camunda-datasource")
    public DataSource camundaBpmDataSource() {
        return DataSourceBuilder.create().build();
    }
}




