package com.github.andyshao.neo4j.demo.dao;

import com.github.andyshao.neo4j.annotation.FormatterResult;
import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.neo4j.annotation.Neo4jSql;
import com.github.andyshao.neo4j.demo.Person;
import com.github.andyshao.neo4j.demo.PersonId;
import com.github.andyshao.neo4j.demo.process.PersonFormatterResult;
import com.github.andyshao.neo4j.domain.Pageable;
import com.github.andyshao.reflect.annotation.Param;
import com.github.andyshao.util.EntityOperation;
import org.neo4j.driver.async.AsyncTransaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

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
    String FIND_BY_PK = "MATCH (n:Person) WHERE n.id = $pk_id RETURN n";

    /* This method is used to Junit testing. */
    @Neo4jSql(sql = FIND_BY_PK)
    Mono<Person> findByPk(@Param("pk")PersonId id, CompletionStage<AsyncTransaction> transaction);

    @Neo4jSql(sql = FIND_BY_PK)
    @FormatterResult(PersonFormatterResult.class)
    Mono<Person> findByPk2(@Param("pk") PersonId id, CompletionStage<AsyncTransaction> tx);

    /* This method is used to Junit testing. */
    @Neo4jSql(sql = "MATCH (n:Person) WHERE n.id= $pk_id RETURN n.name")
    Mono<String> findNameByPk(@Param("pk")PersonId id, CompletionStage<AsyncTransaction> transaction);

    @Neo4jSql(sql = "MATCH (n:Person) WHERE n.name = $name RETURN n")
    Flux<Person> findByName(@Param("name")String name, CompletionStage<AsyncTransaction> transaction);

    @Neo4jSql(sql = "CREATE (n:Person {id: $p_id, name: $p_name, age: $p_age, gender: $p_gender}) RETURN n")
    Mono<Person> save(@Param("p")Person person, CompletionStage<AsyncTransaction> transaction);

    default Mono<Person> saveOrUpdate(@Param("p")Person person, CompletionStage<AsyncTransaction> transaction) {
        if(Objects.isNull(person.getId())) throw new IllegalArgumentException();
        Mono<Person> inDb = findByPk(person, transaction);
        return inDb.hasElement()
                .flatMap(hasElement -> {
                    if(hasElement) {
                        return inDb
                                .flatMap(p -> {
                                    EntityOperation.copyProperties(person, p, Collections.singletonList("id"));
                                    return save(p, transaction);
                                });
                    } else return save(person, transaction);
                });
    }

    /* This method is used to be Junit testing. */
    @Neo4jSql(isUseSqlClip = true, sqlClipName = "saveByList")
    Flux<Person> saveByList(@Param("ps")List<Person> ps, CompletionStage<AsyncTransaction> transaction);

    default Flux<Person> saveOrUpdateByList(@Param("ps")List<Person> persons, CompletionStage<AsyncTransaction> transaction) {
        return Flux.fromStream(persons.stream())
                .flatMap(p -> {
                    Mono<Person> inDb = findByPk(p, transaction);
                    return inDb.hasElement()
                            .flatMap(hasElement -> {
                                if(hasElement) {
                                    return inDb.flatMap(person -> {
                                        EntityOperation.copyProperties(p, person, Collections.singletonList("id"));
                                        return save(person, transaction);
                                    });
                                } else return save(p, transaction);
                            });
                });
    }

    @Neo4jSql(sql = "MATCH (n:Person) WHERE n.age = $age_data RETURN n")
    Flux<Person> findByAge(@Param("age")Pageable<Integer> age, CompletionStage<AsyncTransaction> transaction);

    @Neo4jSql(sql = "MATCH (n:Person) WHERE n.age = $age RETURN n")
    Flux<Person> findByAge(@Param("age")Integer age, CompletionStage<AsyncTransaction> transaction);

    @Neo4jSql(sql = "MATCH (n:Person) WHERE n.age = $age AND n.name = $name RETURN n")
    Flux<Person> findByAgeAndName(@Param("age")Integer age, @Param("name")String name, CompletionStage<AsyncTransaction> transaction);
}
