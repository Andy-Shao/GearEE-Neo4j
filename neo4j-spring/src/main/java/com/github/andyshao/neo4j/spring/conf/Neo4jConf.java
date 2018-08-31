package com.github.andyshao.neo4j.spring.conf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.neo4j.driver.v1.AuthToken;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import com.github.andyshao.neo4j.dao.DaoContext;
import com.github.andyshao.neo4j.dao.DaoFactory;
import com.github.andyshao.neo4j.dao.DaoProcessor;
import com.github.andyshao.neo4j.dao.conf.DaoConfiguration;
import com.github.andyshao.neo4j.io.DeSerializer;
import com.github.andyshao.neo4j.mapper.SqlCompute;
import com.github.andyshao.neo4j.spring.annotation.EnableNeo4jDao;
import com.github.andyshao.neo4j.spring.conf.Neo4jPros.AuthTokenInfo;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 31, 2018<br>
 * Encoding:UNIX UTF-8
 * 
 * @author Andy.Shao
 *
 */
@Configuration
@EnableConfigurationProperties(Neo4jPros.class)
public class Neo4jConf implements ImportAware {
    private final DaoConfiguration dc = new DaoConfiguration();
    private List<Package> pkgs = new ArrayList<>();
    @Autowired
    private Neo4jPros pros;

    @Bean
    @ConditionalOnMissingBean(AuthToken.class)
    public AuthToken neo4jAuthToken() {
        AuthTokenInfo info = new AuthTokenInfo();
        info.setUsername("neo4j");
        info.setPassword("1303595");
        return AuthTokens.basic(info.getUsername() , info.getPassword() , info.getRealm());
    }

    @Bean
    @ConditionalOnMissingBean(Config.class)
    public Config neo4jConfig() {
        return Config.defaultConfig();
    }

    @Bean
    @ConditionalOnMissingBean(DaoContext.class)
    public DaoContext neo4jDaoContext(@Autowired DaoFactory daoFactory) {
        return this.dc.daoContext(this.pkgs , daoFactory);
    }

    @Bean
    @ConditionalOnMissingBean(DaoFactory.class)
    public DaoFactory neo4jDaoFactory(@Autowired DaoProcessor daoProcessor) {
        return this.dc.daoFactory(daoProcessor);
    }

    @Bean
    @ConditionalOnMissingBean(DaoProcessor.class)
    public DaoProcessor neo4jDaoProcessor(@Autowired SqlCompute sqlCompute , @Autowired DeSerializer deSerializer) {
        return this.dc.daoProcessor(sqlCompute , deSerializer);
    }

    @Bean
    @ConditionalOnMissingBean(DeSerializer.class)
    public DeSerializer neo4jDeSerializer() {
        return this.dc.deSerializer();
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean(Driver.class)
    public Driver neo4jDriver(@Autowired AuthToken authToken , @Autowired Config conf) {
        return GraphDatabase.driver("bolt://localhost:7687" , authToken , conf);
    }

    @Bean
    @ConditionalOnMissingBean(SqlCompute.class)
    public SqlCompute neo4jSqlCompute() {
        return this.dc.sqlCompute();
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        Map<String , Object> enableAttrMap = importMetadata.getAnnotationAttributes(EnableNeo4jDao.class.getName());
        AnnotationAttributes enableAttrs = AnnotationAttributes.fromMap(enableAttrMap);
        Class<?>[] packageClasses = enableAttrs.getClassArray("packageClasses");
        Set<Package> tmp = new HashSet<>();
        for (Class<?> pkgClazz : packageClasses)
            tmp.add(pkgClazz.getPackage());
        this.pkgs = new ArrayList<>(tmp);
    }
}
