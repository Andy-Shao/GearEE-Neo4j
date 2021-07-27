package org.example.neo4j.dao;

import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.neo4j.annotation.Neo4jSql;
import com.github.andyshao.reflect.annotation.Param;
import org.example.neo4j.dao.impl.PersonDaoClips;
import org.example.neo4j.domain.Person;
import org.example.neo4j.domain.PersonId;
import org.neo4j.driver.async.AsyncTransaction;
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
@Neo4jDao(value = "peopleDao", eneity = Person.class, pk = String.class, clipClass = PersonDaoClips.class)
public interface PersonDao {
    @Neo4jSql(sql = "MATCH (n:Person) WHERE n.id = $pk_id RETURN n")
    Mono<Person> findByPk(@Param("pk") PersonId id, CompletionStage<AsyncTransaction> transaction);

    @Neo4jSql(sql = "CREATE (n:Person {id: $p_id, name: $p_name, age: $p_age, gender: $p_gender}) RETURN n")
    Mono<Person> save(@Param("p") Person person, CompletionStage<AsyncTransaction> tx);

    @Neo4jSql(isUseSqlClip = true, sqlClipName = "saveOrUpdate")
    Mono<Person> saveOrUpdate(@Param("p") Person person, CompletionStage<AsyncTransaction> tx);
}
