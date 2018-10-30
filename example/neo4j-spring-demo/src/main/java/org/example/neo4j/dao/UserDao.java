package org.example.neo4j.dao;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.example.neo4j.domain.User;
import org.neo4j.driver.v1.Transaction;

import com.github.andyshao.neo4j.annotation.Create;
import com.github.andyshao.neo4j.annotation.DeSerializer;
import com.github.andyshao.neo4j.annotation.Match;
import com.github.andyshao.neo4j.annotation.Neo4jDao;
import com.github.andyshao.reflect.annotation.Param;

@Neo4jDao
public interface UserDao {
    @Create(sql = "CREATE (u:User {username:$us.username, password:$us.password, "
        + "createUser:$us.auditRecord.createUser, createTime:$us.auditRecord.createTime, status:$us.status}) RETURN u")
    @DeSerializer(UserDaoDeSerialiazer.class)
    CompletionStage<Optional<User>> create(@Param("us")User user, @Param("tx") Transaction transaction);
    
    @Match(sql = "MATCH (u:User {username:$un}) RETURN u")
    @DeSerializer(UserDaoDeSerialiazer.class)
    CompletionStage<Optional<User>> findByPk(@Param("un")String username, @Param("tx")Transaction transaction);
    
    default CompletionStage<Optional<User>> trySave(User user, Transaction transaction){
        return findByPk(user.getUsername() , transaction).thenComposeAsync(op -> {
            if(op.isPresent()) return CompletableFuture.completedFuture(op);
            
            return create(user, transaction);
        });
    }
    
    @Match(sql = "MATCH (u:User {username:'andy.shao'}) (u2:User {username:'andy.shao'}) RETURN u")
    CompletionStage<Optional<User>> find(Transaction transaction);
    
    @Create(sql = "MATCH (u:User {username:$un}), (s:System {systemAlias:$sysAlias}) CREATE (u)-[:own]->(s)")
    CompletionStage<Void> addRelation(@Param("un") String username, @Param("sysAlias") String systemAlias, @Param("tx") Transaction transaction);
}
