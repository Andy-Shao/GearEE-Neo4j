package org.example.neo4j.core.service;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.example.neo4j.dao.PersonDao;
import org.example.neo4j.domain.Person;
import org.neo4j.driver.v1.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.andyshao.neo4j.annotation.Neo4jTransaction;
import com.github.andyshao.neo4j.model.PageReturn;
import com.github.andyshao.neo4j.model.Pageable;
import com.github.andyshao.reflect.annotation.Param;

@Service
@Neo4jTransaction
public class PersonCoreService {
    @Autowired
    private PersonDao personDao;

    public CompletionStage<Optional<Person>> findByName(@Param("name")String name,@Param("tx") Transaction tx){
        return this.personDao.findByName(name , tx);
    }
    
    @Neo4jTransaction
    CompletionStage<PageReturn<Person>> findByPage(@Param("pg")Pageable<Void> pageable,@Param("tx") Transaction tx){
        return this.personDao.findByPage(pageable , tx);
    }
}
