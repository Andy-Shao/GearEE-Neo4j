package com.github.andyshao.neo4j.demo.dao;

import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.neo4j.annotation.Neo4jSql;
import com.github.andyshao.neo4j.demo.Person;
import com.github.andyshao.neo4j.demo.PersonId;
import com.github.andyshao.neo4j.model.Pageable;
import com.github.andyshao.reflect.annotation.Param;
import com.github.andyshao.util.EntityOperation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/26
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Neo4jDao(clipClass = PersonDaoClips.class)
public interface PersonDao {
    @Neo4jSql(sql = "MATCH (n:Person) WHERE n.id = $pk.id RETURN n")
    Mono<Person> findByPk(@Param("pk")PersonId id);

    @Neo4jSql(sql = "MATCH (n:Person) WHERE n.name = $name RETURN n")
    Flux<Person> findByName(@Param("name")String name);

    @Neo4jSql(sql = "CREATE (n:Person {id: p.id, name: p.name, age: p.age, gender: p.gender}) RETURN n")
    Mono<Person> save(@Param("p")Person person);

    default Mono<Person> saveOrUpdate(@Param("p")Person person) {
        if(Objects.isNull(person.getId())) throw new IllegalArgumentException();
        Mono<Person> inDb = findByPk(person);
        return inDb.hasElement()
                .flatMap(hasElement -> {
                    if(hasElement) {
                        return inDb
                                .flatMap(p -> {
                                    EntityOperation.copyProperties(person, p, Collections.singletonList("id"));
                                    return save(p);
                                });
                    } else return save(person);
                });
    }

    @Neo4jSql(isUseSqlClip = true, sqlClipName = "saveByList")
    Flux<Person> saveByList(@Param("ps")List<Person> ps);

    default Flux<Person> saveOrUpdateByList(@Param("ps")List<Person> persons) {
        return Flux.fromStream(persons.stream())
                .flatMap(p -> {
                    Mono<Person> inDb = findByPk(p);
                    return inDb.hasElement()
                            .flatMap(hasElement -> {
                                if(hasElement) {
                                    return inDb.flatMap(person -> {
                                        EntityOperation.copyProperties(p, person, Collections.singletonList("id"));
                                        return save(person);
                                    });
                                } else return save(p);
                            });
                });
    }

    @Neo4jSql(sql = "MATCH (n:Person) WHERE n.age = $age.data RETURN n")
    Flux<Person> findByAge(@Param("age")Pageable<Integer> age);

    @Neo4jSql(sql = "MATCH (n:Person) WHERE n.age = $age RETURN n")
    Flux<Person> findByAge(@Param("age")Integer age);
}
