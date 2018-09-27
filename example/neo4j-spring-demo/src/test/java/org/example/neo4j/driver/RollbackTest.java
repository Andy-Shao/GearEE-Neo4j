package org.example.neo4j.driver;

import org.example.neo4j.Neo4jTestApplication;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Neo4jTestApplication.class)
public class RollbackTest {
    @Autowired
    private Driver driver;
    
    @Test
    @Ignore
    public void testRollback() {
        Session session = this.driver.session();
        Transaction tx = session.beginTransaction();
        tx.runAsync("MATCH (u:User) (u2:User) RETURN u")
            .thenComposeAsync(src -> src.nextAsync())
            .whenCompleteAsync((record, ex)->{
                if(ex != null) log.error("STEP TWO", ex);
                else log.info("ex is null");
            })
            .toCompletableFuture().join();
    }
}
