package org.example.neo4j.dao;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.example.neo4j.Neo4jTestApplication;
import org.example.neo4j.domain.SystemInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Neo4jTestApplication.class)
public class SystemDaoTest {
    @Autowired
    private SystemDao systemDao;
    
    @Test
    public void testCreate() {
        SystemInfo system = new SystemInfo();
        system.setSystemAlias("my.test");
        CompletionStage<Optional<SystemInfo>> systemInfo = systemDao.create(system , null);
        System.out.println(systemInfo.toCompletableFuture().join());
    }
}
