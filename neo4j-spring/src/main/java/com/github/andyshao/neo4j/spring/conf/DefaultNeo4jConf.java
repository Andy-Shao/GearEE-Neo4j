package com.github.andyshao.neo4j.spring.conf;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
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
import com.github.andyshao.neo4j.spring.dao.impl.SpringDaoProcessor;
import com.github.andyshao.neo4j.spring.tx.Neo4jTransactionAspect;

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
@Import(Neo4jTransactionAspect.class)
public abstract class DefaultNeo4jConf implements ImportAware {
    private final DaoConfiguration dc = new DaoConfiguration();
    
    @Bean
    public Neo4jDaoScanner neo4jDaoScanner(@Autowired DaoContext daoContext) {
        Neo4jDaoScanner neo4jDaoScanner = new Neo4jDaoScanner();
        neo4jDaoScanner.setDaoContext(daoContext);
        return neo4jDaoScanner;
    }
    
    protected Neo4jPros neo4jPros() {
        return new Neo4jPros.Neo4jProsBuilder().build();
    }
    
    protected abstract List<Package> scannerPackages();
    
    @Bean
    public DaoContext neo4jDaoContext(@Autowired DaoFactory daoFactory) {
        return this.dc.daoContext(scannerPackages() , daoFactory);
    }
    
    @Bean
    public DaoFactory neo4jDaoFactory(@Autowired DaoProcessor daoProcessor) {
        return this.dc.daoFactory(daoProcessor);
    }

    @Bean
    public DaoProcessor neo4jDaoProcessor(@Autowired SqlCompute sqlCompute , @Autowired DeSerializer deSerializer, 
        @Autowired Driver neo4jDriver) {
        return new SpringDaoProcessor(sqlCompute , deSerializer , neo4jDriver);
    }

    @Bean
    public DeSerializer neo4jDeSerializer() {
        return this.dc.deSerializer();
    }

    @Bean(destroyMethod = "close")
    public Driver neo4jDriver() {
        return GraphDatabase.driver(neo4jPros().getUrl() , neo4jAuthToken() , neo4jConfig());
    }
    
    protected AuthToken neo4jAuthToken() {
        AuthTokenInfo info = neo4jPros().getAuthToken();
        return AuthTokens.basic(info.getUsername() , info.getPassword() , info.getRealm());
    }

    protected Config neo4jConfig() {
        return Config.defaultConfig();
    }

    @Bean
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
    }
}
