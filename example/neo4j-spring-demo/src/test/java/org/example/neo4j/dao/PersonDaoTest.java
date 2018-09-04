package org.example.neo4j.dao;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.example.neo4j.Neo4jTestApplication;
import org.example.neo4j.domain.Person;
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
import com.github.andyshao.neo4j.model.PageReturn;
import com.github.andyshao.neo4j.model.Pageable;

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
    public void testFindByName() {
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
    
    @Test
    public void testRemoveByName() {
        final Transaction tx = this.driver.session().beginTransaction();
        CompletionStage<Void> removeByName = this.personDao.removeByName("andy.shao" , tx);
        removeByName.thenAcceptAsync(op -> tx.commitAsync());
        removeByName.toCompletableFuture().join();
    }
    
    @Test
    public void testReplace() {
        Transaction tx = this.driver.session().beginTransaction();
        Person person = new Person();
        person.setName("andy.shao");
        person.setAge(28);
        person.setPhone("13629439483");
        this.personDao.replace(person , tx)
            .thenApplyAsync(op -> tx.commitAsync())
            .toCompletableFuture()
            .join();
    }
    
    @Test
    public void testFindByPage() {
        Transaction tx = this.driver.session().beginTransaction();
        CompletionStage<PageReturn<Person>> findByPage = this.personDao.findByPage(new Pageable<>(), tx);
        findByPage.thenAcceptAsync(pg -> tx.commitAsync());
        PageReturn<Person> pg = findByPage.toCompletableFuture().join();
        System.out.println(pg.getData());
    }
    
    @Test
    public void testFindByAge() {
        Transaction tx = this.driver.session().beginTransaction();
        CompletionStage<List<Person>> findByAge = this.personDao.findByAge(18 , tx);
        findByAge.thenAcceptAsync(ls -> tx.commitAsync());
        System.out.println(findByAge.toCompletableFuture().join());
    }
    
    @Test
    public void testUpdate() {
        Transaction tx = this.driver.session().beginTransaction();
        Person person = new Person();
        person.setAge(30);
        person.setName("andy.shao");
        CompletionStage<Optional<Person>> update = this.personDao.updateSelectiveByPk(person , tx);
        update.thenAcceptAsync(op -> tx.commitAsync()).toCompletableFuture().join();
    }
    
    @Test
    public void testTrySave() {
        Transaction tx = this.driver.session().beginTransaction();
        Person person = new Person();
        person.setAge(19);
        person.setName("andy.shao");
        this.personDao.trySave(person , tx)
            .thenAcceptAsync(op -> tx.commitAsync())
            .toCompletableFuture()
            .join();
    }
    
    @Test
    public void testMapAll() {
        Transaction tx = this.driver.session().beginTransaction();
        CompletionStage<Map<String , Person>> mapAll = this.personDao.mapAll(tx);
        mapAll.thenAcceptAsync(map -> tx.commitAsync());
        System.out.println(mapAll.toCompletableFuture().join());
    }
    
    @Test
    public void testTotalSize() {
        Transaction tx = this.driver.session().beginTransaction();
        CompletionStage<Optional<Long>> totalSize = this.personDao.totalSize(tx);
        totalSize.thenAcceptAsync(op -> tx.commitAsync());
        System.out.println(totalSize.toCompletableFuture().join());
    }
}
