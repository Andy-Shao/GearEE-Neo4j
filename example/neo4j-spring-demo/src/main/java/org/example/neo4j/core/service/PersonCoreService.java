package org.example.neo4j.core.service;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.example.neo4j.dao.PersonDao;
import org.example.neo4j.domain.Person;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.andyshao.reflect.annotation.Param;

@Service
public class PersonCoreService {
    @Autowired
    private Driver driver;
    @Autowired
    private PersonDao personDao;

    @SuppressWarnings("resource")
    public CompletionStage<Optional<Person>> findByName(@Param("name")String name,@Param("tx") Transaction tx){
        final Session session = tx == null ? driver.session() : null;
        final Transaction transaction = session == null ? tx : session.beginTransaction();
        CompletionStage<Optional<Person>> ret = personDao.findByName(name , transaction);
        if(session != null) ret.thenAcceptAsync(op -> transaction.commitAsync().thenAcceptAsync(v -> session.closeAsync()));
        return ret;
    }
}
