package org.example.neo4j.dao;

import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.neo4j.annotation.Neo4jSql;
import com.github.andyshao.reflect.annotation.Param;
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
@Neo4jDao("peopleDao")
public interface PersonDao {
    @Neo4jSql(sql = "MATCH (n:Person) WHERE n.id = $pk_id RETURN n")
    Mono<Person> findByPk(@Param("pk") PersonId id, CompletionStage<AsyncTransaction> transaction);
}
