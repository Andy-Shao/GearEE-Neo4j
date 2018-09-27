package com.github.andyshao.neo4j.driver;

import java.util.HashMap;
import java.util.concurrent.CompletionStage;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResultCursor;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.Values;

import lombok.AllArgsConstructor;
import lombok.Data;

public class CreateApiTest {
    @Data
    @AllArgsConstructor
    public static class User {
        private String username;
        private Integer age;
    }
    
    public static void main(String[] args) {
        try(
        Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j" , "1303595"));
        Session session = driver.session();){
//            Entity entity = session.writeTransaction(tx -> {
//                String query = "CREATE "
//                    + "(n:Api {systemAlias: $systemAlias, apiName: $apiName, createTime: $createTime}) "
//                    + "-[:CreateUser]-> "
//                    + "(u:User {name:$name, age:$age}) "
//                    + "RETURN n,u";
//                StatementResult result = tx.run(query,
//                    Values.parameters("createTime", LocalDateTime.now(), 
//                        "systemAlias", "my.system", 
//                        "apiName", "api001",
//                        "name", "andy.shao",
//                        "age", 28)
//                    );
//                return result.single().get(0).asEntity();
//            });
//            entity.asMap().forEach((key,val) -> System.out.println(String.format("%s:%s" , key, val)));
            Transaction tx = session.beginTransaction();
            CompletionStage<StatementResultCursor> cs = tx.runAsync("MATCH (u:User {username:'andy.shao'}) (u2:User {username:'andy.shao'}) RETURN u", 
                Values.value(new HashMap<>()));
            cs.exceptionally(ex -> {
                System.out.println("ERROR!!");
                return null;
            });
            StatementResultCursor src = cs.toCompletableFuture().join();
            src.nextAsync().handleAsync((red, ex)->{
                if(ex != null) {
                    System.out.println("inner error");
                    if(ex instanceof RuntimeException) {
                        RuntimeException re = (RuntimeException) ex;
                        throw re;
                    } else throw new RuntimeException(ex);
                } else {
                    System.out.println("Record is ");
                    return null;
                }
            });
        }
    }
}
