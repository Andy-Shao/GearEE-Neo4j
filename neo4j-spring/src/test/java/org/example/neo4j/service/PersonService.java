package org.example.neo4j.service;

import com.github.andyshao.neo4j.annotation.Neo4jTransaction;
import org.example.neo4j.dao.PersonDao;
import org.example.neo4j.domain.Person;
import org.example.neo4j.domain.PersonId;
import org.neo4j.driver.async.AsyncTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Service
@Neo4jTransaction
public class PersonService {
    @Autowired
    private PersonDao personDao;

    public Mono<Person> findAdmin(CompletionStage<AsyncTransaction> tx) {
        PersonId id = new PersonId();
        id.setId("ERHSBSYKAHV04SNIPHUPBDR");
        return this.personDao.findByPk(id, tx);
    }
}
