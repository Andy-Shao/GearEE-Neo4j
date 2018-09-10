package org.example.neo4j.core.service;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.example.neo4j.domain.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.andyshao.neo4j.model.PageReturn;
import com.github.andyshao.neo4j.model.Pageable;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PersonCoreServiceTest {
    @Autowired
    private PersonCoreService coreService;
    
    @Test
    public void testfindByName() {
        CompletionStage<Optional<Person>> findByName = this.coreService.findByName("andy.shao" , null);
        Optional<Person> op = findByName.toCompletableFuture().join();
        System.out.println(op);
    }
    
    @Test
    public void testFindBypage() {
        CompletionStage<PageReturn<Person>> findByPage = this.coreService.findByPage(new Pageable<>() , null);
        System.out.println(findByPage.toCompletableFuture().join());
    }
}
