package org.example.neo4j.dao;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.example.neo4j.domain.Person;
import org.neo4j.driver.v1.Transaction;

import com.github.andyshao.neo4j.annotation.Create;
import com.github.andyshao.neo4j.annotation.Match;
import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.neo4j.annotation.SqlInject;
import com.github.andyshao.neo4j.model.PageReturn;
import com.github.andyshao.neo4j.model.Pageable;
import com.github.andyshao.reflect.annotation.Param;

@Neo4jDao(clipClass = PersonDaoClip.class)
public interface PersonDao {
    @Match(sql = "MATCH (n:Person) WHERE n.name=$name RETURN n")
    CompletionStage<Optional<Person>> findByName(@Param("name")String name,@Param("tx") Transaction tx);
    
    @Match(sql = "MATCH (n:Person {name:$name}) DETACH DELETE n")
    CompletionStage<Void> removeByName(@Param("name") String name,@Param("tx") Transaction tx);
    
    @Create(sql = "MERGE (n:Person {name:$p.name}) SET n.age=$p.age, n.phone=$p.phone RETURN n")
    CompletionStage<Optional<Person>> replace(@Param("p")Person person,@Param("tx") Transaction tx);
    
    @Match(sql = "MATCH (n:Person) RETURN n")
    CompletionStage<PageReturn<Person>> findByPage(@Param("pg")Pageable<Void> pageable,@Param("tx") Transaction tx);
    
    @Match(sql = "MATCH (n:Person) RETURN n")
    CompletionStage<Optional<Long>> totalSize(Transaction tx);
    
    @Match(sql = "MATCH (n:Person) WHERE n.age > $age RETURN n")
    CompletionStage<List<Person>> findByAge(@Param("age")Integer age, @Param("tx") Transaction tx);
    
    @Match(sqlInject = @SqlInject(sqlClipName = "updateSelectiveByPk"))
    CompletionStage<Optional<Person>> updateSelectiveByPk(@Param("person") Person person,@Param("tx") Transaction tx);
    
    default CompletionStage<Optional<Person>> trySave(Person person, Transaction tx){
        return this.findByName(person.getName() , tx).thenComposeAsync(op -> {
            if(op.isPresent()) return CompletableFuture.completedFuture(op);
            return replace(person , tx);
        });
    }
}
