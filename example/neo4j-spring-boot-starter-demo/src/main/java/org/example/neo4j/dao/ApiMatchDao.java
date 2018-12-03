package org.example.neo4j.dao;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.example.neo4j.domain.ApiMatch;
import org.neo4j.driver.v1.Transaction;

import com.github.andyshao.neo4j.annotation.Create;
import com.github.andyshao.neo4j.annotation.DeSerializer;
import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.reflect.annotation.Param;

@Neo4jDao
public interface ApiMatchDao {
    @Create(sql = "CREATE (a:ApiMatch {apiMatchName:$am.apiMatchName}) RETURN a")
    @DeSerializer(ApiMatchDeSerialiazer.class)
    CompletionStage<Optional<ApiMatch>> create(@Param("am")ApiMatch apiMatch, @Param("tx")Transaction transaction);
    
    @Create(sql = "MATCH (a:ApiMatch {apiMatchName:$am}) RETURN a")
    @DeSerializer(ApiMatchDeSerialiazer.class)
    CompletionStage<Optional<ApiMatch>> findByPk(@Param("am")String apiMatchName,@Param("tx") Transaction transaction);
    
    default CompletionStage<Optional<ApiMatch>> trySave(ApiMatch apiMatch, Transaction transaction){
        return findByPk(apiMatch.getApiMatchName() , transaction).thenComposeAsync(op -> {
            return op.isPresent() ? CompletableFuture.completedFuture(op) : create(apiMatch , transaction);
        });
    }
    
    @Create(sql = "MATCH (a:ApiMatch {apiMatchName:$amn}), (s:System {systemAlias:$sa}), (u:User {username:$um}) "
        + "CREATE (a)-[:Belongin]->(s), (u)-[:AddMatch]->(a)")
    CompletionStage<Void> addRelationShip(@Param("amn")String apiMatchName, @Param("sa")String systemAlias, 
        @Param("um")String username, @Param("tx")Transaction transaction);
}
