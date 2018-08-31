package com.github.andyshao.neo4j.dao.impl;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;

import com.github.andyshao.neo4j.dao.DaoContext;
import com.github.andyshao.neo4j.dao.DaoFactory;
import com.github.andyshao.neo4j.dao.conf.DaoConfiguration;
import com.github.andyshao.neo4j.demo.Api;
import com.github.andyshao.neo4j.demo.ApiDao;
import com.github.andyshao.neo4j.demo.ApiKey;

public class DaoContextTest {
    private static DaoContext daoContext;
    
    @BeforeClass
    public static void beforeClass() {
        DaoConfiguration dc = new DaoConfiguration();
        DaoFactory daoFactory = dc.daoFactory(dc.daoProcessor(dc.sqlCompute() , dc.deSerializer()));
        daoContext = dc.daoContext(Arrays.asList(ApiDao.class.getPackage()) , daoFactory);
    }

    @Test
    public void testBuildDao() {
        ApiDao apiDao = daoContext.getDao(ApiDao.class);
        assertTrue(apiDao != null);
    }
    
    @Test
    @Ignore
    public void testApiDao() {
        ApiDao apiDao = daoContext.getDao(ApiDao.class);
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
