package com.github.andyshao.neo4j.spring.dao.impl;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.github.andyshao.neo4j.dao.DaoProcessor;
import com.github.andyshao.neo4j.dao.DaoProcessorParam;
import com.github.andyshao.neo4j.dao.impl.SimpleDaoFactory;
import com.github.andyshao.neo4j.demo.ApiDao;
import com.github.andyshao.neo4j.mapper.impl.PackageMapperScanner;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;

public class SimpleDataFactoryTest {
    private volatile SimpleDaoFactory daoFactory;
    private volatile Map<String , Neo4jDaoInfo> scan;
    
    @Before
    public void before() {
        this.daoFactory = new SimpleDaoFactory();
        this.daoFactory.setDaoProcessor(new DaoProcessor() {

            @Override
            public <T> T process(DaoProcessorParam param , Neo4jDaoInfo neo4jDaoInfo) {
                return null;
            }
        });
        PackageMapperScanner scanner = new PackageMapperScanner();
        scanner.setPackagePath(ApiDao.class.getPackage());
        this.scan  = scanner.scan();
    }
    
    @Test
//    @Ignore
    public void test() {
        this.daoFactory.buildDao(this.scan.get("ApiDao"));
    }
}
