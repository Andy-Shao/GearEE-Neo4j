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
    public Mono<Person> findByPk(PersonId id, AsyncTransaction transaction) {
        Class<?>[] argTypes = {PersonId.class, AsyncTransaction.class};
        Optional<Neo4jSql> neo4jSql = this.neo4jDao.findNeo4jSql("findByPk", argTypes);
        if(neo4jSql.isPresent()) {
            Object[] args = {id, transaction};
            Neo4jSql sqlDefinition = neo4jSql.get();
            return this.daoProcessor.processing(this.neo4jDao, sqlDefinition, transaction, args);
        }
        return null;
    }

    @Override
    public Mono<String> findNameByPk(PersonId id, AsyncTransaction transaction) {
        Class<?>[] argTypes = {PersonId.class, AsyncTransaction.class};
        Optional<Neo4jSql> neo4jSql = this.neo4jDao.findNeo4jSql("findNameByPk", argTypes);
        if(neo4jSql.isPresent()) {
            Object[] args = {id, transaction};
            Neo4jSql sqlDefinition = neo4jSql.get();
            return this.daoProcessor.processing(this.neo4jDao, sqlDefinition, transaction, args);
        }
        return null;
    }

    @Override
    public Flux<Person> findByName(String name, AsyncTransaction transaction) {
        Class<?>[] argTypes = {String.class, AsyncTransaction.class};
        Optional<Neo4jSql> neo4jSql = this.neo4jDao.findNeo4jSql("findByName", argTypes);
        if(neo4jSql.isPresent()) {
            Object[] args = {name, transaction};
            Neo4jSql sqlDefinition = neo4jSql.get();
            return this.daoProcessor.processing(this.neo4jDao, sqlDefinition, transaction, args);
        }
        return null;
    }

    @Override
    public Mono<Person> save(Person person, AsyncTransaction transaction) {
        Class<?>[] argTypes = {Person.class, AsyncTransaction.class};
        Optional<Neo4jSql> neo4jSql = this.neo4jDao.findNeo4jSql("save", argTypes);
        if(neo4jSql.isPresent()) {
            Object[] args = {person, transaction};
            Neo4jSql sqlDefinition = neo4jSql.get();
            return this.daoProcessor.processing(this.neo4jDao, sqlDefinition, transaction, args);
        }
        return null;
    }

    @Override
    public Flux<Person> saveByList(List<Person> ps, AsyncTransaction transaction) {
        Class<?>[] argTypes = {List.class, AsyncTransaction.class};
        Optional<Neo4jSql> neo4jSql = this.neo4jDao.findNeo4jSql("saveByList", argTypes);
        if(neo4jSql.isPresent()) {
            Object[] args = {ps, transaction};
            Neo4jSql sqlDefinition = neo4jSql.get();
            return this.daoProcessor.processing(this.neo4jDao, sqlDefinition, transaction, args);
        }
        return null;
    }

    @Override
    public Flux<Person> findByAge(Pageable<Integer> age, AsyncTransaction transaction) {
        Class<?>[] argTypes = {Pageable.class, AsyncTransaction.class};
        Optional<Neo4jSql> neo4jSql = this.neo4jDao.findNeo4jSql("findByAge", argTypes);
        if(neo4jSql.isPresent()) {
            Object[] args = {age, transaction};
            Neo4jSql sqlDefinition = neo4jSql.get();
            return this.daoProcessor.processing(this.neo4jDao, sqlDefinition, transaction, args);
        }
        return null;
    }

    @Override
    public Flux<Person> findByAge(Integer age, AsyncTransaction transaction) {
        Class<?>[] argTypes = {Integer.class, AsyncTransaction.class};
        Optional<Neo4jSql> neo4jSql = this.neo4jDao.findNeo4jSql("findByAge", argTypes);
        if(neo4jSql.isPresent()) {
            Object[] args = {age, transaction};
            Neo4jSql sqlDefinition = neo4jSql.get();
            return this.daoProcessor.processing(this.neo4jDao, sqlDefinition, transaction, args);
        }
        return null;
    }
}
