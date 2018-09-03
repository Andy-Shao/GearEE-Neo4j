package org.example.neo4j.dao;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.example.neo4j.Person;
import org.neo4j.driver.v1.Transaction;

import com.github.andyshao.neo4j.annotation.Match;
import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.reflect.annotation.Param;

@Neo4jDao
public interface PersonDao {
    @Match(sql = "MATCH (n:Person) WHERE n.name=$name RETURN n")
    public CompletionStage<Optional<Person>> findByName(@Param("name")String name,@Param("tx") Transaction tx);
}
