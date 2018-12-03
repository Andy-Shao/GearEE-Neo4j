package org.example.neo4j.dao;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.example.neo4j.domain.SystemInfo;
import org.neo4j.driver.v1.Transaction;

import com.github.andyshao.neo4j.annotation.Create;
import com.github.andyshao.neo4j.annotation.Match;
import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.reflect.annotation.Param;

@Neo4jDao
public interface SystemDao {
    @Create(sql = "CREATE (n:System {systemAlias:$sys.systemAlias}) RETURN n")
    CompletionStage<Optional<SystemInfo>> create(@Param("sys")SystemInfo system, @Param("tx")Transaction transaction);
    
    @Match(sql = "MATCH (n:System {systemAlias:$sa}) RETURN n")
    CompletionStage<Optional<SystemInfo>> findByPk(@Param("sa")String systemAlias, @Param("tx")Transaction transaction);
    
    default CompletionStage<Optional<SystemInfo>> trySave(@Param("sys")SystemInfo system, @Param("tx")Transaction transaction){
        return findByPk(system.getSystemAlias() , transaction).thenComposeAsync(op -> {
            return op.isPresent() ? CompletableFuture.completedFuture(op) : create(system , transaction);
        });
    }
}
