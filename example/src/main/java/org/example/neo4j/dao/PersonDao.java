package org.example.neo4j.dao;

import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.neo4j.annotation.Neo4jSql;
import com.github.andyshao.reflect.annotation.Param;
import org.example.neo4j.domain.Person;
import org.neo4j.driver.async.AsyncTransaction;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/9/1
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Neo4jDao
public interface PersonDao {
    @Neo4jSql(sql = "MATCH (p:Person) WHERE p.id = $id RETURN p")
    Mono<Person> findByPk(@Param("id")String id, CompletionStage<AsyncTransaction> transaction);
}
