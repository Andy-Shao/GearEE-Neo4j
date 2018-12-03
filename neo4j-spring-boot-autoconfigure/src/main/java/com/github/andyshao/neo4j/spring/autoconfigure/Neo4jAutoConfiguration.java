package com.github.andyshao.neo4j.spring.autoconfigure;

import java.util.List;
import java.util.stream.Collectors;

import org.neo4j.driver.v1.AuthToken;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.andyshao.neo4j.dao.DaoContext;
import com.github.andyshao.neo4j.dao.DaoFactory;
import com.github.andyshao.neo4j.dao.DaoProcessor;
import com.github.andyshao.neo4j.dao.conf.DaoConfiguration;
import com.github.andyshao.neo4j.io.DeSerializer;
import com.github.andyshao.neo4j.mapper.SqlCompute;
import com.github.andyshao.neo4j.spring.autoconfigure.Neo4jPros.AuthTokenInfo;
import com.github.andyshao.neo4j.spring.conf.Neo4jDaoScanner;
import com.github.andyshao.neo4j.spring.dao.impl.SpringDaoProcessor;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Dec 3, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
@Configuration
@EnableConfigurationProperties(Neo4jPros.class)
@Import(Neo4jAutoScannerRegsitrar.class)
public class Neo4jAutoConfiguration implements BeanFactoryAware{
    private List<String> packages;
    private final DaoConfiguration dc = new DaoConfiguration();
    @Autowired
    private Neo4jPros neo4jPros;
    
    @Bean
    @ConditionalOnMissingBean
    public Neo4jDaoScanner neo4jDaoScanner(@Autowired DaoContext daoContext) {
        Neo4jDaoScanner neo4jDaoScanner = new Neo4jDaoScanner();
        neo4jDaoScanner.setDaoContext(daoContext);
        return neo4jDaoScanner;
    }
    
    @Bean
    @ConditionalOnMissingBean
    public DaoContext neo4jDaoContext(@Autowired DaoFactory daoFactory) {
        List<Package> scannerPackages = packages.stream().map(Package::getPackage).collect(Collectors.toList());
        return this.dc.daoContext(scannerPackages , daoFactory);
    }
    
    @Bean
    @ConditionalOnMissingBean
    public DaoFactory neo4jDaoFactory(@Autowired DaoProcessor daoProcessor) {
        return this.dc.daoFactory(daoProcessor);
    }

    @Bean
    @ConditionalOnMissingBean
    public DaoProcessor neo4jDaoProcessor(@Autowired SqlCompute sqlCompute , @Autowired DeSerializer deSerializer, 
        @Autowired Driver neo4jDriver) {
        return new SpringDaoProcessor(sqlCompute , deSerializer , neo4jDriver);
    }

    @Bean
    @ConditionalOnMissingBean
    public DeSerializer neo4jDeSerializer() {
        return this.dc.deSerializer();
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean
    public Driver neo4jDriver() {
        return GraphDatabase.driver(neo4jPros.getUrl() , neo4jAuthToken() , neo4jConfig());
    }
    
    protected AuthToken neo4jAuthToken() {
        AuthTokenInfo info = neo4jPros.getAuthToken();
        return AuthTokens.basic(info.getUsername() , info.getPassword() , info.getRealm());
    }

    protected Config neo4jConfig() {
        return Config.defaultConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlCompute neo4jSqlCompute() {
        return this.dc.sqlCompute();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.packages = AutoConfigurationPackages.get(beanFactory);
    }

}
