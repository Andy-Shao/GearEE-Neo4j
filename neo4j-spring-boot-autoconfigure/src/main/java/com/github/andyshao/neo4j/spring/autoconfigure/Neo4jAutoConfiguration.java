package com.github.andyshao.neo4j.spring.autoconfigure;

import com.github.andyshao.neo4j.process.*;
import com.github.andyshao.neo4j.process.config.DaoConfiguration;
import com.github.andyshao.neo4j.process.serializer.FormatterResult;
import com.github.andyshao.neo4j.process.sql.SqlAnalysis;
import com.github.andyshao.neo4j.spring.transaction.Neo4jTransactionAspect;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/30
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Slf4j
@Configuration
@AutoConfigureOrder
@EnableConfigurationProperties(Neo4jPros.class)
@Import(Neo4jTransactionAspect.class)
public class Neo4jAutoConfiguration implements BeanFactoryAware {
    private final DaoConfiguration dc = new DaoConfiguration();
    private List<String> packages;
    @Autowired
    private Neo4jPros neo4jPros;

    @Configuration
    @Import(Neo4jAutoScannerRegister.class)
    @ConditionalOnMissingBean(Neo4jDaoFactoryBean.class)
    public static class Neo4jScanMissing {
        @PostConstruct
        public void afterPropertiesSet() {
            log.debug("No {} found.", Neo4jDaoFactoryBean.class);
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.packages = AutoConfigurationPackages.get(beanFactory);
    }

    protected AuthToken neo4jAuthToken() {
        Neo4jPros.AuthTokenInfo info = neo4jPros.getAuthToken();
        return AuthTokens.basic(info.getUsername() , info.getPassword() , info.getRealm());
    }

    @Bean
    @ConditionalOnMissingBean
    public Config neo4jConfig() {
        return Config.defaultConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    public DaoScanner daoScanner(@Autowired Neo4jPros pros) {
        return new ClassPathAnnotationDaoScanner(this.packages.toArray(new String[0]));
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean
    public Driver neo4jDriver(@Autowired Config neo4jConfig) {
        return GraphDatabase.driver(this.neo4jPros.getUrl() , neo4jAuthToken() , neo4jConfig);
    }

    @Bean
    @ConditionalOnMissingBean
    public EntityScanner entityScanner() {
        return this.dc.entityScanner();
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlAnalysis sqlAnalysis() {
        return this.dc.sqlAnalysis();
    }

    @Bean
    @ConditionalOnMissingBean
    public FormatterResult formatterResult(@Autowired EntityScanner entityScanner) {
        return this.dc.formatterResult(entityScanner);
    }

    @Bean
    @ConditionalOnMissingBean
    public DaoProcessor daoProcessor(SqlAnalysis sqlAnalysis, FormatterResult formatterResult) {
        return this.dc.daoProcessor(sqlAnalysis, formatterResult);
    }

    @Bean
    @ConditionalOnMissingBean
    public DaoFactory daoFactory(DaoProcessor daoProcessor) {
        return this.dc.daoFactory(daoProcessor);
    }
}
