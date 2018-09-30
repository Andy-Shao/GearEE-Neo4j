package com.github.andyshao.neo4j.dao.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;

import com.github.andyshao.neo4j.dao.DaoFactory;
import com.github.andyshao.neo4j.dao.conf.DaoConfiguration;
import com.github.andyshao.neo4j.demo.Api;
import com.github.andyshao.neo4j.demo.ApiDao;
import com.github.andyshao.neo4j.demo.ApiKey;
import com.github.andyshao.neo4j.mapper.impl.PackageMapperScanner;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;

public class DataFactoryTest {
    private static DaoFactory daoFactory;
    private static Map<String , Neo4jDaoInfo> scan;
    
    @BeforeAll
    public static void before() {
        DaoConfiguration dc = new DaoConfiguration();
        daoFactory = dc.daoFactory(dc.daoProcessor(dc.sqlCompute() , dc.deSerializer()));
        PackageMapperScanner scanner = new PackageMapperScanner();
        scanner.setPackagePath(ApiDao.class.getPackage());
        scan  = scanner.scan();
    }
    
    @Test
    public void testBuildDao() {
        ApiDao apiDao = (ApiDao) daoFactory.buildDao(scan.get("ApiDao"));
        assertNotNull(apiDao);
    }
    
    @Test
    @Disabled
    public void testFindInfos() {
        ApiDao apiDao = (ApiDao) daoFactory.buildDao(scan.get("ApiDao"));
        try(Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j" , "1303595"));
                Session session = driver.session();){
            Transaction tx = session.beginTransaction();
            ApiKey pk = new ApiKey();
            pk.setApiName("api001");
            pk.setSystemAlias("my.system");
            CompletionStage<Optional<Api>> findByPk = apiDao.findByPk(pk  , tx);
            findByPk.thenApplyAsync(op -> tx.commitAsync());
            System.out.println(findByPk.toCompletableFuture().join());
        }
    }
}
