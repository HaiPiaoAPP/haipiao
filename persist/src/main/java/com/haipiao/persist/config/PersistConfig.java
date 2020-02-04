package com.haipiao.persist.config;

import com.haipiao.persist.property.PersistProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Collections;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.haipiao.persist.property"})
@EnableJpaRepositories(basePackages = {"com.haipiao.persist.repository"})
public class PersistConfig {

    private static final String DATA_BASE_PLATFORM = "org.hibernate.dialect.PostgreSQLDialect";
    private static final String DOMAIN_ENTITIES_PACKAGE = "com.haipiao.persist.entity";

    @Autowired
    PersistProperties persistProperties;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(persistProperties.getPostgres().getDriverClassName());
        dataSource.setUrl(persistProperties.getPostgres().getUrl());
        dataSource.setUsername(persistProperties.getPostgres().getUsername());
        dataSource.setPassword(persistProperties.getPostgres().getPassword());
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter hibernateJpa = new HibernateJpaVendorAdapter();
        hibernateJpa.setDatabasePlatform(DATA_BASE_PLATFORM);
        hibernateJpa.setShowSql(true);

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPackagesToScan(DOMAIN_ENTITIES_PACKAGE);
        emf.setJpaVendorAdapter(hibernateJpa);
        emf.setJpaPropertyMap(Collections.singletonMap("javax.persistence.validation.mode", "none"));
        return emf;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager txnMgr = new JpaTransactionManager();
        txnMgr.setEntityManagerFactory(entityManagerFactory().getObject());
        return txnMgr;
    }

}
