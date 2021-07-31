package com.github.andyshao.neo4j.spring.config;

import com.github.andyshao.neo4j.process.*;
import com.github.andyshao.neo4j.process.config.DaoConfiguration;
import com.github.andyshao.neo4j.process.dao.DaoFactory;
import com.github.andyshao.neo4j.process.serializer.FormatterResult;
import com.github.andyshao.neo4j.process.sql.SqlAnalysis;
import com.github.andyshao.neo4j.spring.annotation.EnableNeo4jDao;
import com.github.andyshao.neo4j.spring.transaction.Neo4jTransactionAspect;
import org.neo4j.driver.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Import({Neo4jTransactionAspect.class})
public abstract class DefaultNeo4jConfiguration implements ImportAware {
    private final DaoConfiguration dc = new DaoConfiguration();
//    private Set<Package> scannerPackages = Sets.newHashSet();

    protected Neo4jPros neo4jPros() {
        return new Neo4jPros.Neo4jProsBuilder().build();
    }

    protected Config neo4jConfig() {
        return Config.defaultConfig();
    }

    protected AuthToken neo4jAuthToken() {
        Neo4jPros.AuthTokenInfo info = neo4jPros().getAuthToken();
        return AuthTokens.basic(info.getUsername() , info.getPassword() , info.getRealm());
    }

    protected abstract  Set<Package> scannerPackages();

    @Bean
    public DaoScanner daoScanner() {
        return new ClassPathAnnotationDaoScanner(scannerPackages().toArray(new Package[0]));
    }

    @Bean(destroyMethod = "close")
    public Driver neo4jDriver() {
        return GraphDatabase.driver(neo4jPros().getUrl() , neo4jAuthToken() , neo4jConfig());
    }

    @Bean
    public EntityScanner entityScanner() {
        return this.dc.entityScanner();
    }

    @Bean
    public SqlAnalysis sqlAnalysis() {
        return this.dc.sqlAnalysis();
    }

    @Bean
    public FormatterResult formatterResult(@Autowired EntityScanner entityScanner) {
        return this.dc.formatterResult(entityScanner);
    }

    @Bean
    public DaoProcessor daoProcessor(SqlAnalysis sqlAnalysis, FormatterResult formatterResult) {
        return this.dc.daoProcessor(sqlAnalysis, formatterResult);
    }

    @Bean
    public DaoFactory daoFactory(DaoProcessor daoProcessor) {
        return this.dc.daoFactory(daoProcessor);
    }

    @Bean
    public Neo4jDaoDefinitionRegistryPostProcessor neo4jDaoDefinitionRegistryPostProcessor(
            @Autowired DaoScanner daoScanner, @Autowired DaoFactory daoFactory) {
        Neo4jDaoDefinitionRegistryPostProcessor scanner = new Neo4jDaoDefinitionRegistryPostProcessor();
        scanner.setDaoFactory(daoFactory);
        scanner.setDaoScanner(daoScanner);
        return scanner;
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        Map<String , Object> enableAttrMap = importMetadata.getAnnotationAttributes(EnableNeo4jDao.class.getName());
        AnnotationAttributes enableAttrs = AnnotationAttributes.fromMap(enableAttrMap);
        Class<?>[] packageClasses = enableAttrs.getClassArray("packageClasses");
        Set<Package> scannerPackages = new HashSet<>();
        for (Class<?> pkgClazz : packageClasses)
            scannerPackages.add(pkgClazz.getPackage());
    }
}
