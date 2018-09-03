package org.example.neo4j.dao;

import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.example.neo4j.Neo4jTestApplication;
import org.example.neo4j.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.andyshao.neo4j.dao.DaoContext;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Neo4jTestApplication.class)
public class PersonDaoTest {
    @Autowired
    private Driver driver;
    @Autowired
    private DaoContext daoContext;
    @Autowired
    private PersonDao personDao;
    
    @Test
    public void test() {
        System.out.println("Starting...");
        PersonDao personDao = daoContext.getDao(PersonDao.class);
        assertTrue(personDao != null);
        Map<String , Object> daos = daoContext.getDaos();
        for(Entry<String , Object> entry : daos.entrySet()) {
            System.out.println(String.format("%s : %s" , entry.getKey(), entry.getValue().getClass().getInterfaces()[0].getName()));
        }
        
        
        Session session = driver.session();
        final Transaction tx = session.beginTransaction();
        CompletionStage<Optional<Person>> findByName = this.personDao.findByName("andy.shao" , tx);
        findByName.thenAcceptAsync(op -> tx.commitAsync());
        Optional<Person> op = findByName.toCompletableFuture().join();
        if(op.isPresent()) System.out.println(op.get());
        System.out.println("END!!");
    }
}
