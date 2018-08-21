package com.github.andyshao.neo4j.spring.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.github.andyshao.neo4j.dao.DaoProcessor;
import com.github.andyshao.neo4j.dao.DaoProcessorParam;
import com.github.andyshao.neo4j.dao.impl.SimpleDaoFactory;
import com.github.andyshao.neo4j.demo.ApiDao;
import com.github.andyshao.neo4j.mapper.impl.PackageMapperScanner;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;
import com.github.andyshao.neo4j.model.PageReturn;

public class SpringDataDaoFactoryTest {
    private volatile SimpleDaoFactory daoFactory;
    private volatile Map<String , Neo4jDaoInfo> scan;
    
    @Before
    public void before() {
        this.daoFactory = new SimpleDaoFactory();
        this.daoFactory.setDaoProcessor(new DaoProcessor() {
            
            @Override
            public <T> Optional<T> execute(DaoProcessorParam param , Class<T> retType) {
                return null;
            }
            
            @Override
            public <T> List<T> multiRet(DaoProcessorParam param , Class<T> retType) {
                return null;
            }
            
            @Override
            public <T> PageReturn<T> findByPage(DaoProcessorParam param , Class<T> retType) {
                return null;
            }

            @Override
            public void execute(DaoProcessorParam param) {
                
            }
        });
        PackageMapperScanner scanner = new PackageMapperScanner();
        scanner.setPackagePath(ApiDao.class.getPackage());
        this.scan  = scanner.scan();
    }
    
    @Test
    public void test() {
        this.daoFactory.getDao(this.scan.get("ApiSearchDao"));
    }
}
