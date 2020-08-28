package com.github.andyshao.neo4j.demo.dao;

import com.github.andyshao.neo4j.demo.Person;
import com.github.andyshao.neo4j.demo.PersonId;
import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.neo4j.domain.Neo4jSql;
import com.github.andyshao.neo4j.domain.Pageable;
import com.github.andyshao.neo4j.process.DaoProcessor;
import org.neo4j.driver.async.AsyncTransaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class PersonDaoImpl implements PersonDao {
    private final DaoProcessor daoProcessor;
    private final Neo4jDao neo4jDao;

    public PersonDaoImpl(DaoProcessor daoProcessor, Neo4jDao neo4jDao) {
        this.daoProcessor = daoProcessor;
        this.neo4jDao = neo4jDao;
    }

    @Override
    public Mono<Person> findByPk(PersonId id, CompletionStage<AsyncTransaction> transaction) {
        Class<?>[] argTypes = {PersonId.class, CompletionStage.class};
        Optional<Neo4jSql> neo4jSql = this.neo4jDao.findNeo4jSql("findByPk", argTypes);
        Object[] args = {id, transaction};
        Neo4jSql sqlDefinition = neo4jSql.get();
        return this.daoProcessor.processing(this.neo4jDao, sqlDefinition, args);
    }

    @Override
    public Mono<String> findNameByPk(PersonId id, CompletionStage<AsyncTransaction> transaction) {
        Class<?>[] argTypes = {PersonId.class, CompletionStage.class};
        Optional<Neo4jSql> neo4jSql = this.neo4jDao.findNeo4jSql("findNameByPk", argTypes);
        Object[] args = {id, transaction};
        Neo4jSql sqlDefinition = neo4jSql.get();
        return this.daoProcessor.processing(this.neo4jDao, sqlDefinition, args);
    }

    @Override
    public Flux<Person> findByName(String name, CompletionStage<AsyncTransaction> transaction) {
        Class<?>[] argTypes = {String.class, CompletionStage.class};
        Optional<Neo4jSql> neo4jSql = this.neo4jDao.findNeo4jSql("findByName", argTypes);
        Object[] args = {name, transaction};
        Neo4jSql sqlDefinition = neo4jSql.get();
        return this.daoProcessor.processing(this.neo4jDao, sqlDefinition, args);
    }

    @Override
    public Mono<Person> save(Person person, CompletionStage<AsyncTransaction> transaction) {
        Class<?>[] argTypes = {Person.class, CompletionStage.class};
        Optional<Neo4jSql> neo4jSql = this.neo4jDao.findNeo4jSql("save", argTypes);
        Object[] args = {person, transaction};
        Neo4jSql sqlDefinition = neo4jSql.get();
        return this.daoProcessor.processing(this.neo4jDao, sqlDefinition, args);
    }

    @Override
    public Flux<Person> saveByList(List<Person> ps, CompletionStage<AsyncTransaction> transaction) {
        Class<?>[] argTypes = {List.class, CompletionStage.class};
        Optional<Neo4jSql> neo4jSql = this.neo4jDao.findNeo4jSql("saveByList", argTypes);
        Object[] args = {ps, transaction};
        Neo4jSql sqlDefinition = neo4jSql.get();
        return this.daoProcessor.processing(this.neo4jDao, sqlDefinition, args);
    }

    @Override
    public Flux<Person> findByAge(Pageable<Integer> age, CompletionStage<AsyncTransaction> transaction) {
        Class<?>[] argTypes = {Pageable.class, CompletionStage.class};
        Optional<Neo4jSql> neo4jSql = this.neo4jDao.findNeo4jSql("findByAge", argTypes);
        Object[] args = {age, transaction};
        Neo4jSql sqlDefinition = neo4jSql.get();
        return this.daoProcessor.processing(this.neo4jDao, sqlDefinition, args);
    }

    @Override
    public Flux<Person> findByAge(Integer age, CompletionStage<AsyncTransaction> transaction) {
        Class<?>[] argTypes = {Integer.class, CompletionStage.class};
        Optional<Neo4jSql> neo4jSql = this.neo4jDao.findNeo4jSql("findByAge", argTypes);
        Object[] args = {age, transaction};
        Neo4jSql sqlDefinition = neo4jSql.get();
        return this.daoProcessor.processing(this.neo4jDao, sqlDefinition, args);
    }

    @Override
    public Flux<Person> findByAgeAndName(Integer age, String name, CompletionStage<AsyncTransaction> transaction) {
        Class<?>[] argTypes = {Integer.class, String.class, CompletionStage.class};
        Optional<Neo4jSql> neo4jSql = this.neo4jDao.findNeo4jSql("findByAgeAndName", argTypes);
        Object[] args = {age, name, transaction};
        Neo4jSql sqlDefinition = neo4jSql.get();
        return this.daoProcessor.processing(this.neo4jDao, sqlDefinition, args);
    }
}
